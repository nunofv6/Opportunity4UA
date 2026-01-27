package ua.tqs.opportunity4ua.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.Role;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.service.OpportunityService;
import ua.tqs.opportunity4ua.service.UserService;

@WebMvcTest(OpportunityController.class)
public class OpportunityControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpportunityService opportunityService;

    @MockitoBean
    private UserService userService;

    @Test
    void getAllOpportunities_returns200() throws Exception {
        when(opportunityService.getAllOpportunities())
                .thenReturn(List.of(new Opportunity()));

        mockMvc.perform(get("/api/opportunity"))
                .andExpect(status().isOk());
    }

    @Test
    void promoterCanCloseOpportunity() throws Exception {
        User promoter = new User();
        promoter.setId(1L);
        promoter.setRole(Role.PROMOTER);

        Opportunity op = new Opportunity();
        op.setId(1L);

        when(userService.authenticate("token")).thenReturn(promoter);
        when(opportunityService.closeOpportunity(1L, promoter)).thenReturn(op);

        mockMvc.perform(put("/api/opportunity/1/close")
                .header("X-Auth-Token", "token"))
                .andExpect(status().isOk());
    }

    @Test
    void volunteerCannotCloseOpportunity() throws Exception {
        User volunteer = new User();
        volunteer.setRole(Role.VOLUNTEER);

        when(userService.authenticate("token")).thenReturn(volunteer);

        mockMvc.perform(put("/api/opportunity/1/close")
                .header("X-Auth-Token", "token"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getOpportunityById_notFound() throws Exception {
        when(userService.authenticate("token")).thenReturn(new User());
        when(opportunityService.getOpportunityById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/opportunity/99")
                .header("X-Auth-Token", "token"))
                .andExpect(status().isNotFound());
    }
}
