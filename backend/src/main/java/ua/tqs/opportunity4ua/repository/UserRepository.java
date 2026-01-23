package ua.tqs.opportunity4ua.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs.opportunity4ua.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
