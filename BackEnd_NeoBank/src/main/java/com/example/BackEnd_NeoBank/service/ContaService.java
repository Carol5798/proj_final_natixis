package com.example.BackEnd_NeoBank.service;

import com.example.BackEnd_NeoBank.entity.ContaBancaria;
import com.example.BackEnd_NeoBank.entity.Utilizador;
import com.example.BackEnd_NeoBank.repository.ContaRepository;
import com.example.BackEnd_NeoBank.repository.UtilizadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContaService {
    private final ContaRepository _contaRepository;
    private final UtilizadorRepository _utilizadorRepository;


    public ContaBancaria criarConta(Long idUtilizador, String entidade, int tipoConta) {
        // Verificar se o utilizador existe
        Utilizador utilizador = _utilizadorRepository.findById(idUtilizador)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));

        // Verificar se o utilizador já tem uma conta associada
        if (_contaRepository.existsByUtilizador_Id(idUtilizador)) {
            throw new IllegalStateException("O utilizador já tem uma conta associada");
        }

        // Gerar um IBAN único
        String iban;
        do {
            iban = gerarIban(entidade); // Gera o IBAN baseado na entidade
        } while (_contaRepository.existsByIban(iban));

        // Criar a conta bancária
        ContaBancaria novaConta = new ContaBancaria();
        novaConta.utilizador = utilizador;
        novaConta.iban = iban;
        novaConta.saldo = 0.0f;
        novaConta.tipoConta = tipoConta;
        novaConta.entidade = entidade;

        return _contaRepository.save(novaConta);
    }

    // Simulação da geração de um IBAN
    private String gerarIban(String entidade) {
        return "PT50" + entidade + (int) (Math.random() * 1_000_000_000);
    }
}
