package ua.tqs.opportunity4ua.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CreateOpportunity {
    private String title;
    private String description;
    private String requiredSkills;
    private int maxVolunteers;
    private int points;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}