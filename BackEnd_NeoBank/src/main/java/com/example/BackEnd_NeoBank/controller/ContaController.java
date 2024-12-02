package com.example.BackEnd_NeoBank.controller;

import com.example.BackEnd_NeoBank.entity.ContaBancaria;
import com.example.BackEnd_NeoBank.service.ContaService;
import com.example.BackEnd_NeoBank.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/conta")
@RequiredArgsConstructor
public class ContaController {
    private final ContaService _contaService;


    @PostMapping("/criar")
    public ResponseEntity<ApiResponse> criarConta(@RequestParam Long idUtilizador) {
        try {
            _contaService.criarConta(idUtilizador, "", 1);
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



}
