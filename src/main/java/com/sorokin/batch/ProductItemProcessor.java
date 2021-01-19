package com.sorokin.batch;

import com.sorokin.model.Product;

import org.springframework.batch.item.ItemProcessor;

public class ProductItemProcessor implements ItemProcessor<Product, Product> {
    @Override
    public Product process(Product product) {
        throw new UnsupportedOperationException();
    }
}
