package ua.tqs.opportunity4ua.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import ua.tqs.opportunity4ua.entity.Application;
import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.Role;
import ua.tqs.opportunity4ua.enums.ApplicationStatus;
import ua.tqs.opportunity4ua.enums.OpportunityStatus;

@DataJpaTest
class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User volunteer;
    private Opportunity opportunity;

    @BeforeEach
    void setup() {
        volunteer = new User();
        volunteer.setEmail("volunteer@test.com");
        volunteer.setPassword("pass");
        volunteer.setRole(Role.VOLUNTEER);
        volunteer.setPointBalance(0);
        entityManager.persist(volunteer);

        User promoter = new User();
        promoter.setEmail("promoter@test.com");
        promoter.setPassword("pass");
        promoter.setRole(Role.PROMOTER);
        promoter.setPointBalance(0);
        entityManager.persist(promoter);

        opportunity = new Opportunity();
        opportunity.setTitle("Test Opportunity");
        opportunity.setPromoter(promoter);
        opportunity.setMaxVolunteers(5);
        opportunity.setCurrentVolunteers(0);
        opportunity.setStatus(OpportunityStatus.OPEN);
        opportunity.setStartDate(LocalDateTime.now());
        opportunity.setEndDate(LocalDateTime.now().plusDays(10));
        opportunity.setDescription("Opportunity Description");
        opportunity.setRequiredSkills("Skills");
        opportunity.setPoints(20);
        entityManager.persist(opportunity);

        entityManager.flush();
    }

    @Test
    void existsByVolunteerAndOpportunity_returnsTrue() {
        Application app = new Application();
        app.setVolunteer(volunteer);
        app.setOpportunity(opportunity);
        app.setStatus(ApplicationStatus.PENDING);
        app.setAppliedAt(LocalDateTime.now());

        entityManager.persist(app);
        entityManager.flush();

        boolean exists =
                applicationRepository.existsByVolunteerAndOpportunity(volunteer, opportunity);

        assertTrue(exists);
    }

    @Test
    void findByVolunteerAndOpportunity_returnsApplication() {
        Application app = new Application();
        app.setVolunteer(volunteer);
        app.setOpportunity(opportunity);
        app.setStatus(ApplicationStatus.PENDING);
        app.setAppliedAt(LocalDateTime.now());

        entityManager.persist(app);
        entityManager.flush();

        Optional<Application> result =
                applicationRepository.findByVolunteerAndOpportunity(volunteer, opportunity);

        assertTrue(result.isPresent());
    }

    @Test
    void findByOpportunity_returnsList() {
        Application app = new Application();
        app.setVolunteer(volunteer);
        app.setOpportunity(opportunity);
        app.setStatus(ApplicationStatus.PENDING);
        app.setAppliedAt(LocalDateTime.now());

        entityManager.persist(app);
        entityManager.flush();

        List<Application> apps =
                applicationRepository.findByOpportunity(opportunity);

        assertEquals(1, apps.size());
    }

    @Test
    void findByVolunteer_returnsList() {
        Application app = new Application();
        app.setVolunteer(volunteer);
        app.setOpportunity(opportunity);
        app.setStatus(ApplicationStatus.PENDING);
        app.setAppliedAt(LocalDateTime.now());

        entityManager.persist(app);
        entityManager.flush();

        List<Application> apps =
                applicationRepository.findByVolunteer(volunteer);

        assertEquals(1, apps.size());
    }
}
