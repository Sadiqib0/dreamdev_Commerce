package org.dreamcommerce.dreamCommerce.controller;

import org.springframework.test.context.ActiveProfiles;
import tools.jackson.databind.ObjectMapper;
import org.dreamcommerce.dreamCommerce.dtos.requests.AddProductRequest;
import org.dreamcommerce.dreamCommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = {"test"})
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName(

            """
            Given:

            When:


            Check:

            """
    )
    void testAddProduct(){
try {
    AddProductRequest productRequest = new AddProductRequest();
    mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsBytes(productRequest)))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andDo(MockMvcResultHandlers.print());
} catch (Exception e) {
    assertNull(e);
}
    }
}
