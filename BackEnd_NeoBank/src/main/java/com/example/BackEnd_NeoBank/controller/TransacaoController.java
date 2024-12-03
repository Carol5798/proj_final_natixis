package com.example.BackEnd_NeoBank.controller;

import com.example.BackEnd_NeoBank.service.TransacaoService;
import com.example.BackEnd_NeoBank.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService _transacaoService;

    @PostMapping("/transferir")
    public ResponseEntity<ApiResponse> transferir(
            @RequestParam String ibanDestino,
            @RequestParam Float valor) {
        try {
            ApiResponse response = _transacaoService.transferir(ibanDestino, valor);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Erro inesperado ao processar a transferência."));
        }
    }

    @PostMapping("/pagarReferenciaPagamento")
    public ResponseEntity<ApiResponse> pagarReferenciaPagamento(
            @RequestParam String entidade,
            @RequestParam String referencia,
            @RequestParam float valor) {
        try {
            ApiResponse response = _transacaoService.pagarReferenciaPagamento(entidade, referencia, valor);
            if (response.success) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response); // Sucesso
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // Erro (ex: saldo insuficiente)
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage())); // Erro específico de argumento (ex: dados inválidos)
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Erro inesperado ao processar o pagamento.")); // Erro genérico
        }
    }

}
