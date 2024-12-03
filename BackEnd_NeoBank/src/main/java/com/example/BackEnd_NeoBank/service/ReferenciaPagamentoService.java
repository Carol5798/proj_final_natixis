package com.example.BackEnd_NeoBank.service;

import com.example.BackEnd_NeoBank.entity.ContaBancaria;
import com.example.BackEnd_NeoBank.entity.ReferenciaPagamento;
import com.example.BackEnd_NeoBank.entity.Utilizador;
import com.example.BackEnd_NeoBank.repository.ContaBancariaRepository;
import com.example.BackEnd_NeoBank.repository.ReferenciaPagamentoRepository;
import com.example.BackEnd_NeoBank.repository.UtilizadorRepository;
import com.example.BackEnd_NeoBank.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReferenciaPagamentoService {
    private final ReferenciaPagamentoRepository _referenciaPagamentoRepository;
    private final UtilizadorRepository _utilizadorRepository;
    private final ContaBancariaRepository _contaBancariaRepository;

    public ApiResponse criarReferenciaPagamento(float valor) {

            if (valor <= 0) {
                return new ApiResponse(false, "O valor deve ser positivo.");
            }

            String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Verificar se o utilizador existe
            Utilizador utilizador = _utilizadorRepository.findByEmail(authenticatedEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));

            // Verificar a conta bancaria
            ContaBancaria conta = _contaBancariaRepository.findByUtilizador_Id(utilizador.id)
                    .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));

            // Verificar se é conta de empresa
            if(conta.tipoConta != 2){
                throw new IllegalStateException("Essa conta não pode criar referencias de pagamento");
            }

            String referenciaUnica = gerarReferenciaUnica();

            ReferenciaPagamento referenciaPagamento = new ReferenciaPagamento();
            referenciaPagamento.valor = valor;
            referenciaPagamento.entidade = conta.entidade;
            referenciaPagamento.referencia = referenciaUnica;
            referenciaPagamento.concluido = false;
            referenciaPagamento.dataExpiracao = LocalDateTime.now().plusDays(1);

            _referenciaPagamentoRepository.save(referenciaPagamento);

            // Criar um Map para o campo data
            Map<String, Object> responseData = Map.of("ReferenciaPagamento", referenciaPagamento);

            return new ApiResponse(true, "Referência de pagamento criada com sucesso.", responseData);
    }

    private String gerarReferenciaUnica() {
        String referencia;
        do {
            referencia = String.format("%09d", (int) (Math.random() * 1_000_000_000));
        } while (_referenciaPagamentoRepository.existsByReferencia(referencia));
        return referencia;
    }
}
