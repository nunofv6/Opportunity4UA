package ua.tqs.opportunity4ua.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.Role;
import ua.tqs.opportunity4ua.enums.OpportunityStatus;

@DataJpaTest
public class OpportunityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Test
    void findByStatus_returnsOnlyOpen() {
        User promoter = new User(null, "promoter@test.com", "1234", Role.PROMOTER, null, null, 0, null, null);
        entityManager.persistAndFlush(promoter);
        
        Opportunity open = new Opportunity(null, "Volunteering job", "Volunteering job", LocalDateTime.now(), LocalDateTime.of(2026, 1, 30, 16, 0), "Skills", 20, 0, 15, OpportunityStatus.OPEN, promoter, null);
        Opportunity closed = new Opportunity(null, "Volunteering job", "Volunteering job", LocalDateTime.now(), LocalDateTime.of(2026, 1, 30, 16, 0), "Skills", 20, 0, 15, OpportunityStatus.CLOSED, promoter, null);

        entityManager.persistAndFlush(open);
        entityManager.persistAndFlush(closed);

        List<Opportunity> result =
                opportunityRepository.findByStatus(OpportunityStatus.OPEN);

        assertEquals(1, result.size());
        assertEquals(OpportunityStatus.OPEN, result.get(0).getStatus());
    }

    @Test
    void findByPromoter_returnsOnlyPromotersOpportunities() {
        User promoter = new User(null, "promoter@test.com", "1234", Role.PROMOTER, null, null, 0, null, null);

        entityManager.persistAndFlush(promoter);

        Opportunity op = new Opportunity(null, "Volunteering job", "Volunteering job", LocalDateTime.now(), LocalDateTime.of(2026, 1, 30, 16, 0), "Skills", 20, 0, 15, OpportunityStatus.OPEN, promoter, null);

        entityManager.persistAndFlush(op);

        List<Opportunity> result =
                opportunityRepository.findByPromoter(promoter);

        assertEquals(1, result.size());
    }
}
