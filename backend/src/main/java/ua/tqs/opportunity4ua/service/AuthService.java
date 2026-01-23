package ua.tqs.opportunity4ua.service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.utils.Token;
import ua.tqs.opportunity4ua.repository.TokenRepository;
import ua.tqs.opportunity4ua.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = UUID.randomUUID().toString();
        tokenRepository.save(new Token(token, user, LocalDateTime.now()));
        return token;
    }

    public User authenticate(String token) {
        return tokenRepository.findById(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"))
            .getUser();
    }
}