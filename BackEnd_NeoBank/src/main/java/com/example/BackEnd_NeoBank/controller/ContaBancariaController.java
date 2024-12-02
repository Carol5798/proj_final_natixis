package com.example.BackEnd_NeoBank.controller;

import com.example.BackEnd_NeoBank.service.ContaBancariaService;
import com.example.BackEnd_NeoBank.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/conta")
@RequiredArgsConstructor
public class ContaBancariaController {
    private final ContaBancariaService _contaBancariaService;


    @PostMapping("/criar")
    public ResponseEntity<ApiResponse> criarConta() {
        try {

            _contaBancariaService.criarConta();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Conta criada com sucesso."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Utilizador não encontrado."));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, "O utilizador já tem uma conta associada."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Erro inesperado ao criar a conta."));
        }
    }

    @GetMapping("/obterInformacaoConta")
    public ResponseEntity<ApiResponse> obterInformacaoConta() {
        try {
            String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            ApiResponse response = _contaBancariaService.obterContaPorEmail(authenticatedEmail);
            return ResponseEntity.ok(response); // Retorna 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Erro ao obter a conta."));
        }
    }


}
