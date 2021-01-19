package com.sorokin.controller;

import com.sorokin.model.Product;
import com.sorokin.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository repository;

    @Test
    void shouldReturnAllProducts() throws Exception {
        Product product1 = new Product("Chocolate", "Desserts", "Amazon");
        product1.setId(1L);
        Product product2 = new Product("Coke", "Beverage", "Rewe");
        product2.setId(2L);
        List<Product> products = List.of(product1, product2);
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(products));

        this.mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"id\":1,\"name\":\"Chocolate\",\"category\":\"Desserts\",\"seller\":\"Amazon\"},{\"id\":2,\"name\":\"Coke\",\"category\":\"Beverage\",\"seller\":\"Rewe\"}],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":2,\"size\":2,\"number\":0,\"sort\":{\"unsorted\":true,\"sorted\":false,\"empty\":true},\"numberOfElements\":2,\"first\":true,\"empty\":false}"));
    }

    @Test
    void shouldReturnExistedProduct() throws Exception {
        Product product = new Product("Chocolate", "Desserts", "Amazon");
        product.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        this.mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Chocolate\",\"category\":\"Desserts\",\"seller\":\"Amazon\"}"));
    }

    @Test
    void shouldReturnExceptionForNotExistedProductId() throws Exception {
        this.mockMvc.perform(get("/products/123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product with id=123 could not be found"));
    }

    @Test
    void shouldCreateNewUserWithPostRequest() throws Exception {
        Product product = new Product("Chocolate", "Desserts", "Amazon");
        product.setId(3L);
        when(repository.save(product)).thenReturn(product);

        this.mockMvc.perform(post("/products")
                .contentType("application/json")
                .content("{\"id\":3,\"name\":\"Chocolate\",\"category\":\"Desserts\",\"seller\":\"Amazon\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":3,\"name\":\"Chocolate\",\"category\":\"Desserts\",\"seller\":\"Amazon\"}"));
    }

    @Test
    void shouldCreateNewProductWithPutRequest() throws Exception {
        Product newProduct = new Product("Chocolate", "Desserts", "Amazon");
        newProduct.setId(78L);
        when(repository.save(eq(newProduct))).thenReturn(newProduct);

        this.mockMvc.perform(put("/products/78")
                .contentType("application/json")
                .content("{\"id\":79, \"name\":\"Chocolate\",\"category\":\"Desserts\",\"seller\":\"Amazon\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":78, \"name\":\"Chocolate\",\"category\":\"Desserts\",\"seller\":\"Amazon\"}"));
    }

    @Test
    void shouldUpdateExistingUser() throws Exception {
        Product oldProduct = new Product("Chocolate", "Desserts", "Amazon");
        oldProduct.setId(38L);
        Product newProduct = new Product("Ice Cream", "Desserts", "Amazon");
        newProduct.setId(38L);
        when(repository.findById(38L)).thenReturn(Optional.of(oldProduct));
        when(repository.save(eq(newProduct))).thenReturn(newProduct);

        this.mockMvc.perform(put("/products/38")
                .contentType("application/json")
                .content("{\"id\":38, \"name\":\"Ice Cream\",\"category\":\"Desserts\",\"seller\":\"Amazon\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":38, \"name\":\"Ice Cream\",\"category\":\"Desserts\",\"seller\":\"Amazon\"}"));
    }

    @Test
    void shouldNotDeleteWhenProductDoesNotExist() throws Exception {
        this.mockMvc.perform(delete("/products/1234"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Product product = new Product("Chocolate", "Desserts", "Amazon");
        product.setId(117L);
        when(repository.findById(117L)).thenReturn(Optional.of(product));
        this.mockMvc.perform(delete("/products/117"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFilterByNameAndCategory() throws Exception {
        Product product1 = new Product("Chocolate", "Desserts", "Amazon");
        product1.setId(1L);

        when(repository.findByNameIgnoreCaseAndCategoryIgnoreCase(eq("chocolate"), eq("desserts"), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(product1)));

        this.mockMvc.perform(get("/search")
                    .param("name","chocolate")
                    .param("category","desserts"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"id\":1,\"name\":\"Chocolate\",\"category\":\"Desserts\",\"seller\":\"Amazon\"}],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":1,\"size\":1,\"number\":0,\"sort\":{\"unsorted\":true,\"sorted\":false,\"empty\":true},\"numberOfElements\":1,\"first\":true,\"empty\":false}"));
    }
}
