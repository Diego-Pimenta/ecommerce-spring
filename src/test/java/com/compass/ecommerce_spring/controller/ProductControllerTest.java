package com.compass.ecommerce_spring.controller;

import com.compass.ecommerce_spring.config.SecurityTestConfig;
import com.compass.ecommerce_spring.controller.impl.ProductStockControllerImpl;
import com.compass.ecommerce_spring.dto.request.CreateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockRequestDto;
import com.compass.ecommerce_spring.exception.custom.ResourceAlreadyExistsException;
import com.compass.ecommerce_spring.exception.custom.ResourceNotFoundException;
import com.compass.ecommerce_spring.service.impl.ProductStockServiceImpl;
import com.compass.ecommerce_spring.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.compass.ecommerce_spring.common.ProductConstants.CREATE_PRODUCT_REQUEST;
import static com.compass.ecommerce_spring.common.ProductConstants.PRODUCTS_RESPONSE;
import static com.compass.ecommerce_spring.common.ProductConstants.PRODUCT_RESPONSE;
import static com.compass.ecommerce_spring.common.ProductConstants.UPDATE_PRODUCT_REQUEST;
import static com.compass.ecommerce_spring.common.UserConstants.USER_DETAILS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityTestConfig.class)
@WebMvcTest(ProductStockControllerImpl.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductStockServiceImpl service;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private UserDetailsService userDetailsService;

    private String token;

    @BeforeEach
    public void setup() {
        when(userDetailsService.loadUserByUsername(any())).thenReturn(USER_DETAILS);
        when(jwtUtil.createToken(USER_DETAILS)).thenReturn("valid-token");

        token = "Bearer " + jwtUtil.createToken(USER_DETAILS);
    }

    @BeforeEach
    public void auth() {
        var authentication = new UsernamePasswordAuthenticationToken(
                USER_DETAILS,
                null,
                USER_DETAILS.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void saveProduct_WithValidData_ReturnsCreated() throws Exception {
        when(service.save(CREATE_PRODUCT_REQUEST)).thenReturn(PRODUCT_RESPONSE);

        mockMvc.perform(post("/v1/products")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_PRODUCT_REQUEST)))
                .andExpect(authenticated())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(PRODUCT_RESPONSE.id()))
                .andExpect(jsonPath("$.name").value(PRODUCT_RESPONSE.name()))
                .andDo(print());
    }

    @Test
    public void saveProduct_WithInvalidData_ReturnsBadRequest() throws Exception {
        var invalidCreateRequest = new CreateProductStockRequestDto("", 0, BigDecimal.ZERO, "");

        mockMvc.perform(post("/v1/products")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateRequest)))
                .andExpect(authenticated())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveProduct_WithExistingName_ReturnsConflict() throws Exception {
        when(service.save(any())).thenThrow(ResourceAlreadyExistsException.class);

        mockMvc.perform(post("/v1/products")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_PRODUCT_REQUEST)))
                .andExpect(authenticated())
                .andExpect(status().isConflict());
    }

    @Test
    public void getProduct_WithExistingId_ReturnsProductResponseDto() throws Exception {
        when(service.findById(1L)).thenReturn(PRODUCT_RESPONSE);

        mockMvc.perform(get("/v1/products/1")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    public void getProduct_WithNonexistentId_ReturnsNotFound() throws Exception {
        when(service.findById(0L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/v1/products/0")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(authenticated())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllProducts_ReturnsListOfProductResponseDto() throws Exception {
        when(service.findAll(0, 10, "name", "ASC")).thenReturn(PRODUCTS_RESPONSE);

        mockMvc.perform(get("/v1/products")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productStockResponseDtoList[0].id").value(PRODUCTS_RESPONSE.get(0).id()))
                .andExpect(jsonPath("$._embedded.productStockResponseDtoList[0].name").value(PRODUCTS_RESPONSE.get(0).name()));
    }

    @Test
    public void updateProduct_WithExistingId_ReturnsProductResponseDto() throws Exception {
        when(service.update(1L, UPDATE_PRODUCT_REQUEST)).thenReturn(PRODUCT_RESPONSE);

        mockMvc.perform(put("/v1/products/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_PRODUCT_REQUEST)))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PRODUCT_RESPONSE.id()))
                .andExpect(jsonPath("$.name").value(PRODUCT_RESPONSE.name()))
                .andDo(print());
    }

    @Test
    public void updateProduct_WithNonexistentId_ReturnsNotFound() throws Exception {
        var invalidUpdateRequest = new UpdateProductStockRequestDto("", 0, BigDecimal.ZERO, "", false);

        mockMvc.perform(put("/v1/products/0")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateRequest)))
                .andExpect(authenticated())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateProduct_WithExistingName_ReturnsConflict() throws Exception {
        when(service.update(any(), any())).thenThrow(ResourceAlreadyExistsException.class);

        mockMvc.perform(put("/v1/products/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_PRODUCT_REQUEST)))
                .andExpect(authenticated())
                .andExpect(status().isConflict());
    }

    @Test
    public void deleteProduct_WithExistingId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/v1/products/1")
                        .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteProduct_WithNonexistentId_ReturnsNotFound() throws Exception {
        doThrow(ResourceNotFoundException.class).when(service).delete(0L);

        mockMvc.perform(delete("/v1/products/0")
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }
}
