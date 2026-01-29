package ua.tqs.opportunity4ua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.tqs.opportunity4ua.entity.Reward;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.repository.RewardRepository;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private RewardService rewardService;

    @Test
    void getRewardsForVolunteer_returnsRewards() {
        User user = new User();
        Reward reward = new Reward();

        when(rewardRepository.findByVolunteer(user)).thenReturn(List.of(reward));

        List<Reward> result = rewardService.getRewardsForVolunteer(user);

        assertEquals(1, result.size());
        verify(rewardRepository).findByVolunteer(user);
    }
}