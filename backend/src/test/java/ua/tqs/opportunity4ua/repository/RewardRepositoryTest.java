package ua.tqs.opportunity4ua.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.entity.Reward;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.OpportunityStatus;
import ua.tqs.opportunity4ua.enums.Role;

@DataJpaTest
class RewardRepositoryTest {

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByVolunteer_returnsRewards() {
        User volunteer = new User();
        volunteer.setEmail("vol@test.com");
        volunteer.setPassword("pass");
        volunteer.setRole(Role.VOLUNTEER);
        volunteer.setPointBalance(0);
        entityManager.persist(volunteer);

        User promoter = new User(null, "promoter@test.com", "1234", Role.PROMOTER, null, null, 0, null, null);
        entityManager.persist(promoter);
        Opportunity op = new Opportunity(null, "Volunteering job", "Volunteering job", LocalDateTime.now(), LocalDateTime.of(2026, 1, 30, 16, 0), "Skills", 20, 0, 15, OpportunityStatus.OPEN, promoter, null);
        entityManager.persist(op);
        
        Reward reward = new Reward();
        reward.setVolunteer(volunteer);
        reward.setPoints(50);
        reward.setAwardedAt(LocalDateTime.now());
        reward.setOpportunity(op);

        entityManager.persist(reward);
        entityManager.flush();

        List<Reward> rewards = rewardRepository.findByVolunteer(volunteer);

        assertEquals(1, rewards.size());
        assertEquals(50, rewards.get(0).getPoints());
    }
}
