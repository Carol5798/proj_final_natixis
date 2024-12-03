package com.example.BackEnd_NeoBank.controller;

import com.example.BackEnd_NeoBank.service.ReferenciaPagamentoService;
import com.example.BackEnd_NeoBank.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ReferanciaPagamentos")
@RequiredArgsConstructor
public class ReferenciaPagamentoController {
    private final ReferenciaPagamentoService referenciaPagamentoService;

    @PostMapping("/criar")
    public ResponseEntity<ApiResponse> criarReferenciaPagamento(@RequestParam float valor) {
        try {
            ApiResponse resposta = referenciaPagamentoService.criarReferenciaPagamento(valor);
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Erro inesperado ao criar a referência de pagamento."));
        }
    }
}
