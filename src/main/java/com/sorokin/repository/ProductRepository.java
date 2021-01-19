package com.sorokin.repository;

import com.sorokin.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameIgnoreCaseAndCategoryIgnoreCase(String name, String category, Pageable pageable);
}
