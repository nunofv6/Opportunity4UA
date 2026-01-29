package ua.tqs.opportunity4ua.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import ua.tqs.opportunity4ua.entity.Reward;
import ua.tqs.opportunity4ua.entity.User;
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
        entityManager.persist(volunteer);

        Reward reward = new Reward();
        reward.setVolunteer(volunteer);
        reward.setPoints(50);
        reward.setAwardedAt(LocalDateTime.now());

        entityManager.persist(reward);
        entityManager.flush();

        List<Reward> rewards = rewardRepository.findByVolunteer(volunteer);

        assertEquals(1, rewards.size());
        assertEquals(50, rewards.get(0).getPoints());
    }
}
