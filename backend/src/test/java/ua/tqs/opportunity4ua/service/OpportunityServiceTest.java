package ua.tqs.opportunity4ua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import ua.tqs.opportunity4ua.dto.CreateOpportunity;
import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.OpportunityStatus;
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
        existingOpportunity.setStatus(OpportunityStatus.OPEN);
    }

    @Test
    void promoterCanCloseOwnOpportunity() {
        User promoter = new User();
        promoter.setId(1L);
        existingOpportunity.setPromoter(promoter);
        when(opportunityRepository.findById(1L))
            .thenReturn(Optional.of(existingOpportunity));
        when(opportunityRepository.save(any(Opportunity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        Opportunity op = opportunityService.closeOpportunity(existingOpportunity.getId(), promoter);
        assertEquals(OpportunityStatus.CLOSED, op.getStatus());
        verify(opportunityRepository).save(op);
    }

    @Test
    void getAllOpportunities_returnsAll() {
        when(opportunityRepository.findAll()).thenReturn(List.of(new Opportunity()));

        List<Opportunity> result = opportunityService.getAllOpportunities();

        assertEquals(1, result.size());
    }

    @Test
    void getOpenOpportunities_returnsOnlyOpen() {
        when(opportunityRepository.findByStatus(OpportunityStatus.OPEN))
                .thenReturn(List.of(new Opportunity()));

        List<Opportunity> result = opportunityService.getOpenOpportunities();

        assertEquals(1, result.size());
    }

    @Test
    void createOpportunity_setsCorrectFields() {
        User promoter = new User();
        promoter.setId(1L);

        CreateOpportunity dto = new CreateOpportunity();
        dto.setTitle("Test");
        dto.setDescription("Desc");
        dto.setMaxVolunteers(5);
        dto.setPoints(10);

        when(opportunityRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        Opportunity created = opportunityService.createOpportunity(dto, promoter);

        assertEquals("Test", created.getTitle());
        assertEquals(OpportunityStatus.OPEN, created.getStatus());
        assertEquals(0, created.getCurrentVolunteers());
        assertEquals(promoter, created.getPromoter());
    }

    @Test
    void promoterCannotCloseOthersOpportunity() {
        User owner = new User();
        owner.setId(1L);

        User attacker = new User();
        attacker.setId(2L);

        Opportunity op = new Opportunity();
        op.setPromoter(owner);

        when(opportunityRepository.findById(1L))
                .thenReturn(Optional.of(op));

        assertThrows(RuntimeException.class,
                () -> opportunityService.closeOpportunity(1L, attacker));
    }
}
