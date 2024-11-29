package com.example.BackEnd_NeoBank.repository;

import com.example.BackEnd_NeoBank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailOrNif(String email, String nif);
    boolean existsByEmail(String email);
    boolean existsByNif(String nif);
}
