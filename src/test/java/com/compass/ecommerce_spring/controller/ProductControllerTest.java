package com.compass.ecommerce_spring.controller;

import com.compass.ecommerce_spring.controller.impl.ProductStockControllerImpl;
import com.compass.ecommerce_spring.service.impl.ProductStockServiceImpl;
import com.compass.ecommerce_spring.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.compass.ecommerce_spring.common.ProductConstants.CREATE_PRODUCT_REQUEST_DTO;
import static com.compass.ecommerce_spring.common.ProductConstants.PRODUCT_RESPONSE_DTO;
import static com.compass.ecommerce_spring.common.UserConstants.USER_DETAILS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductStockControllerImpl.class)
class ProductControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductStockServiceImpl service;
    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    public void auth() {
        var authentication = new UsernamePasswordAuthenticationToken(USER_DETAILS, null, USER_DETAILS.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void saveProduct_WithValidData_ReturnsCreated() throws Exception {
        var token = "Bearer valid_token";

        when(jwtUtil.createToken(any())).thenReturn(token);
        when(jwtUtil.isTokenValid(token)).thenReturn(true);

        when(service.save(CREATE_PRODUCT_REQUEST_DTO)).thenReturn(PRODUCT_RESPONSE_DTO);

        // keep getting 403
        mockMvc.perform(post("/v1/products")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_PRODUCT_REQUEST_DTO)))
                .andExpect(authenticated())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PRODUCT_RESPONSE_DTO))
                .andDo(print());
    }

    @Test
    public void saveProduct_WithInvalidData_ReturnsBadRequest() throws Exception {
    }

    @Test
    public void saveProduct_WithExistingName_ReturnsConflict() throws Exception {
    }

    @Test
    public void getProduct_WithExistingId_ReturnsProductResponseDto() throws Exception {
    }

    @Test
    public void getProduct_WithNonexistentId_ReturnsNotFound() throws Exception {
    }

    @Test
    public void getAllProducts_ReturnsListOfProductResponseDto() throws Exception {
    }

    @Test
    public void updateProduct_WithExistingId_ReturnsProductResponseDto() throws Exception {
    }

    @Test
    public void updateProduct_WithNonexistentId_ReturnsNotFound() throws Exception {
    }

    @Test
    public void updateProduct_WithExistingName_ReturnsConflict() throws Exception {
    }

    @Test
    public void updateProductStatus_WithExistingId_ReturnsProductResponseDto() throws Exception {
    }

    @Test
    public void updateProductStatus_WithNonexistentId_ReturnsNotFound() throws Exception {
    }

    @Test
    public void deleteProduct_WithExistingId_ReturnsNoContent() throws Exception {
    }

    @Test
    public void deleteProduct_WithNonexistentId_ReturnsNotFound() throws Exception {
    }
}
