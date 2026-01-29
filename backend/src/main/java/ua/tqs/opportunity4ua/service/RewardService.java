package ua.tqs.opportunity4ua.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.tqs.opportunity4ua.entity.Reward;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.repository.RewardRepository;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;

    public List<Reward> getRewardsForVolunteer(User volunteer) {
        return rewardRepository.findByVolunteer(volunteer);
    }
}