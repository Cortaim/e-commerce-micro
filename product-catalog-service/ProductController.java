package com.myecommerce.product_catalog_service;

import com.myecommerce.product_catalog_service.Product;
import com.myecommerce.product_catalog_service.ProductService;
import com.myecommerce.product_catalog_service.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
    
    @GetMapping("/{id}/stock")
    public ResponseEntity<Integer> getStock(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Продукт не найден
        }
        return ResponseEntity.ok(product.getStock()); 
    }

    
    @PutMapping("/{id}/stock")
    public ResponseEntity<?> reserveProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null || product.getStock() <= 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Продукт не найден или его нет в наличии
        }
        product.setStock(product.getStock() - 1); 
        productService.saveProduct(product); 
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
