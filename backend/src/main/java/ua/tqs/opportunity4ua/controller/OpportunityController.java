package ua.tqs.opportunity4ua.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ua.tqs.opportunity4ua.dto.CreateOpportunity;
import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.Role;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.service.OpportunityService;
import ua.tqs.opportunity4ua.service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/opportunity")
@RequiredArgsConstructor
public class OpportunityController {

    private final UserService userService;
    private final OpportunityService opportunityService;

    @GetMapping
    public ResponseEntity<List<Opportunity>> getAllOpportunities() {
        List<Opportunity> opportunities = opportunityService.getAllOpportunities();
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/open")
    public ResponseEntity<List<Opportunity>> getOpenOpportunities() {
        List<Opportunity> opportunities = opportunityService.getOpenOpportunities();
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Opportunity> getOpportunityById(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable Long id) {

        userService.authenticate(token);

        return opportunityService.getOpportunityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Opportunity> closeOpportunity(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable Long id) {

        User user = userService.authenticate(token);

        if (user.getRole() != Role.PROMOTER) {
            return ResponseEntity.status(403).build();
        }

        Opportunity opportunity = opportunityService.closeOpportunity(id, user);
        return ResponseEntity.ok(opportunity);
    }

    @PostMapping
    public ResponseEntity<Opportunity> createOpportunity(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody CreateOpportunity opportunity) {

        User user = userService.authenticate(token);

        if (user.getRole() != Role.PROMOTER) {
            return ResponseEntity.status(403).build();
        }

        Opportunity saved = opportunityService.createOpportunity(opportunity, user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Opportunity>> getMyOpportunities(
            @RequestHeader("X-Auth-Token") String token) {

        User user = userService.authenticate(token);

        if (user.getRole() != Role.PROMOTER) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
            opportunityService.getOpportunitiesByPromoter(user)
        );
    }
}