package com.example.BackEnd_NeoBank.service;


import com.example.BackEnd_NeoBank.entity.*;
import com.example.BackEnd_NeoBank.repository.ContaBancariaRepository;
import com.example.BackEnd_NeoBank.repository.ReferenciaPagamentoRepository;
import com.example.BackEnd_NeoBank.repository.TransacaoRepository;
import com.example.BackEnd_NeoBank.repository.UtilizadorRepository;
import com.example.BackEnd_NeoBank.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final ContaBancariaRepository _contaBancariaRepository;
    private final UtilizadorRepository _utilizadorRepository;
    private final TransacaoRepository _transacaoRepository;
    private final ReferenciaPagamentoRepository _referenciaPagamentoRepository;

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
        ContaBancaria origem = _contaBancariaRepository.findByUtilizador_Id(utilizador.id)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));
        ContaBancaria destino = _contaBancariaRepository.findByIban(ibanDestino)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada."));

        // Verificar se a conta destino é diferente da conta origem
        if(Objects.equals(ibanDestino, origem.iban)){
            throw new IllegalStateException("Não é possível enviar dinheiro para a mesma conta.");
        }

        // Verificar saldo
        if (origem.getSaldo() < valor) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }

        // Atualizar saldos
        origem.setSaldo(origem.getSaldo() - valor);
        destino.setSaldo(destino.getSaldo() + valor);

        // Criar e guardar a transferência bancária
        Transacao transferencia = new Transacao();
        transferencia.setIbanContaOrigem(origem.iban);
        transferencia.setIbanContaDestino(ibanDestino);
        transferencia.setValor(valor);
        transferencia.setData(LocalDateTime.now());
        transferencia.setTipo(1); // 1 para Transferência Bancária
        transferencia.setIbanContaDestino(destino.iban);
        _transacaoRepository.save(transferencia);

        // Atualizar as contas na base de dados
        _contaBancariaRepository.save(origem);
        _contaBancariaRepository.save(destino);

        return new ApiResponse(true, "Transação realizada com sucesso.");
    }

    @Transactional
    public ApiResponse pagarReferenciaPagamento(String entidade, String referencia, float valor) {
        try {
            System.out.println("Início do pagamento: " + referencia); // Log de início

            // Obter o email do utilizador autenticado
            String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println("Email do utilizador autenticado: " + authenticatedEmail);

            // Verificar se o utilizador existe
            Utilizador utilizador = _utilizadorRepository.findByEmail(authenticatedEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
            System.out.println("Utilizador encontrado: " + utilizador.getEmail());

            // Verificar se a conta existe
            ContaBancaria contaOrigem = _contaBancariaRepository.findByUtilizador_Id(utilizador.id)
                    .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));
            System.out.println("Conta origem encontrada: " + contaOrigem.getIban());

            // Verificar se a referência existe
            ReferenciaPagamento referenciaPagamento = _referenciaPagamentoRepository.findByEntidadeAndReferenciaAndValor(entidade, referencia, valor)
                    .orElseThrow(() -> new IllegalArgumentException("Referência de pagamento não encontrada."));
            System.out.println("Referência encontrada: " + referenciaPagamento.referencia);

            // Verificar se a referência já foi paga
            if (referenciaPagamento.concluido) {
                return new ApiResponse(false, "Esta referência de pagamento já foi paga.");
            }

            // Verificar saldo suficiente
            if (contaOrigem.getSaldo() < valor) {
                return new ApiResponse(false, "Saldo insuficiente.");
            }

            // Verificar se está a tentar pagar a sua própria referência
            if (contaOrigem.entidade == entidade) {
                return new ApiResponse(false, "Não é possível pagar uma referência da sua própria entidade.");
            }

            // Encontrar a conta destino
            ContaBancaria contaDestino = _contaBancariaRepository.findByEntidade(entidade)
                    .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada."));
            System.out.println("Conta destino encontrada: " + contaDestino.getIban());

            // Processar o pagamento
            contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
            contaDestino.setSaldo(contaDestino.getSaldo() + valor);
            _contaBancariaRepository.save(contaOrigem);
            _contaBancariaRepository.save(contaDestino);

            // Marcar a referência como concluída
            referenciaPagamento.concluido = true;
            _referenciaPagamentoRepository.save(referenciaPagamento);

            // Criar a transação de pagamento
            Transacao transacao = new Transacao();
            transacao.setIbanContaOrigem(contaOrigem.getIban());
            transacao.setValor(valor);
            transacao.setData(LocalDateTime.now());
            transacao.setTipo(2); // Tipo 2 para pagamento de referência
            transacao.setIbanContaDestino(contaDestino.getIban());
            _transacaoRepository.save(transacao);

            return new ApiResponse(true, "Pagamento realizado com sucesso.");

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
            return new ApiResponse(false, e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace(); // Imprimir stack trace para melhor debug
            return new ApiResponse(false, "Erro inesperado ao processar o pagamento.");
        }
    }

//    @Transactional
//    public ApiResponse realizarPagamento(String entidade, String referencia, float valor) {
//        // Verificar se o valor é positivo
//        if (valor <= 0) {
//            return new ApiResponse(false, "O valor da transação deve ser positivo.");
//        }
//
//        // Obter o utilizador logado
//        String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Utilizador utilizador = _utilizadorRepository.findByEmail(authenticatedEmail)
//                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado."));
//
//        // Verificar se o utilizador tem uma conta associada
//        ContaBancaria contaOrigem = _contaBancariaRepository.findByUtilizador_Id(utilizador.id)
//                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));
//
//        // Verificar se a conta origem tem saldo suficiente
//        if (contaOrigem.getSaldo() < valor) {
//            return new ApiResponse(false, "Saldo insuficiente na conta origem.");
//        }
//
//        // Buscar a conta destino com a entidade e referência
//        ContaBancaria contaDestino = _contaBancariaRepository.findByEntidade(entidade)
//                .orElseThrow(() -> new IllegalArgumentException("Conta destino não encontrada."));
//
//        // Atualizar as contas
//        contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
//        contaDestino.setSaldo(contaDestino.getSaldo() + valor);
//
//        // Criar e guardar o Pagamento
//        Pagamento pagamento = new Pagamento();
//        pagamento.setIbanContaOrigem(contaOrigem.getIban());
//        pagamento.setValor(valor);
//        pagamento.setData(LocalDateTime.now());
//        pagamento.setTipo(2); // 2 - Pagamento
//        pagamento.setEstado(true);
//        pagamento.entidade = entidade;
//        pagamento.referencia = referencia;
//        pagamento.metodoPagamento = "MbWay";
//
//        // Salvar transação no repositório Transacao
//        _transacaoRepository.save(pagamento);
//
//        // Atualizar as contas na base de dados
//        _contaBancariaRepository.save(contaOrigem);
//        _contaBancariaRepository.save(contaDestino);
//
//        // Retornar resposta com sucesso
//        return new ApiResponse(true, "Pagamento realizado com sucesso.");
//    }

}
