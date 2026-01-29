package ua.tqs.opportunity4ua.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs.opportunity4ua.entity.Reward;
import ua.tqs.opportunity4ua.entity.User;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByVolunteer(User volunteer);
}
