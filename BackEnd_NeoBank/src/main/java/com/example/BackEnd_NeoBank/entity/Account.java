package com.example.BackEnd_NeoBank.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "conta_bancaria")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idUtilizador", nullable = false)
    private User user;

    @Column(name = "iban", nullable = false, unique = true, length = 34)
    private String iban;

    @Column(name = "saldo", nullable = false)
    private Float saldo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoConta tipo;

    @Column(name = "entidade")
    private String entidade;

    public enum TipoConta {
        CORRENTE,
        EMPRESA
    }
}
