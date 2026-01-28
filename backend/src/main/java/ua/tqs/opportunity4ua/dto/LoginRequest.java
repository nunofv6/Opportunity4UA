package ua.tqs.opportunity4ua.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
