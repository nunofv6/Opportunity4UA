package ua.tqs.opportunity4ua.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.tqs.opportunity4ua.entity.Item;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.Role;
import ua.tqs.opportunity4ua.repository.ItemRepository;
import ua.tqs.opportunity4ua.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Item redeem(Long rewardItemId, User volunteer) {

        if (volunteer.getRole() != Role.VOLUNTEER) {
            throw new RuntimeException("Only volunteers can redeem points");
        }

        Item item = itemRepository.findById(rewardItemId)
                .orElseThrow(() -> new RuntimeException("Reward item not found"));

        if (!item.isActive()) {
            throw new RuntimeException("Reward item is not active");
        }

        if (volunteer.getPointBalance() < item.getCostPoints()) {
            throw new RuntimeException("Not enough points");
        }

        volunteer.setPointBalance(volunteer.getPointBalance() - item.getCostPoints());
        volunteer.getRedeemedItems().add(item);

        userRepository.save(volunteer);

        return item;
    }

    public List<Item> listActiveItems() {
        return itemRepository.findByActiveTrue();
    }
}