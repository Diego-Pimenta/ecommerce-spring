package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.request.CreateSaleRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateSaleRequestDto;
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
import com.compass.ecommerce_spring.service.SaleService;
import com.compass.ecommerce_spring.service.mapper.SaleItemMapper;
import com.compass.ecommerce_spring.service.mapper.SaleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = "sales", allEntries = true)
    @Override
    public SaleResponseDto save(CreateSaleRequestDto createSaleRequestDto) {
        var user = userRepository.findByCpf(createSaleRequestDto.customerCpf())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        var sale = saleMapper.createSaleToEntity(user);

        var saleItems = createSaleRequestDto.items()
                .stream()
                .map(item -> {
                    var product = productRepository.findByIdAndActive(item.productId(), true)
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found in stock"));
                    return saleItemMapper.toEntity(sale, product, item.quantity());
                })
                .collect(Collectors.toSet());

        validateStock(saleItems);

        var createdSale = saleRepository.save(sale);

        var createdSaleItems = saleItems
                .stream()
                .map(saleItemRepository::save)
                .collect(Collectors.toSet());

        createdSale.setItems(createdSaleItems);
        return saleMapper.toDto(createdSale);
    }

    @Cacheable(value = "sales", key = "#id")
    @Transactional(readOnly = true)
    @Override
    public SaleResponseDto findById(Long id) {
        return saleRepository.findByIdFetchItems(id)
                .map(saleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
    }

    // TODO: HttpMessageNotWritableException: Could not write JSON: object is not an instance of declaring class
    @Cacheable(value = "sales")
    @Transactional(readOnly = true)
    @Override
    public List<SaleResponseDto> findAll() {
        return saleRepository.findAllFetchItems()
                .stream()
                .map(saleMapper::toDto)
                .collect(Collectors.toList());
    }

    @CachePut(value = "sales", key = "#id")
    @Override
    public SaleResponseDto update(Long id, UpdateSaleRequestDto updateSaleRequestDto) {
        var sale = saleRepository.findByIdFetchItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        // só é possível atualizar os itens da venda caso não tenha sido paga
        if (sale.getStatus().equals(SaleStatus.PAID) ||
                sale.getStatus().equals(SaleStatus.SHIPPED) ||
                sale.getStatus().equals(SaleStatus.DONE)
        ) {
            throw new BusinessException("Sale was already processed");
        }
        if (sale.getStatus().equals(SaleStatus.CANCELLED)) {
            throw new BusinessException("Sale was cancelled");
        }

        var existingSaleItems = sale.getItems()
                .stream()
                .map(item -> {
                    var product = productRepository.findByIdAndActive(item.getId().getProduct().getId(), true)
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found in stock"));
                    return saleItemMapper.toEntity(sale, product, item.getQuantity());
                })
                .collect(Collectors.toSet());

        var saleItems = updateSaleRequestDto.items()
                .stream()
                .map(item -> {
                    var product = productRepository.findByIdAndActive(item.productId(), true)
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found in stock"));
                    return saleItemMapper.toEntity(sale, product, item.quantity());
                })
                .collect(Collectors.toSet());

        validateStock(saleItems);

        // TODO: ObjectOptimisticLockingFailureException: Row was updated or deleted by another transaction
        existingSaleItems.forEach(item -> saleItemRepository.deleteBySaleId(sale.getId()));

        var updatedSaleItems = saleItems
                .stream()
                .map(saleItemRepository::save)
                .collect(Collectors.toSet());

        var updatedSale = saleMapper.updateSaleToEntity(sale, updatedSaleItems);
        return saleMapper.toDto(updatedSale);
    }

    @CachePut(value = "sales", key = "#id")
    @Override
    public SaleResponseDto updateStatus(Long id, UpdateSaleStatusRequestDto updateSaleStatusRequestDto) {
        var sale = saleRepository.findByIdFetchItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        if (updateSaleStatusRequestDto.status().equals(SaleStatus.WAITING_PAYMENT)) { // importante para funcionar o 5° if
            throw new BusinessException("Status already in use");
        }
        if (sale.getStatus().equals(SaleStatus.DONE)) {
            throw new BusinessException("Sale was already completed");
        }
        if (sale.getStatus().equals(SaleStatus.CANCELLED)) {
            throw new BusinessException("Sale was cancelled");
        }

        // verifica se a venda foi cancelada depois de paga e antes de concluída
        if (updateSaleStatusRequestDto.status().equals(SaleStatus.CANCELLED) && !sale.getStatus().equals(SaleStatus.WAITING_PAYMENT)) {
            sale.getItems().forEach(item -> addToStock(item.getId().getProduct(), item.getQuantity()));
        }

        validateStock(sale.getItems()); // tem que rodar antes de tirar os produtos do estoque

        // desconta os produtos do estoque apenas quando sair do status de WAITING_PAYMENT
        if (sale.getStatus().equals(SaleStatus.WAITING_PAYMENT) && !updateSaleStatusRequestDto.status().equals(SaleStatus.CANCELLED)) {
            sale.getItems().forEach(item -> removeFromStock(item.getId().getProduct(), item.getQuantity()));
        }

        sale.setStatus(updateSaleStatusRequestDto.status());
        var updatedSale = saleRepository.save(sale);
        updatedSale.setItems(sale.getItems());
        return saleMapper.toDto(updatedSale);
    }

    @CacheEvict(value = "sales", allEntries = true)
    @Override
    public void delete(Long id) {
        var sale = saleRepository.findByIdFetchItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        if (sale.getStatus().equals(SaleStatus.DONE)) {
            throw new BusinessException("Sale was already completed");
        }

        // devolve os produtos ao estoque caso tenham sido removidos
        if (sale.getStatus().equals(SaleStatus.PAID) ||
                sale.getStatus().equals(SaleStatus.SHIPPED)
        ) {
            sale.getItems().forEach(item -> addToStock(item.getId().getProduct(), item.getQuantity()));
        }

        sale.setStatus(SaleStatus.CANCELLED);
        saleRepository.save(sale);
    }

    private void validateStock(Set<SaleItem> saleItems) {
        var insufficientStock = saleItems
                .stream()
                .anyMatch(item -> !isProductAvailableInStock(item.getId().getProduct(), item.getQuantity()));

        if (insufficientStock) {
            throw new BusinessException("Insufficient stock of products");
        }
    }

    private boolean isProductAvailableInStock(ProductStock product, Integer quantity) {
        return product.getQuantity() >= quantity;
    }

    private void removeFromStock(ProductStock product, Integer quantity) {
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    private void addToStock(ProductStock product, Integer quantity) {
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }
}
