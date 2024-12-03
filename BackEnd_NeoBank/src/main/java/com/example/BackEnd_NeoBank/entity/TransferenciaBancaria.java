package com.example.BackEnd_NeoBank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "transferencia_bancaria")
public class TransferenciaBancaria extends Transacao {

    public String IbanContaDestino;

}
