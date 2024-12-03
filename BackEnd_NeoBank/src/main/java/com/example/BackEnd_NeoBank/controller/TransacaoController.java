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

    @PostMapping("/pagamento")
    public ResponseEntity<ApiResponse> realizarPagamento(
            @RequestParam String entidade,
            @RequestParam String referencia,
            @RequestParam float valor) {
        try {
            // Chama o serviço para realizar o pagamento
            ApiResponse response = _transacaoService.realizarPagamento(entidade, referencia, valor);

            // Retorna a resposta com o status adequado
            if (response.success) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (IllegalArgumentException e) {
            // Erros de validação ou outros problemas
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            // Erro genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Erro inesperado ao realizar o pagamento."));
        }
    }

}
