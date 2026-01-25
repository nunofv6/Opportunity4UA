package ua.tqs.opportunity4ua.dto;

import jakarta.validation.constraints.Size;

public class VolunteerProfileUpdate {

    @Size(max = 500, message = "Skills description is too long")
    private String skills;

    @Size(max = 255, message = "Availability description is too long")
    private String availability;

    public VolunteerProfileUpdate() {}

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
