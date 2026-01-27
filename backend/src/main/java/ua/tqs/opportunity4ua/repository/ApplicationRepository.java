package ua.tqs.opportunity4ua.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs.opportunity4ua.entity.Application;
import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.User;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByVolunteerAndOpportunity(User volunteer, Opportunity opportunity);
    Optional<Application> findByVolunteerAndOpportunity(User volunteer, Opportunity opportunity);
    List<Application> findByOpportunity(Opportunity opportunity);
}
