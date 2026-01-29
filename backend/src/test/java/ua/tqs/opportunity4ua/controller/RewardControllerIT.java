package ua.tqs.opportunity4ua.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.tqs.opportunity4ua.entity.Item;
import ua.tqs.opportunity4ua.entity.Reward;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.Role;
import ua.tqs.opportunity4ua.service.ItemService;
import ua.tqs.opportunity4ua.service.RewardService;
import ua.tqs.opportunity4ua.service.UserService;

@WebMvcTest(RewardController.class)
class RewardControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RewardService rewardService;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private UserService userService;

    @Test
    void listActiveRewards_returns200() throws Exception {
        when(itemService.listActiveItems())
                .thenReturn(List.of(new Item()));

        mockMvc.perform(get("/api/rewards"))
            .andExpect(status().isOk());
    }

    @Test
    void myRewards_returnsRewards() throws Exception {
        User volunteer = new User();
        volunteer.setRole(Role.VOLUNTEER);

        when(userService.authenticate("token")).thenReturn(volunteer);
        when(rewardService.getRewardsForVolunteer(volunteer))
                .thenReturn(List.of(new Reward()));

        mockMvc.perform(get("/api/rewards/me")
                .header("X-Auth-Token", "token"))
            .andExpect(status().isOk());
    }

    @Test
    void myRewardItems_returnsItems() throws Exception {
        User volunteer = new User();
        volunteer.setRole(Role.VOLUNTEER);
        volunteer.setRedeemedItems(new ArrayList<>());

        volunteer.getRedeemedItems().add(new Item());

        when(userService.authenticate("token")).thenReturn(volunteer);

        mockMvc.perform(get("/api/rewards/items/me")
                .header("X-Auth-Token", "token"))
            .andExpect(status().isOk());
    }

    @Test
    void redeem_reward_returnsItem() throws Exception {
        User volunteer = new User();
        volunteer.setRole(Role.VOLUNTEER);

        Item item = new Item();
        item.setId(1L);

        when(userService.authenticate("token")).thenReturn(volunteer);
        when(itemService.redeem(1L, volunteer)).thenReturn(item);

        mockMvc.perform(post("/api/rewards/1/redeem")
                .header("X-Auth-Token", "token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }
}
