package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.request.SaleRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateSaleStatusRequestDto;
import com.compass.ecommerce_spring.dto.response.SaleResponseDto;
import com.compass.ecommerce_spring.entity.ProductStock;
import com.compass.ecommerce_spring.entity.SaleItem;
import com.compass.ecommerce_spring.entity.enums.Role;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public SaleResponseDto save(SaleRequestDto saleRequestDto) {
        var userCpf = retrieveUserCpfFromToken();

        var user = userRepository.findByCpf(userCpf).orElseThrow();

        var sale = saleMapper.createSaleToEntity(user);

        var saleItems = saleRequestDto.items()
                .stream()
                .map(item -> {
                    var product = productRepository.findByIdAndActive(item.productId(), true)
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found in stock or inactive"));
                    return saleItemMapper.toEntity(sale, product, item.quantity());
                })
                .collect(Collectors.toSet());

        validateStock(saleItems);

        var createdSale = saleRepository.save(sale);

        var createdSaleItems = saleItems
                .stream()
                .map(saleItemRepository::save)
                .collect(Collectors.toSet());

        createdSale.getItems().addAll(createdSaleItems);
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
    public SaleResponseDto update(Long id, SaleRequestDto saleRequestDto) {
        var sale = saleRepository.findByIdFetchItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        checkCancelledSale(sale.getStatus());
        checkProcessedSale(sale.getStatus());
        checkPermission(sale.getCustomer().getCpf());

        var saleItems = saleRequestDto.items()
                .stream()
                .map(item -> {
                    var product = productRepository.findByIdAndActive(item.productId(), true)
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found in stock or inactive"));
                    return saleItemMapper.toEntity(sale, product, item.quantity());
                })
                .collect(Collectors.toSet());

        validateStock(saleItems);

        saleItemRepository.deleteAll(sale.getItems());
        sale.getItems().clear();

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

        checkCompletedSale(sale.getStatus());
        checkCancelledSale(sale.getStatus());
        checkRegressingStatus(sale.getStatus(), updateSaleStatusRequestDto.status());
        checkPermission(sale.getCustomer().getCpf());

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

    @CacheEvict(value = "sales", allEntries = true)
    @Override
    public void delete(Long id) {
        var sale = saleRepository.findByIdFetchItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        checkCompletedSale(sale.getStatus());

        // devolve os produtos ao estoque caso tenham sido separados
        if (sale.getStatus().equals(SaleStatus.PAID) || sale.getStatus().equals(SaleStatus.SHIPPED)) {
            sale.getItems().forEach(item -> addToStock(item.getId().getProduct(), item.getQuantity()));
        }

        sale.setStatus(SaleStatus.CANCELLED);
        saleRepository.save(sale);
    }

    private void validateStock(Set<SaleItem> saleItems) {
        var insufficientProduct = saleItems
                .stream()
                .filter(item -> item.getId().getProduct().getQuantity() < item.getQuantity())
                .findFirst();

        if (insufficientProduct.isPresent()) {
            var productName = insufficientProduct.get().getId().getProduct().getName();
            throw new BusinessException("Unavailable quantity of product: " + productName);
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

    private String retrieveUserCpfFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    private Role retrieveUserRoleFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .findFirst()
                .orElseThrow();
    }

    private void checkPermission(String cpf) {
        if (retrieveUserRoleFromToken().equals(Role.CLIENT) && !retrieveUserCpfFromToken().equals(cpf)) {
            throw new BusinessException("Sale belongs to another user");
        }
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

    private void checkRegressingStatus(SaleStatus status, SaleStatus newStatus) {
        if (status.equals(SaleStatus.SHIPPED) && newStatus.equals(SaleStatus.PAID)) {
            throw new BusinessException("Sale cannot regress status");
        }
    }
}
