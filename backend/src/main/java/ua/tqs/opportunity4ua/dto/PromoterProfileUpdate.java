package ua.tqs.opportunity4ua.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PromoterProfileUpdate {

    @NotBlank(message = "Affiliation is required")
    @Size(max = 150)
    private String affiliation;

    public PromoterProfileUpdate() {}

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getAffiliation() {
        return affiliation;
    }
}
