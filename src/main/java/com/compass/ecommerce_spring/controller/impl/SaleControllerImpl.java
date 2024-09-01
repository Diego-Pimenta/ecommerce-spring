package com.compass.ecommerce_spring.controller.impl;

import com.compass.ecommerce_spring.controller.SaleController;
import com.compass.ecommerce_spring.dto.request.SaleRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateSaleStatusRequestDto;
import com.compass.ecommerce_spring.dto.response.SaleResponseDto;
import com.compass.ecommerce_spring.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/v1/sales")
public class SaleControllerImpl implements SaleController {

    private final SaleService service;

    @PostMapping
    @Override
    public ResponseEntity<SaleResponseDto> save(@RequestBody @Valid SaleRequestDto saleRequestDto) {
        SaleResponseDto saleResponseDto = service.save(saleRequestDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(saleResponseDto.id()).toUri();
        return ResponseEntity.created(uri).body(saleResponseDto);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<SaleResponseDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Override
    public ResponseEntity<List<SaleResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<SaleResponseDto> update(
            @PathVariable("id") Long id,
            @RequestBody @Valid SaleRequestDto saleRequestDto
    ) {
        return ResponseEntity.ok(service.update(id, saleRequestDto));
    }

    @PatchMapping("/status/{id}")
    @Override
    public ResponseEntity<SaleResponseDto> updateStatus(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateSaleStatusRequestDto updateSaleStatusRequestDto
    ) {
        return ResponseEntity.ok(service.updateStatus(id, updateSaleStatusRequestDto));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
