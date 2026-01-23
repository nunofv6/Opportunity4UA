package ua.tqs.opportunity4ua.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.entity.Role;
import ua.tqs.opportunity4ua.repository.UserRepository;
import ua.tqs.opportunity4ua.service.AuthService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<User> register(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        if (user.getRole() == null) {
            user.setRole(Role.VOLUNTEER);
        }

        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(
            @RequestHeader("X-Auth-Token") String token) {

        User user = authService.authenticate(token);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me/profile")
    public ResponseEntity<User> updateProfile(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody User updatedData) {

        User user = authService.authenticate(token);

        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }
}
