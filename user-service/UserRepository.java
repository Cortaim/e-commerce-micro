package com.myecommerce.user_service;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(Long id);

    User findByEmailAndPassword(String email, String password);
}
