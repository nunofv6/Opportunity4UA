package ua.tqs.opportunity4ua.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.Role;
import ua.tqs.opportunity4ua.entity.StatusOpportunity;
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

    @PostMapping
    public ResponseEntity<Opportunity> createOpportunity(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody Opportunity opportunity) {

        User user = userService.authenticate(token);

        if (user.getRole() != Role.PROMOTER) {
            return ResponseEntity.status(403).build();
        }

        opportunity.setPromoter(user);
        opportunity.setStatus(StatusOpportunity.OPEN);

        Opportunity saved = opportunityService.createOpportunity(opportunity);
        return ResponseEntity.ok(saved);
    }
}
