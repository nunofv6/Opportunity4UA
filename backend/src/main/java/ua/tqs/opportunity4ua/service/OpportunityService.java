package ua.tqs.opportunity4ua.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ua.tqs.opportunity4ua.dto.CreateOpportunity;
import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.StatusOpportunity;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.repository.OpportunityRepository;

@Service
@RequiredArgsConstructor
public class OpportunityService {
    
    private final OpportunityRepository opportunityRepository;

    public List<Opportunity> getAllOpportunities() {
        return opportunityRepository.findAll();
    }

    public List<Opportunity> getOpenOpportunities() {
        return opportunityRepository.findByStatus(StatusOpportunity.OPEN);
    }

    public Optional<Opportunity> getOpportunityById(Long id) {
        return opportunityRepository.findById(id);
    }

    public List<Opportunity> getOpportunitiesByPromoter(User promoter) {
        return opportunityRepository.findByPromoter(promoter);
    }

    public Opportunity createOpportunity(CreateOpportunity opportunity, User user) {
        Opportunity newOpportunity = new Opportunity();
        newOpportunity.setTitle(opportunity.getTitle());
        newOpportunity.setDescription(opportunity.getDescription());
        newOpportunity.setRequiredSkills(opportunity.getRequiredSkills());
        newOpportunity.setMaxVolunteers(opportunity.getMaxVolunteers());
        newOpportunity.setPoints(opportunity.getPoints());
        newOpportunity.setStartDate(opportunity.getStartDate());
        newOpportunity.setEndDate(opportunity.getEndDate());
        newOpportunity.setPromoter(user);
        newOpportunity.setStatus(StatusOpportunity.OPEN);
        newOpportunity.setCurrentVolunteers(0);

        return opportunityRepository.save(newOpportunity);
    }

    public Opportunity closeOpportunity(Long id, User promoter) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        if (!opportunity.getPromoter().getId().equals(promoter.getId())) {
            throw new RuntimeException("Not allowed to close this opportunity");
        }

        opportunity.setStatus(StatusOpportunity.CLOSED);
        return opportunityRepository.save(opportunity);
    }
}
