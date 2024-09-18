package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.request.SaleRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateSaleStatusRequestDto;
import com.compass.ecommerce_spring.dto.response.SaleResponseDto;
import com.compass.ecommerce_spring.entity.ProductStock;
import com.compass.ecommerce_spring.entity.SaleItem;
import com.compass.ecommerce_spring.entity.enums.SaleStatus;
import com.compass.ecommerce_spring.exception.custom.BusinessException;
import com.compass.ecommerce_spring.exception.custom.ResourceNotFoundException;
import com.compass.ecommerce_spring.repository.ProductStockRepository;
import com.compass.ecommerce_spring.repository.SaleItemRepository;
import com.compass.ecommerce_spring.repository.SaleRepository;
import com.compass.ecommerce_spring.repository.UserRepository;
import com.compass.ecommerce_spring.security.AccessAuthority;
import com.compass.ecommerce_spring.service.SaleService;
import com.compass.ecommerce_spring.service.mapper.SaleItemMapper;
import com.compass.ecommerce_spring.service.mapper.SaleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final ProductStockRepository productRepository;
    private final SaleItemRepository saleItemRepository;
    private final SaleMapper saleMapper;
    private final SaleItemMapper saleItemMapper;
    private final AccessAuthority accessAuthority;

    @CacheEvict(value = "sales", allEntries = true)
    @Override
    public SaleResponseDto save(SaleRequestDto saleRequestDto) {
        var userCpf = accessAuthority.retrieveUserCpfFromToken();

        var user = userRepository.findByCpf(userCpf).orElseThrow();

        var sale = saleMapper.createSaleToEntity(user);

        var saleItems = saleRequestDto.items().stream()
                .map(item -> {
                    var product = productRepository.findById(item.productId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found in stock or inactive"));
                    return saleItemMapper.toEntity(sale, product, item.quantity());
                })
                .collect(Collectors.toSet());

        validateStock(saleItems);

        var createdSale = saleRepository.save(sale);

        var createdSaleItems = saleItems.stream()
                .map(saleItemRepository::save)
                .collect(Collectors.toSet());

        createdSale.getItems().addAll(createdSaleItems);
        return saleMapper.toDto(createdSale);
    }

    @Cacheable(value = "sales", key = "#id")
    @Transactional(readOnly = true)
    @Override
    public SaleResponseDto findById(Long id) {
        return saleRepository.findById(id)
                .map(saleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
    }

    @Cacheable(value = "sales")
    @Transactional(readOnly = true)
    @Override
    public List<SaleResponseDto> findAll(Integer page, Integer size, String orderBy, String direction) {
        var pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);

        return saleRepository.findAll(pageRequest).stream()
                .map(saleMapper::toDto)
                .collect(Collectors.toList());
    }

    @CachePut(value = "sales", key = "#id")
    @Override
    public SaleResponseDto update(Long id, SaleRequestDto saleRequestDto) {
        var sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        checkCancelledSale(sale.getStatus());
        checkProcessedSale(sale.getStatus());

        accessAuthority.checkPermission(sale.getCustomer().getCpf());

        var saleItems = saleRequestDto.items().stream()
                .map(item -> {
                    var product = productRepository.findById(item.productId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found in stock or inactive"));
                    return saleItemMapper.toEntity(sale, product, item.quantity());
                })
                .collect(Collectors.toSet());

        validateStock(saleItems);

        saleItemRepository.deleteAll(sale.getItems());
        sale.getItems().clear();

        var updatedSaleItems = saleItems.stream()
                .map(saleItemRepository::save)
                .collect(Collectors.toSet());

        var updatedSale = saleMapper.updateSaleToEntity(sale, updatedSaleItems);
        return saleMapper.toDto(updatedSale);
    }

    @Caching(
            put = @CachePut(value = "sales", key = "#id"),
            evict = @CacheEvict(value = "products", allEntries = true)
    )
    @Override
    public SaleResponseDto updateStatus(Long id, UpdateSaleStatusRequestDto updateSaleStatusRequestDto) {
        var sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        checkCompletedSale(sale.getStatus());
        checkCancelledSale(sale.getStatus());
        checkRegressingSaleStatus(sale.getStatus(), updateSaleStatusRequestDto.status());

        accessAuthority.checkPermission(sale.getCustomer().getCpf());

        // verifica se a venda foi cancelada depois de paga e antes de concluída
        if (updateSaleStatusRequestDto.status().equals(SaleStatus.CANCELLED) && !sale.getStatus().equals(SaleStatus.WAITING_PAYMENT)) {
            sale.getItems().forEach(item -> addToStock(item.getId().getProduct(), item.getQuantity()));
        }

        // desconta os produtos do estoque apenas quando sair do status de WAITING_PAYMENT
        if (sale.getStatus().equals(SaleStatus.WAITING_PAYMENT) &&
                !updateSaleStatusRequestDto.status().equals(SaleStatus.CANCELLED) &&
                !updateSaleStatusRequestDto.status().equals(SaleStatus.WAITING_PAYMENT)) {
            validateStock(sale.getItems());
            sale.getItems().forEach(item -> removeFromStock(item.getId().getProduct(), item.getQuantity()));
        }

        sale.setStatus(updateSaleStatusRequestDto.status());
        var updatedSale = saleRepository.save(sale);
        updatedSale.getItems().addAll(sale.getItems());
        return saleMapper.toDto(updatedSale);
    }

    @CacheEvict(value = {"sales", "products"}, allEntries = true)
    @Override
    public void delete(Long id) {
        var sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        // devolve os produtos ao estoque caso tenham sido separados e a venda não foi previamente cancelada
        if (!sale.getStatus().equals(SaleStatus.CANCELLED) &&
                sale.getStatus().equals(SaleStatus.PAID) ||
                sale.getStatus().equals(SaleStatus.SHIPPED)) {
            sale.getItems().forEach(item -> addToStock(item.getId().getProduct(), item.getQuantity()));
        }

        saleItemRepository.deleteAll(sale.getItems());
        saleRepository.delete(sale);
    }

    private void validateStock(Set<SaleItem> saleItems) {
        var insufficientProducts = saleItems.stream()
                .filter(item -> item.getId().getProduct().getQuantity() < item.getQuantity() ||
                        !item.getId().getProduct().getActive())
                .map(item -> item.getId().getProduct().getName())
                .toList();

        if (!insufficientProducts.isEmpty()) {
            throw new BusinessException("Inactive or unavailable quantity of products: " + String.join(", ", insufficientProducts));
        }
    }

    private void addToStock(ProductStock product, Integer quantity) {
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }

    private void removeFromStock(ProductStock product, Integer quantity) {
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    private void checkCompletedSale(SaleStatus status) {
        if (status.equals(SaleStatus.DONE)) {
            throw new BusinessException("Sale was already completed");
        }
    }

    private void checkCancelledSale(SaleStatus status) {
        if (status.equals(SaleStatus.CANCELLED)) {
            throw new BusinessException("Sale was cancelled");
        }
    }

    private void checkProcessedSale(SaleStatus status) {
        if (status.equals(SaleStatus.PAID) || status.equals(SaleStatus.SHIPPED) || status.equals(SaleStatus.DONE)) {
            throw new BusinessException("Sale was already processed");
        }
    }

    private void checkRegressingSaleStatus(SaleStatus status, SaleStatus newStatus) {
        if (status.equals(SaleStatus.SHIPPED) && newStatus.equals(SaleStatus.PAID)) {
            throw new BusinessException("Sale cannot regress status");
        }
    }
}
