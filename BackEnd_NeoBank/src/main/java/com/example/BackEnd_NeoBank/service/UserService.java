package com.example.BackEnd_NeoBank.service;

import com.example.BackEnd_NeoBank.entity.User;
import com.example.BackEnd_NeoBank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail()) || userRepository.existsByNif(user.getNif())) {
            throw new IllegalArgumentException("Conta já criada!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> loginUser(String emailOrNif, String rawPassword) {
        return userRepository.findByEmailOrNif(emailOrNif, emailOrNif)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()));
    }


    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        existingUser.setNome(updatedUser.getNome());
        existingUser.setDataNascimento(updatedUser.getDataNascimento());
        existingUser.setNif(updatedUser.getNif());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setNumeroTelemovel(updatedUser.getNumeroTelemovel());

        return userRepository.save(existingUser);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Utilizador não encontrado");
        }
        userRepository.deleteById(id);
    }
}
