package com.example.BackEnd_NeoBank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "utilizador")
public class Utilizador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String nome;

    public LocalDate dataNascimento;

    public String nif;

    public String username;

    public String password;

    public String email;

    public String numeroTelemovel;
}
