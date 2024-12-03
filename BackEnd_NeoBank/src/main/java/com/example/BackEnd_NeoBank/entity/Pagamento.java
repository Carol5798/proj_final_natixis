package com.example.BackEnd_NeoBank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamento")
public class Pagamento extends Transacao {


    public String entidade;

    public String referencia;
    public String metodoPagamento;

    // Getters e Setters
}

