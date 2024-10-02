package com.compass.ecommerce_spring.controller.impl;

import com.compass.ecommerce_spring.controller.ProductStockController;
import com.compass.ecommerce_spring.dto.request.CreateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateActiveStatusRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;
import com.compass.ecommerce_spring.service.ProductStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/products")
public class ProductStockControllerImpl implements ProductStockController {

    private final ProductStockService service;

    @PostMapping
    @Override
    public ResponseEntity<EntityModel<ProductStockResponseDto>> save(@RequestBody @Valid CreateProductStockRequestDto createProductStockRequestDto) {
        var product = service.save(createProductStockRequestDto);
        var productModel = EntityModel.of(product);

        productModel.add(linkTo(methodOn(ProductStockControllerImpl.class).findById(product.id())).withSelfRel());
        productModel.add(linkTo(methodOn(ProductStockControllerImpl.class).findAll(0, 10, "name", "ASC")).withRel("products"));

        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(product.id()).toUri();

        return ResponseEntity.created(uri).body(productModel);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<EntityModel<ProductStockResponseDto>> findById(@PathVariable("id") Long id) {
        var product = service.findById(id);
        var productModel = EntityModel.of(product);

        productModel.add(linkTo(methodOn(ProductStockControllerImpl.class).findById(id)).withSelfRel());
        productModel.add(linkTo(methodOn(ProductStockControllerImpl.class).findAll(0, 10, "name", "ASC")).withRel("products"));

        return ResponseEntity.ok(productModel);
    }

    @GetMapping
    @Override
    public ResponseEntity<CollectionModel<EntityModel<ProductStockResponseDto>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {
        var products = service.findAll(page, size, orderBy, direction);

        var productModels = products.stream()
                .map(product -> EntityModel.of(product,
                        linkTo(methodOn(ProductStockControllerImpl.class).findById(product.id())).withSelfRel()))
                .toList();

        var collectionModel = CollectionModel.of(productModels,
                linkTo(methodOn(ProductStockControllerImpl.class).findAll(page, size, orderBy, direction)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<EntityModel<ProductStockResponseDto>> update(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateProductStockRequestDto updateProductStockRequestDto
    ) {
        var product = service.update(id, updateProductStockRequestDto);
        var productModel = EntityModel.of(product);

        productModel.add(linkTo(methodOn(ProductStockControllerImpl.class).findById(id)).withSelfRel());
        productModel.add(linkTo(methodOn(ProductStockControllerImpl.class).findAll(0, 10, "name", "ASC")).withRel("products"));

        return ResponseEntity.ok(productModel);
    }

    @PatchMapping("/status/{id}")
    @Override
    public ResponseEntity<EntityModel<ProductStockResponseDto>> updateStatus(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateActiveStatusRequestDto updateActiveStatusRequestDto
    ) {
        var product = service.updateStatus(id, updateActiveStatusRequestDto);
        var productModel = EntityModel.of(product);

        productModel.add(linkTo(methodOn(ProductStockControllerImpl.class).findById(id)).withSelfRel());
        productModel.add(linkTo(methodOn(ProductStockControllerImpl.class).findAll(0, 10, "name", "ASC")).withRel("products"));

        return ResponseEntity.ok(productModel);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
