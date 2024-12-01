package com.example.BackEnd_NeoBank.controller;

import com.example.BackEnd_NeoBank.entity.User;
import com.example.BackEnd_NeoBank.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Date;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/register")
    public User postUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String emailOrNif, @RequestParam String password) {
        return userService.loginUser(emailOrNif, password)
                .map(user -> {
                    String token = generateJwtToken(user);
                    return ResponseEntity.ok("Login bem-sucedido! Token: " + token);
                })
                .orElse(ResponseEntity.status(401).body("Credenciais inválidas"));
    }

    private String generateJwtToken(User user) {
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
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Utilizador apagado com sucesso");
    }
}
