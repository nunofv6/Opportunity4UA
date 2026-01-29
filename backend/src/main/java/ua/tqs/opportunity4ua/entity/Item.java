package ua.tqs.opportunity4ua.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(length = 2000, nullable=false)
    private String description;

    @Column(nullable=false)
    private int costPoints;

    @Column(nullable=false)
    private boolean active = true;
}
