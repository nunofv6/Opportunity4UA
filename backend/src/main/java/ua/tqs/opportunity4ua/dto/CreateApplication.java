package ua.tqs.opportunity4ua.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateApplication {
    private Long id;
    private String status;
    private LocalDateTime appliedAt;
    private Long opportunityId;
    private String opportunityTitle;
}
