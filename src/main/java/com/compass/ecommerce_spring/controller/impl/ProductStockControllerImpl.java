package com.compass.ecommerce_spring.controller.impl;

import com.compass.ecommerce_spring.controller.ProductStockController;
import com.compass.ecommerce_spring.dto.request.CreateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;
import com.compass.ecommerce_spring.service.ProductStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/products")
public class ProductStockControllerImpl implements ProductStockController {

    private final ProductStockService service;

    @PostMapping
    @Override
    public ResponseEntity<ProductStockResponseDto> save(@RequestBody @Valid CreateProductStockRequestDto createProductStockRequestDto) {
        ProductStockResponseDto productStockResponseDto = service.save(createProductStockRequestDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(productStockResponseDto.id()).toUri();
        return ResponseEntity.created(uri).body(productStockResponseDto);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ProductStockResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ProductStockResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ProductStockResponseDto> update(@PathVariable Long id, @RequestBody @Valid UpdateProductStockRequestDto updateProductStockRequestDto) {
        return ResponseEntity.ok(service.update(id, updateProductStockRequestDto));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
