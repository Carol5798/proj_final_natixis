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


    public void updateUser(Long id, Utilizador updatedUser) {
        Utilizador existingUser = utilizadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        existingUser.setNome(updatedUser.getNome());
        existingUser.setDataNascimento(updatedUser.getDataNascimento());
        existingUser.setNif(updatedUser.getNif());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setNumeroTelemovel(updatedUser.getNumeroTelemovel());

        utilizadorRepository.save(existingUser);
    }

    public Utilizador getUser(Long id) {
        return utilizadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
    }

    public void deleteUser(Long id) {
        if (!utilizadorRepository.existsById(id)) {
            throw new IllegalArgumentException("Utilizador não encontrado");
        }
        utilizadorRepository.deleteById(id);
    }
}
