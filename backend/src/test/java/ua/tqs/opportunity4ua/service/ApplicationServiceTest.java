package ua.tqs.opportunity4ua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.tqs.opportunity4ua.entity.Application;
import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.ApplicationStatus;
import ua.tqs.opportunity4ua.enums.OpportunityStatus;
import ua.tqs.opportunity4ua.enums.Role;
import ua.tqs.opportunity4ua.repository.ApplicationRepository;
import ua.tqs.opportunity4ua.repository.OpportunityRepository;
import ua.tqs.opportunity4ua.repository.RewardRepository;
import ua.tqs.opportunity4ua.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private OpportunityRepository opportunityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private User volunteer;
    private User promoter;
    private Opportunity opportunity;

    @BeforeEach
    void setUp() {
        volunteer = new User();
        volunteer.setId(1L);
        volunteer.setRole(Role.VOLUNTEER);
        volunteer.setPointBalance(0);

        promoter = new User();
        promoter.setId(2L);
        promoter.setRole(Role.PROMOTER);

        opportunity = new Opportunity();
        opportunity.setId(10L);
        opportunity.setStatus(OpportunityStatus.OPEN);
        opportunity.setMaxVolunteers(2);
        opportunity.setCurrentVolunteers(0);
        opportunity.setPromoter(promoter);
        opportunity.setPoints(50);
    }

    @Test
    void volunteerCanApplyToOpenOpportunity() {
        User volunteer = new User();
        volunteer.setRole(Role.VOLUNTEER);

        Opportunity op = new Opportunity();
        op.setStatus(OpportunityStatus.OPEN);
        op.setMaxVolunteers(5);
        op.setCurrentVolunteers(0);

        when(opportunityRepository.findById(1L))
                .thenReturn(Optional.of(op));
        when(applicationRepository.existsByVolunteerAndOpportunity(volunteer, op))
                .thenReturn(false);
        when(applicationRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        Application app = applicationService.applyToOpportunity(1L, volunteer);

        assertEquals(ApplicationStatus.PENDING, app.getStatus());
        assertEquals(volunteer, app.getVolunteer());
    }

    @Test
    void cannotApplyTwice() {
        User volunteer = new User();
        volunteer.setRole(Role.VOLUNTEER);

        Opportunity op = new Opportunity();
        op.setStatus(OpportunityStatus.OPEN);

        when(opportunityRepository.findById(1L))
                .thenReturn(Optional.of(op));

        assertThrows(RuntimeException.class,
                () -> applicationService.applyToOpportunity(1L, volunteer));
    }

    @Test
    void promoterCanAcceptPendingApplication() {
        User promoter = new User();
        promoter.setId(1L);
        promoter.setRole(Role.PROMOTER);

        Opportunity op = new Opportunity();
        op.setPromoter(promoter);
        op.setMaxVolunteers(1);
        op.setCurrentVolunteers(0);
        op.setStatus(OpportunityStatus.OPEN);

        Application app = new Application();
        app.setId(10L);
        app.setStatus(ApplicationStatus.PENDING);
        app.setOpportunity(op);

        when(applicationRepository.findById(10L))
                .thenReturn(Optional.of(app));
        when(applicationRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(opportunityRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        Application accepted =
                applicationService.acceptApplication(10L, promoter);

        assertEquals(ApplicationStatus.ACCEPTED, accepted.getStatus());
        assertEquals(1, op.getCurrentVolunteers());
        assertEquals(OpportunityStatus.CLOSED, op.getStatus());
    }

    @Test
    void promoterCanCompleteAcceptedApplication_andPointsAreGranted() {
        User promoter = new User();
        promoter.setId(1L);
        promoter.setRole(Role.PROMOTER);

        User volunteer = new User();
        volunteer.setId(2L);
        volunteer.setPointBalance(10);

        Opportunity op = new Opportunity();
        op.setPromoter(promoter);
        op.setPoints(20);

        Application app = new Application();
        app.setId(5L);
        app.setStatus(ApplicationStatus.ACCEPTED);
        app.setVolunteer(volunteer);
        app.setOpportunity(op);

        when(applicationRepository.findById(5L))
                .thenReturn(Optional.of(app));
        when(applicationRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(userRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(rewardRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        Application completed =
                applicationService.completeApplication(5L, promoter);

        assertEquals(ApplicationStatus.COMPLETED, completed.getStatus());
        assertEquals(30, volunteer.getPointBalance());
    }

    @Test
    void applyToOpportunity_nonVolunteer_throws() {
        volunteer.setRole(Role.PROMOTER);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> applicationService.applyToOpportunity(10L, volunteer)
        );

        assertEquals("Only volunteers can apply", ex.getMessage());
    }

    @Test
    void acceptApplication_success_and_closes_opportunity() {
        Application application = new Application();
        application.setId(1L);
        application.setStatus(ApplicationStatus.PENDING);
        application.setVolunteer(volunteer);
        application.setOpportunity(opportunity);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(applicationRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(opportunityRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Application result = applicationService.acceptApplication(1L, promoter);

        assertEquals(ApplicationStatus.ACCEPTED, result.getStatus());
        assertEquals(1, opportunity.getCurrentVolunteers());
    }
}
