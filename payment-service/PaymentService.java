package com.myecommerce.payment_service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Payment createPayment(Long orderId) {
        if (!isOrderValid(orderId)) {
            throw new RuntimeException("Invalid order ID: " + orderId);
        }

        if (isOrderAlreadyPaid(orderId)) {
            throw new RuntimeException("Order with ID " + orderId + " has already been paid.");
        }

        double orderAmount = getOrderAmount(orderId);

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(orderAmount);
        payment.setStatus("COMPLETED");
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        updateOrderStatus(orderId, "PAID");

        return savedPayment;
    }


    private double getOrderAmount(Long orderId) {
        String orderServiceUrl = "http://order-service.default.svc.cluster.local:8082/orders/" + orderId;
        try {
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(orderServiceUrl, JsonNode.class);
            JsonNode orderJson = response.getBody();

            if (orderJson != null && orderJson.has("totalPrice")) {
                return orderJson.get("totalPrice").asDouble();
            } else {
                throw new RuntimeException("Order totalPrice not found for order ID: " + orderId);
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
    }

    private boolean isOrderValid(Long orderId) {
        String orderServiceUrl = "http://order-service.default.svc.cluster.local:8082/orders/" + orderId;
        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(orderServiceUrl, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false; // Заказ не найден
        }
    }

    private boolean isOrderAlreadyPaid(Long orderId) {
        String orderServiceUrl = "http://order-service.default.svc.cluster.local:8082/orders/" + orderId;
        try {
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(orderServiceUrl, JsonNode.class);
            JsonNode orderJson = response.getBody();

            if (orderJson != null && orderJson.has("status")) {
                String status = orderJson.get("status").asText();
                return "PAID".equalsIgnoreCase(status);
            }
            return false;
        } catch (HttpClientErrorException.NotFound e) {
            return false; 
        }
    }
    
    private void updateOrderStatus(Long orderId, String newStatus) {
        String orderServiceUrl = "http://order-service.default.svc.cluster.local:8082/orders/" + orderId + "/status";
        try {
            restTemplate.put(orderServiceUrl, newStatus); // Отправка PUT-запроса
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to update order status for order ID: " + orderId, e);
        }
    }


    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}

