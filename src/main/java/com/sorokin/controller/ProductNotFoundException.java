package com.sorokin.controller;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super(String.format("Product with id=%d could not be found", id));
    }
}
