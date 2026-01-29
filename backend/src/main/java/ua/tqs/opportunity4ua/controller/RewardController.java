package ua.tqs.opportunity4ua.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ua.tqs.opportunity4ua.entity.Reward;
import ua.tqs.opportunity4ua.entity.Item;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.service.ItemService;
import ua.tqs.opportunity4ua.service.RewardService;
import ua.tqs.opportunity4ua.service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;
    private final ItemService itemService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Item>> listActiveRewards() {
        return ResponseEntity.ok(itemService.listActiveItems());
    }
    
    @GetMapping("/me")
    public ResponseEntity<List<Reward>> myRewards(
            @RequestHeader("X-Auth-Token") String token) {

        User user = userService.authenticate(token);
        return ResponseEntity.ok(rewardService.getRewardsForVolunteer(user));
    }

    @GetMapping("/items/me")
    public ResponseEntity<List<Item>> myRewardItems(
            @RequestHeader("X-Auth-Token") String token) {

        User user = userService.authenticate(token);
        return ResponseEntity.ok(user.getRedeemedItems());
    }

    @PostMapping("/{id}/redeem")
    public ResponseEntity<Item> redeem(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable Long id) {

        User user = userService.authenticate(token);
        Item item = itemService.redeem(id, user);
        return ResponseEntity.ok(item);
    }
}
