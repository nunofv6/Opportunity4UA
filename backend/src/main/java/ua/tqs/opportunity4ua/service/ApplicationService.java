package ua.tqs.opportunity4ua.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.tqs.opportunity4ua.entity.Application;
import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.ApplicationStatus;
import ua.tqs.opportunity4ua.enums.OpportunityStatus;
import ua.tqs.opportunity4ua.enums.Role;
import ua.tqs.opportunity4ua.repository.ApplicationRepository;
import ua.tqs.opportunity4ua.repository.OpportunityRepository;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final OpportunityRepository opportunityRepository;

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public List<Application> getApplicationsByOpportunityId(Long opportunityId) {
        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));
        return applicationRepository.findByOpportunity(opportunity);
    }

    public Application apply(Long opportunityId, User volunteer) {

        if (volunteer.getRole() != Role.VOLUNTEER) {
            throw new RuntimeException("Only volunteers can apply");
        }

        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        if (opportunity.getStatus() != OpportunityStatus.OPEN) {
            throw new RuntimeException("Opportunity is closed");
        }

        if (opportunity.getCurrentVolunteers() >= opportunity.getMaxVolunteers()) {
            throw new RuntimeException("Opportunity is full");
        }

        if (applicationRepository.existsByVolunteerAndOpportunity(volunteer, opportunity)) {
            throw new RuntimeException("Already applied");
        }

        Application application = new Application();
        application.setVolunteer(volunteer);
        application.setOpportunity(opportunity);
        application.setStatus(ApplicationStatus.PENDING);
        application.setAppliedAt(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    public Application acceptApplication(Long applicationId, User promoter) {
        if (promoter.getRole() != Role.PROMOTER) {
            throw new RuntimeException("Only promoters can accept applications");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Opportunity opportunity = application.getOpportunity();

        if (!opportunity.getPromoter().getId().equals(promoter.getId())) {
            throw new RuntimeException("Not allowed");
        }

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new RuntimeException("Application is not pending");
        }

        application.setStatus(ApplicationStatus.ACCEPTED);
        opportunity.setCurrentVolunteers(opportunity.getCurrentVolunteers() + 1);
        if (opportunity.getCurrentVolunteers() >= opportunity.getMaxVolunteers()) {
            opportunity.setStatus(OpportunityStatus.CLOSED);
        }
        
        opportunityRepository.save(opportunity);
        return applicationRepository.save(application);
    }
}
