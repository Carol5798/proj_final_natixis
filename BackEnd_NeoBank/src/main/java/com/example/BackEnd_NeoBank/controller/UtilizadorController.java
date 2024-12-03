package com.example.BackEnd_NeoBank.controller;

import com.example.BackEnd_NeoBank.entity.Utilizador;
import com.example.BackEnd_NeoBank.repository.UtilizadorRepository;
import com.example.BackEnd_NeoBank.service.UtilizadorService;
import com.example.BackEnd_NeoBank.utils.ApiResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/utilizador")
@RequiredArgsConstructor
public class UtilizadorController {
    private final UtilizadorService utilizadorService;
    private final UtilizadorRepository utilizadorRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> postUser(@RequestBody Utilizador user) {
        try {
            utilizadorService.registerUser(user);
            ApiResponse response = new ApiResponse(true, "Utilizador registado com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse response;
            if (e instanceof IllegalArgumentException) {
                response = new ApiResponse(false, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } else {
                response = new ApiResponse(false, "Ocorreu um erro a criar a conta.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody Map<String, String> loginData) {
        try {
            String emailOrNif = loginData.get("emailOrNif");
            String password = loginData.get("password");

            return utilizadorService.loginUser(emailOrNif, password)
                    .map(user -> {
                        String token = generateJwtToken(user);
                        Map<String, Object> data = new HashMap<>();
                        data.put("token", token);

                        ApiResponse response = new ApiResponse(true, "Login bem-sucedido!", data);
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        ApiResponse response = new ApiResponse(false, "Credenciais inválidas");
                        return ResponseEntity.status(401).body(response);
                    });
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Ocorreu um erro ao processar o login.");
            return ResponseEntity.status(500).body(response);
        }
    }

    private String generateJwtToken(Utilizador user) {
        String secretKey = "WuS9qRVgYo3DUCsO0Xw+vNAnaFbc2kjdmnf6mpYiBJuUM0o+btYjkIh0gdb+xk4u-n";
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        long expirationTime = 3600000;
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody Utilizador updatedUser) {
        String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            utilizadorService.updateUser(authenticatedEmail, updatedUser);
            ApiResponse response = new ApiResponse(true, "Utilizador atualizado com sucesso.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Ocorreu um erro ao atualizar o utilizador.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getUser() {
        try {
            String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Utilizador user = utilizadorService.getUserByEmail(authenticatedEmail);

            Map<String, Object> userResponse = new HashMap<>();
            userResponse.put("nome", user.getNome());
            userResponse.put("dataNascimento", user.getDataNascimento());
            userResponse.put("nif", user.getNif());
            userResponse.put("username", user.getUsername());
            userResponse.put("email", user.getEmail());
            userResponse.put("numeroTelemovel", user.getNumeroTelemovel());

            ApiResponse response = new ApiResponse(true, "Utilizador encontrado", userResponse);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Ocorreu um erro ao procurar o utilizador");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse> deleteUser() {
        String authenticatedEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            utilizadorService.deleteUser(authenticatedEmail);
            ApiResponse response = new ApiResponse(true, "Utilizador excluído com sucesso.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Ocorreu um erro ao excluir o utilizador.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
