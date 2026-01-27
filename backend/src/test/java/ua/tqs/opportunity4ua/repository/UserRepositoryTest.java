package ua.tqs.opportunity4ua.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import ua.tqs.opportunity4ua.entity.User;
import ua.tqs.opportunity4ua.enums.Role;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_existingUser_returnsUser() {
        User user = new User(null, "test@mail.com", "pass", Role.VOLUNTEER, null, null, null);
        entityManager.persistAndFlush(user);

        Optional<User> found = userRepository.findByEmail("test@mail.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@mail.com");
    }

    @Test
    void existsByEmail_existingUser_returnsTrue() {
        User user = new User(null, "exists@mail.com", "pass", Role.ADMIN, null, null, null);
        entityManager.persistAndFlush(user);

        boolean exists = userRepository.existsByEmail("exists@mail.com");

        assertThat(exists).isTrue();
    }

    @Test
    void save_duplicateEmail_throwsException() {
        User user1 = new User(null, "dup@mail.com", "pass", Role.VOLUNTEER, null, null, null);
        User user2 = new User(null, "dup@mail.com", "pass", Role.ADMIN, null, null, null);

        entityManager.persistAndFlush(user1);

        assertThatThrownBy(() -> {
            entityManager.persistAndFlush(user2);
        }).isInstanceOf(Exception.class);
    }
}
