package com.sorokin.controller;

import com.sorokin.model.Product;
import com.sorokin.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> all(Pageable pageable) {
        return ResponseEntity
                .ok(this.productRepository.findAll(pageable));
    }

    @GetMapping("products/{id}")
    public ResponseEntity<Product> one(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity.ok()::body)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> findByNameAndCategory(@RequestParam String name,
                                                               @RequestParam String category,
                                                               Pageable pageable) {
        return ResponseEntity
                .ok(this.productRepository.findByNameIgnoreCaseAndCategoryIgnoreCase(name, category, pageable));
    }

    @PostMapping("/products")
    ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.productRepository.save(product));
    }

    @PutMapping("/products/{id}")
    ResponseEntity<Product> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        return this.productRepository
                .findById(id)
                .map(foundProduct -> {
                    foundProduct.setName(newProduct.getName());
                    foundProduct.setCategory(newProduct.getCategory());
                    foundProduct.setSeller(newProduct.getSeller());
                    return ResponseEntity
                            .ok(this.productRepository.save(foundProduct));
                }).orElseGet(() -> {
                    newProduct.setId(id);
                    return ResponseEntity
                            .ok(this.productRepository.save(newProduct));
                });
    }

    @DeleteMapping("/products/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return this.productRepository
                .findById(id)
                .map(foundProduct -> {
                    this.productRepository.delete(foundProduct);
                    return ResponseEntity
                            .ok()
                            .build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
