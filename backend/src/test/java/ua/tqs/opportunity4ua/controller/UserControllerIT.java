package ua.tqs.opportunity4ua.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.tqs.opportunity4ua.dto.VolunteerProfileUpdate;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.Role;
import ua.tqs.opportunity4ua.service.UserService;


@WebMvcTest(UserController.class)
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void getAllUsers_returnsUsers() throws Exception {
        User user = new User(1L, "test@mail.com", "pass", Role.VOLUNTEER, null, null, 0, null, null);
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].email").value("test@mail.com"));
    }

    @Test
    void register_success() throws Exception {
        User user = new User(null, "new@mail.com", "pass", Role.VOLUNTEER, null, null, 0, null, null);
        when(userService.register(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "email": "new@mail.com",
                    "password": "pass",
                    "role": "VOLUNTEER"
                }
                """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("new@mail.com"));
    }

    @Test
    void register_duplicateEmail_returnsBadRequest() throws Exception {
        when(userService.register(any(User.class))).thenReturn(null);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "email": "dup@mail.com",
                    "password": "pass"
                }
                """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void login_returnsToken() throws Exception {
        when(userService.login("test@mail.com", "pass"))
            .thenReturn("token123");

        String body = """
            {
              "email": "test@mail.com",
              "password": "pass"
            }
            """;

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(content().string("token123"));
    }

    @Test
    void getCurrentUser_returnsUser() throws Exception {
        User user = new User(1L, "me@mail.com", "pass", Role.ADMIN, null, null, 0, null, null);
        when(userService.authenticate("token123")).thenReturn(user);

        mockMvc.perform(get("/api/users/me")
                .header("X-Auth-Token", "token123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("me@mail.com"));
    }

    @Test
    void updateProfile_returnsUpdatedVolunteer() throws Exception {
        User updated = new User(1L, "updated@mail.com", "pass", Role.PROMOTER, null, null, 0, null, null);
        when(userService.updateVolunteerProfile(anyString(), any(VolunteerProfileUpdate.class)))
            .thenReturn(updated);

        mockMvc.perform(put("/api/users/me/profile/volunteer")
                .header("X-Auth-Token", "token123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "skills": "Friendly, helpful, organized",
                    "availability": "Evenings and weekends"
                }
                """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("updated@mail.com"))
            .andExpect(jsonPath("$.role").value("PROMOTER"));
    }
}

