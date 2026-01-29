package ua.tqs.opportunity4ua.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.tqs.opportunity4ua.dto.CreateApplication;
import ua.tqs.opportunity4ua.entity.Application;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.ApplicationStatus;
import ua.tqs.opportunity4ua.enums.Role;
import ua.tqs.opportunity4ua.service.ApplicationService;
import ua.tqs.opportunity4ua.service.UserService;

@WebMvcTest(ApplicationController.class)
class ApplicationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplicationService applicationService;

    @MockitoBean
    private UserService userService;

    @Test
    void getAllApplications_returns200() throws Exception {
        when(userService.authenticate("token")).thenReturn(new User());
        when(applicationService.getAllApplications())
                .thenReturn(List.of(new Application()));

        mockMvc.perform(get("/api/application")
                .header("X-Auth-Token", "token"))
            .andExpect(status().isOk());
    }

    @Test
    void applyToOpportunity_asVolunteer_returns200() throws Exception {
        User volunteer = new User();
        volunteer.setRole(Role.VOLUNTEER);

        Application app = new Application();
        app.setId(1L);
        app.setStatus(ApplicationStatus.PENDING);

        when(userService.authenticate("token")).thenReturn(volunteer);
        when(applicationService.applyToOpportunity(1L, volunteer))
                .thenReturn(app);

        mockMvc.perform(post("/api/application/1/apply")
                .header("X-Auth-Token", "token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void acceptApplication_asPromoter_returns200() throws Exception {
        User promoter = new User();
        promoter.setRole(Role.PROMOTER);

        Application app = new Application();
        app.setStatus(ApplicationStatus.ACCEPTED);

        when(userService.authenticate("token")).thenReturn(promoter);
        when(applicationService.acceptApplication(1L, promoter))
                .thenReturn(app);

        mockMvc.perform(put("/api/application/1/accept")
                .header("X-Auth-Token", "token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    void getMyApplications_returnsList() throws Exception {
        User volunteer = new User();
        volunteer.setRole(Role.VOLUNTEER);

        CreateApplication dto = new CreateApplication(
                1L, "PENDING", null, 10L, "Opportunity");

        when(userService.authenticate("token")).thenReturn(volunteer);
        when(applicationService.getApplicationsByVolunteer(volunteer))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/application/me")
                .header("X-Auth-Token", "token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}
