package com.example.BackEnd_NeoBank.service;

import com.example.BackEnd_NeoBank.entity.ContaBancaria;
import com.example.BackEnd_NeoBank.entity.Utilizador;
import com.example.BackEnd_NeoBank.repository.ContaBancariaRepository;
import com.example.BackEnd_NeoBank.repository.UtilizadorRepository;
import com.example.BackEnd_NeoBank.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContaBancariaService {
    private final ContaBancariaRepository _contaBancariaRepository;
    private final UtilizadorRepository _utilizadorRepository;


    public ContaBancaria criarConta() {

        String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Verificar se o utilizador existe
        Utilizador utilizador = _utilizadorRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));

        // Verificar se o utilizador já tem uma conta associada
        if (_contaBancariaRepository.existsByUtilizador_Id(utilizador.id)) {
            throw new IllegalStateException("O utilizador já tem uma conta associada");
        }

        ContaBancaria novaConta = new ContaBancaria();
        if(utilizador.tipoUtilizador == 2){
            // Gerar a entidade
            String entidade;
            do{
                entidade = gerarEntidade();
            }while(_contaBancariaRepository.existsByEntidade(entidade));

            novaConta.entidade = entidade;
        }

        // Gerar um IBAN único
        String iban;
        do {
            iban = gerarIban();
        } while (_contaBancariaRepository.existsByIban(iban));

        novaConta.utilizador = utilizador;
        novaConta.iban = iban;
        novaConta.saldo = 0.0f;
        novaConta.tipoConta = utilizador.tipoUtilizador;

        return _contaBancariaRepository.save(novaConta);
    }

    public ApiResponse obterContaPorEmail(String email) {

        // Verificar se o utilizador existe
        Utilizador utilizador = _utilizadorRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado."));

        ContaBancaria conta = _contaBancariaRepository.findByUtilizador_Id(utilizador.id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada para o NIF fornecido."));

        // Criar um Map para o campo data
        Map<String, Object> responseData = Map.of("conta", conta);

        return new ApiResponse(true, "Conta encontrada com sucesso.", responseData);
    }


    private String gerarEntidade() {
        return String.format("%05d", (int) (Math.random() * 100_000));
    }

    // Simulação da geração de um IBAN
    private String gerarIban() {
        // Gera 21 números aleatórios, formatados para garantir que sejam sempre 21 dígitos
        String numerosAleatorios = String.format("%021d", (long) (Math.random() * Math.pow(10, 21)));
        return "PT50" + numerosAleatorios;
    }
}
