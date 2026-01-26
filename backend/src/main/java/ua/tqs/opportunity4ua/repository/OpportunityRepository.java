package ua.tqs.opportunity4ua.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.StatusOpportunity;
import ua.tqs.opportunity4ua.entity.User;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    List<Opportunity> findByStatus(StatusOpportunity status);
    List<Opportunity> findByPromoter(User promoter);
}