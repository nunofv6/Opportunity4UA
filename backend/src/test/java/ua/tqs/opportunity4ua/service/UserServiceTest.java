package ua.tqs.opportunity4ua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ua.tqs.opportunity4ua.dto.VolunteerProfileUpdate;
import ua.tqs.opportunity4ua.entity.Role;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.repository.TokenRepository;
import ua.tqs.opportunity4ua.repository.UserRepository;
import ua.tqs.opportunity4ua.utils.Token;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "test@email.com", "password123", Role.VOLUNTEER, null, null, null);
    }

    @Test
    void login_successful_returnsToken() {
        when(userRepository.findByEmail(user.getEmail()))
            .thenReturn(Optional.of(user));

        String token = userService.login(user.getEmail(), "password123");

        assertNotNull(token);
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void login_invalidEmail_throwsException() {
        when(userRepository.findByEmail("wrong@email.com"))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
            userService.login("wrong@email.com", "password")
        );
    }

    @Test
    void login_invalidPassword_throwsException() {
        when(userRepository.findByEmail(user.getEmail()))
            .thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () ->
            userService.login(user.getEmail(), "wrongpass")
        );
    }

    @Test
    void authenticate_validToken_returnsUser() {
        Token token = new Token("token123", user, LocalDateTime.now());

        when(tokenRepository.findById("token123"))
            .thenReturn(Optional.of(token));

        User result = userService.authenticate("token123");

        assertEquals(user, result);
    }

    @Test
    void authenticate_invalidToken_throwsException() {
        when(tokenRepository.findById("invalid"))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
            userService.authenticate("invalid")
        );
    }

    @Test
    void updateProfile_updatesEmailAndRole() {
        User existing = new User(1L, "old@mail.com", "pass", Role.VOLUNTEER, null, null, null);
        VolunteerProfileUpdate updated = new VolunteerProfileUpdate();
        updated.setSkills("Friendly, Punctual");
        updated.setAvailability("Weekends");

        Token token = new Token("token123", existing, LocalDateTime.now());

        when(tokenRepository.findById("token123"))
            .thenReturn(Optional.of(token));
        when(userRepository.save(any(User.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateVolunteerProfile("token123", updated);

        assertEquals("Friendly, Punctual", result.getSkills());
        assertEquals("Weekends", result.getAvailability());
    }
}