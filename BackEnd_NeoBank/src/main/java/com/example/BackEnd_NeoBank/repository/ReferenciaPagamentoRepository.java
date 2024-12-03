package com.example.BackEnd_NeoBank.repository;

import com.example.BackEnd_NeoBank.entity.ReferenciaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReferenciaPagamentoRepository extends JpaRepository<ReferenciaPagamento, Long> {
    // Verifica se já existe uma referência específica
    Optional<ReferenciaPagamento> findByReferencia(String referencia);
    boolean existsByReferencia(String referencia);
    Optional<ReferenciaPagamento> findByEntidadeAndReferenciaAndValor(String entidade, String referencia, float valor);
}
