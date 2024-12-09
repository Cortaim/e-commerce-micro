package com.myecommerce.order_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }


    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            if (!orderService.isUserValid(order.getUserId())) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404, если пользователь не найден
            }

            order.setStatus("CREATED");
            Order createdOrder = orderService.createOrder(order); 

            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED); 
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400, ошибка запроса
        }
    }


    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
    
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @RequestBody String newStatus) {
        boolean updated = orderService.updateOrderStatus(orderId, newStatus);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
