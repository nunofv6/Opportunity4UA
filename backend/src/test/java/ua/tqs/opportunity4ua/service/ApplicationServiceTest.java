package ua.tqs.opportunity4ua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

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

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private OpportunityRepository opportunityRepository;

    @InjectMocks
    private ApplicationService applicationService;

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

        Application app = applicationService.apply(1L, volunteer);

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
                () -> applicationService.apply(1L, volunteer));
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
}
