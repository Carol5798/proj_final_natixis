package com.example.BackEnd_NeoBank.service;


import com.example.BackEnd_NeoBank.entity.*;
import com.example.BackEnd_NeoBank.repository.ContaRepository;
import com.example.BackEnd_NeoBank.repository.TransacaoRepository;
import com.example.BackEnd_NeoBank.repository.UtilizadorRepository;
import com.example.BackEnd_NeoBank.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final ContaRepository _contaRepository;
    private final UtilizadorRepository _utilizadorRepository;
    private final TransacaoRepository _transacaoRepository;

    @Transactional
    public ApiResponse transferir(String ibanDestino, Float valor) {

        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }
        // Obter o email do utilizador autenticado
        String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Verificar se o utilizador existe
        Utilizador utilizador = _utilizadorRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));

        // Verificar se as contas existem
        ContaBancaria origem = _contaRepository.findByUtilizador_Id(utilizador.id)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));
        ContaBancaria destino = _contaRepository.findByIban(ibanDestino)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada."));

        // Verificar saldo
        if (origem.getSaldo() < valor) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }

        // Atualizar saldos
        origem.setSaldo(origem.getSaldo() - valor);
        destino.setSaldo(destino.getSaldo() + valor);

        // Criar e guardar a transferência bancária
        TransferenciaBancaria transferencia = new TransferenciaBancaria();
        transferencia.setIbanContaOrigem(origem.iban);
        transferencia.setIbanContaDestino(ibanDestino);
        transferencia.setValor(valor);
        transferencia.setData(LocalDateTime.now());
        transferencia.setTipo(1); // 1 para Transferência Bancária
        transferencia.setEstado(true);

        _transacaoRepository.save(transferencia);

        // Atualizar as contas na base de dados
        _contaRepository.save(origem);
        _contaRepository.save(destino);

        return new ApiResponse(true, "Transação realizada com sucesso.");
    }

    @Transactional
    public ApiResponse realizarPagamento(String entidade, String referencia, float valor) {
        // Verificar se o valor é positivo
        if (valor <= 0) {
            return new ApiResponse(false, "O valor da transação deve ser positivo.");
        }

        // Obter o utilizador logado
        String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utilizador utilizador = _utilizadorRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado."));

        // Verificar se o utilizador tem uma conta associada
        ContaBancaria contaOrigem = _contaRepository.findByUtilizador_Id(utilizador.id)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));

        // Verificar se a conta origem tem saldo suficiente
        if (contaOrigem.getSaldo() < valor) {
            return new ApiResponse(false, "Saldo insuficiente na conta origem.");
        }

        // Buscar a conta destino com a entidade e referência
        ContaBancaria contaDestino = _contaRepository.findByEntidade(entidade)
                .orElseThrow(() -> new IllegalArgumentException("Conta destino não encontrada."));

        // Atualizar as contas
        contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
        contaDestino.setSaldo(contaDestino.getSaldo() + valor);

        // Criar e guardar o Pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setIbanContaOrigem(contaOrigem.getIban());
        pagamento.setValor(valor);
        pagamento.setData(LocalDateTime.now());
        pagamento.setTipo(2); // 2 - Pagamento
        pagamento.setEstado(true);
        pagamento.entidade = entidade;
        pagamento.referencia = referencia;
        pagamento.metodoPagamento = "MbWay";

        // Salvar transação no repositório Transacao
        _transacaoRepository.save(pagamento);

        // Atualizar as contas na base de dados
        _contaRepository.save(contaOrigem);
        _contaRepository.save(contaDestino);

        // Retornar resposta com sucesso
        return new ApiResponse(true, "Pagamento realizado com sucesso.");
    }

}
