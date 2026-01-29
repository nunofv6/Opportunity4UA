package ua.tqs.opportunity4ua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.tqs.opportunity4ua.entity.Item;
import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.Role;
import ua.tqs.opportunity4ua.repository.ItemRepository;
import ua.tqs.opportunity4ua.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemService itemService;

    private User volunteer;
    private Item item;

    @BeforeEach
    void setUp() {
        volunteer = new ua.tqs.opportunity4ua.entity.User();
        volunteer.setRole(Role.VOLUNTEER);
        volunteer.setPointBalance(100);
        volunteer.setRedeemedItems(new ArrayList<>());

        item = new Item();
        item.setId(1L);
        item.setCostPoints(50);
        item.setActive(true);
    }

    @Test
    void redeem_success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item result = itemService.redeem(1L, volunteer);

        assertEquals(50, volunteer.getPointBalance());
        assertTrue(volunteer.getRedeemedItems().contains(item));
        verify(userRepository).save(volunteer);
    }

    @Test
    void redeem_notEnoughPoints_throws() {
        volunteer.setPointBalance(10);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> itemService.redeem(1L, volunteer)
        );

        assertEquals("Not enough points", ex.getMessage());
    }

    @Test
    void listActiveItems_returnsItems() {
        when(itemRepository.findByActiveTrue()).thenReturn(List.of(item));

        List<Item> result = itemService.listActiveItems();

        assertEquals(1, result.size());
    }
}
