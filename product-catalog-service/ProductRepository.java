package com.myecommerce.product_catalog_service;

import com.myecommerce.product_catalog_service.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

