package ua.tqs.opportunity4ua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import ua.tqs.opportunity4ua.entity.Application;
import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.ApplicationStatus;
import ua.tqs.opportunity4ua.enums.OpportunityStatus;
import ua.tqs.opportunity4ua.enums.Role;
import ua.tqs.opportunity4ua.repository.ApplicationRepository;
import ua.tqs.opportunity4ua.repository.OpportunityRepository;

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
        when(applicationRepository.existsByVolunteerAndOpportunity(volunteer, op))
                .thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> applicationService.apply(1L, volunteer));
    }
}
