package ua.tqs.opportunity4ua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.StatusOpportunity;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.repository.OpportunityRepository;

public class OpportunityServiceTest {
    @Mock
    private OpportunityRepository opportunityRepository;

    @InjectMocks
    private OpportunityService opportunityService;

    private Opportunity existingOpportunity;

    @BeforeEach
    void setup() {
        org.mockito.MockitoAnnotations.openMocks(this);
        existingOpportunity = new Opportunity();
        existingOpportunity.setId(1L);
        existingOpportunity.setStatus(StatusOpportunity.OPEN);
    }

    @Test
    void promoterCanCloseOwnOpportunity() {
        User promoter = new User();
        promoter.setId(1L);
        existingOpportunity.setPromoter(promoter);
        when(opportunityRepository.findById(1L))
            .thenReturn(Optional.of(existingOpportunity));
        Opportunity op = opportunityService.closeOpportunity(existingOpportunity.getId(), promoter);
        assertEquals(StatusOpportunity.CLOSED, op.getStatus());
        verify(opportunityRepository).save(op);
    }
}
