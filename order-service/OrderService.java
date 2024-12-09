package com.myecommerce.order_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Order order) {
        if (!validateAndReserveProducts(order.getProductIds())) {
            throw new RuntimeException("One or more products are unavailable");
        }
        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public boolean isUserValid(Long userId) {
        String userServiceUrl = "http://user-service.default.svc.cluster.local:8080/users/" + userId;
        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(userServiceUrl, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    // Проверка, имеет ли заказ статус PAID
    public boolean isOrderPaid(Long orderId) {
        Order order = getOrderById(orderId);
        return "PAID".equalsIgnoreCase(order.getStatus());
    }
    
    public boolean updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(newStatus);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    public boolean validateAndReserveProducts(List<Long> productIds) {
        for (Long productId : productIds) {
            if (!isProductAvailable(productId)) {
                return false; 
            }

            reserveProduct(productId);
        }
        return true;
    }
    
    private boolean isProductAvailable(Long productId) {
        String productServiceUrl = "http://product-catalog-service.default.svc.cluster.local:8081/products/" + productId + "/stock";
        try {
            ResponseEntity<Integer> response = restTemplate.getForEntity(productServiceUrl, Integer.class);
            Integer stock = response.getBody();
            return stock != null && stock > 0; 
        } catch (HttpClientErrorException.NotFound e) {
            return false; 
        }
    }

    private void reserveProduct(Long productId) {
        String productServiceUrl = "http://product-catalog-service.default.svc.cluster.local:8081/products/" + productId + "/stock";
        try {
            restTemplate.put(productServiceUrl, null);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to reserve product with ID: " + productId, e);
        }
    }
    
}
