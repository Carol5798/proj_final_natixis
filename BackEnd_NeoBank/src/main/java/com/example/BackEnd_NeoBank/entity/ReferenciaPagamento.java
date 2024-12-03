package com.example.BackEnd_NeoBank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "referencia_pagamento")
public class ReferenciaPagamento{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public long id;
    public String entidade;
    public String referencia;
    public float valor;
    public boolean concluido;
    public LocalDateTime dataExpiracao;

}

