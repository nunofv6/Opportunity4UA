package ua.tqs.opportunity4ua.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Token {
    @Id
    private String token;

    @ManyToOne
    private User user;

    private LocalDateTime createdAt;
}
