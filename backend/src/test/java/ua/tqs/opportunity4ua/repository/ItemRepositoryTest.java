package ua.tqs.opportunity4ua.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import ua.tqs.opportunity4ua.entity.Item;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByActiveTrue_returnsOnlyActiveItems() {
        Item activeItem = new Item();
        activeItem.setName("Active Item");
        activeItem.setCostPoints(10);
        activeItem.setActive(true);
        activeItem.setDescription("An active item");

        Item inactiveItem = new Item();
        inactiveItem.setName("Inactive Item");
        inactiveItem.setCostPoints(10);
        inactiveItem.setActive(false);
        inactiveItem.setDescription("An inactive item");

        entityManager.persist(activeItem);
        entityManager.persist(inactiveItem);
        entityManager.flush();

        List<Item> result = itemRepository.findByActiveTrue();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
    }
}
