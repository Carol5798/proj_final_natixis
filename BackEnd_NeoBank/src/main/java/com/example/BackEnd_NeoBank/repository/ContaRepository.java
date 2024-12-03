package com.example.BackEnd_NeoBank.repository;

import com.example.BackEnd_NeoBank.entity.ContaBancaria;
import com.example.BackEnd_NeoBank.entity.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository  extends JpaRepository<ContaBancaria, Long> {
    Optional<ContaBancaria> findById(long id);

    // Verificar se o utilizador já tem uma conta
    boolean existsByUtilizador_Id(Long idUtilizador);

    // Verificar se um IBAN já existe
    boolean existsByIban(String iban);

    Optional<ContaBancaria> findByUtilizador_Id(Long idUtilizador);

    Optional<ContaBancaria> findByIban(String iban);
    Optional<ContaBancaria> findByEntidade(String entidade);
}

