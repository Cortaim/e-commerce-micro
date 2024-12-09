package com.myecommerce.user_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return userService.createUser(user);
    }

@GetMapping("/{id}")
public ResponseEntity<Void> checkUserExists(@PathVariable Long id) {
	System.out.println("Checking user existence for ID: " + id);
    boolean exists = userService.existsById(id);
    System.out.println("User exists: " + exists);
    return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}

