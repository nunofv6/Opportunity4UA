package ua.tqs.opportunity4ua.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ua.tqs.opportunity4ua.entity.Application;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.service.ApplicationService;
import ua.tqs.opportunity4ua.service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications(
            @RequestHeader("X-Auth-Token") String token) {

        userService.authenticate(token);

        List<Application> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Application>> getApplicationsByOpportunityId(@PathVariable Long id) {
        List<Application> applications = applicationService.getApplicationsByOpportunityId(id);
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<Application> apply(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable Long id) {

        User volunteer = userService.authenticate(token);

        Application application = applicationService.apply(id, volunteer);
        return ResponseEntity.ok(application);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Application> accept(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable Long id) {

        User promoter = userService.authenticate(token);

        Application application = applicationService.acceptApplication(id, promoter);

        return ResponseEntity.ok(application);
    }
}
