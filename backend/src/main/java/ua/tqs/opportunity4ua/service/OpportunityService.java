package ua.tqs.opportunity4ua.service;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.StatusOpportunity;
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

    public Opportunity createOpportunity(Opportunity opportunity) {
        return opportunityRepository.save(opportunity);
    }
}
