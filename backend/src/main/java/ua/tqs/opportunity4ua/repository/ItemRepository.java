package ua.tqs.opportunity4ua.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs.opportunity4ua.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByActiveTrue();
}