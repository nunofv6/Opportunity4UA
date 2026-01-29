package ua.tqs.opportunity4ua.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User volunteer;

    @ManyToOne(optional = false)
    private Opportunity opportunity;

    private int points;

    private LocalDateTime awardedAt;
}