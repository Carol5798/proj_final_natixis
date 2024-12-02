package com.example.BackEnd_NeoBank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "conta_bancaria")
public class ContaBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne
    @JoinColumn(name = "id_utilizador", nullable = false)
    @JsonIgnore
    public Utilizador utilizador;

    public String iban;

    public Float saldo;

    public int tipoConta; //1-CORRENTE, 2- EMPRESA

    public String entidade;

}
