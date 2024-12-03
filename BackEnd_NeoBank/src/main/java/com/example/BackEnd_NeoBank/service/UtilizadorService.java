package com.example.BackEnd_NeoBank.service;

import com.example.BackEnd_NeoBank.entity.Utilizador;
import com.example.BackEnd_NeoBank.repository.UtilizadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilizadorService {
    private final UtilizadorRepository utilizadorRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(Utilizador user) {
        if (utilizadorRepository.existsByEmail(user.getEmail()) || utilizadorRepository.existsByNif(user.getNif())) {
            throw new IllegalArgumentException("Conta já existente!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        utilizadorRepository.save(user);
    }

    public Optional<Utilizador> loginUser(String emailOrNif, String rawPassword) {
        return utilizadorRepository.findByEmailOrNif(emailOrNif, emailOrNif)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()));
    }


    public void updateUser(String email, Utilizador updatedUser) {
        Utilizador existingUser = utilizadorRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));

        existingUser.setNome(updatedUser.getNome());
        existingUser.setDataNascimento(updatedUser.getDataNascimento());
        existingUser.setNif(updatedUser.getNif());
        existingUser.setNumeroTelemovel(updatedUser.getNumeroTelemovel());
        existingUser.setUsername(updatedUser.getUsername());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        utilizadorRepository.save(existingUser);
    }

    public Utilizador getUserByEmail(String email) {
        return utilizadorRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado."));
    }

    public void deleteUser(String email) {
        Utilizador existingUser = utilizadorRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado."));
        utilizadorRepository.delete(existingUser);
    }

}
