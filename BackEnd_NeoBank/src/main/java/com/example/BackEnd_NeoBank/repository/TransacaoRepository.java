package com.example.BackEnd_NeoBank.repository;

import com.example.BackEnd_NeoBank.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {


}