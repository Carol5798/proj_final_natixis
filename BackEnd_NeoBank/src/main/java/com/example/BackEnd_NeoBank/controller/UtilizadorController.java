package com.example.BackEnd_NeoBank.controller;

import com.example.BackEnd_NeoBank.entity.Utilizador;
import com.example.BackEnd_NeoBank.service.UtilizadorService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Date;

@RestController
@RequestMapping("/utilizador")
@RequiredArgsConstructor
public class UtilizadorController {
    private final UtilizadorService utilizadorService;


    @PostMapping("/register")
    public Utilizador postUser(@RequestBody Utilizador user) {
        return utilizadorService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String emailOrNif, @RequestParam String password) {
        return utilizadorService.loginUser(emailOrNif, password)
                .map(user -> {
                    String token = generateJwtToken(user);
                    return ResponseEntity.ok("Login bem-sucedido! Token: " + token);
                })
                .orElse(ResponseEntity.status(401).body("Credenciais inválidas"));
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


    @PutMapping("/{id}")
    public ResponseEntity<Utilizador> updateUser(@PathVariable Long id, @RequestBody Utilizador user) {
        return ResponseEntity.ok(utilizadorService.updateUser(id, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilizador> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(utilizadorService.getUser(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        utilizadorService.deleteUser(id);
        return ResponseEntity.ok("Utilizador apagado com sucesso");
    }
}
