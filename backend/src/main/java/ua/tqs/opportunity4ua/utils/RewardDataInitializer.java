package ua.tqs.opportunity4ua.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import ua.tqs.opportunity4ua.entity.Item;
import ua.tqs.opportunity4ua.repository.ItemRepository;

@Configuration
@RequiredArgsConstructor
public class RewardDataInitializer {

    private final ItemRepository itemRepository;

    @Bean
    CommandLineRunner initRewards() {
        return args -> {

            if (itemRepository.count() > 0) {
                return;
            }

            itemRepository.save(new Item(
                    null,
                    "UA Merch Pack",
                    "Official UA t-shirt and notebook",
                    50,
                    true
            ));

            itemRepository.save(new Item(
                    null,
                    "Library Priority Access",
                    "Priority booking for library study rooms",
                    30,
                    true
            ));

            itemRepository.save(new Item(
                    null,
                    "Free Short Course",
                    "Discount voucher for UA short courses",
                    100,
                    true
            ));

            itemRepository.save(new Item(
                    null,
                    "Diploma Supplement Mention",
                    "Official recognition in diploma supplement",
                    150,
                    true
            ));
        };
    }
}
