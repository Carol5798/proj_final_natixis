package com.example.BackEnd_NeoBank.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "transacao")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String IbanContaOrigem;

    public float valor;

    public LocalDateTime data;

    public int tipo;// 1-TRANSFERENCIA_BANCARIA, 2 -PAGAMENTO

    public boolean estado = true;

}
