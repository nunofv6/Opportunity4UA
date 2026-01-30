package ua.tqs.opportunity4ua.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import ua.tqs.opportunity4ua.entity.Item;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.Role;
import ua.tqs.opportunity4ua.repository.ItemRepository;
import ua.tqs.opportunity4ua.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Bean
    CommandLineRunner initData() {
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

            userRepository.save(new User(
                    null,
                    "test@example.com",
                    "password",
                    Role.VOLUNTEER,
                    null,
                    null,
                    10000,
                    null,
                    null
            ));

            userRepository.save(new User(
                    null,
                    "lowpoints@example.com",
                    "password",
                    Role.VOLUNTEER,
                    null,
                    null,
                    5,
                    null,
                    null
            ));
        };
    }
}
