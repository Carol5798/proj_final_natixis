package com.example.BackEnd_NeoBank.repository;

import com.example.BackEnd_NeoBank.entity.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {
    Optional<Utilizador> findByEmailOrNif(String email, String nif);
    Optional<Utilizador> findByNif(String nif);
    Optional<Utilizador> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNif(String nif);

    boolean existsById(long id);
    Optional<Utilizador> findById(long id);
}
