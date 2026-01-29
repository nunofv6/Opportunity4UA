package ua.tqs.opportunity4ua.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.dto.LoginRequest;
import ua.tqs.opportunity4ua.dto.PromoterProfileUpdate;
import ua.tqs.opportunity4ua.dto.VolunteerProfileUpdate;
import ua.tqs.opportunity4ua.service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> register(@RequestBody User user) {
        User saved = userService.register(user);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(
            @RequestHeader("X-Auth-Token") String token) {

        User user = userService.authenticate(token);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me/profile/volunteer")
    public ResponseEntity<User> updateVolunteerProfile(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody VolunteerProfileUpdate updatedData) {

        User saved = userService.updateVolunteerProfile(token, updatedData);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/me/profile/promoter")
    public ResponseEntity<User> updatePromoterProfile(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody PromoterProfileUpdate updatedData) {

        User saved = userService.updatePromoterProfile(token, updatedData);
        return ResponseEntity.ok(saved);
    }
}
