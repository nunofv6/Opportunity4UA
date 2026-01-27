import { useEffect, useState } from "react";
import { apiRequest } from "../api/api.ts";

export default function VolunteerProfile({ token } : { token: string }) {
  const [email, setEmail] = useState("");
  const [skills, setSkills] = useState("");
  const [availability, setAvailability] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    async function loadProfile() {
      const user = await apiRequest("/users/me", "GET", null, token);
      setEmail(user.email || "");
      setSkills(user.skills || "");
      setAvailability(user.availability || "");
    }
    loadProfile();
  }, [token]);

  async function handleSave() {
    await apiRequest(
      "/users/me/profile/volunteer",
      "PUT",
      { skills, availability },
      token
    );
    setMessage("Profile updated successfully");
  }

  return (
    <div style={{ maxWidth: "500px", margin: "2rem auto" }}>
      <h2>My Profile</h2>

      <div style={{ marginBottom: "1rem" }}>
        <label style={{ fontWeight: "600", display: "block" }}>
            Email
        </label>
        <div
            style={{
            padding: "0.6rem",
            backgroundColor: "#f5f5f5",
            borderRadius: "4px",
            color: "#333"
            }}
        >
            {email}
        </div>
      </div>

      <label>Skills</label>
      <textarea
        value={skills}
        onChange={e => setSkills(e.target.value)}
        rows={4}
        style={{ width: "100%", marginBottom: "1rem" }}
      />

      <label>Availability</label>
      <input
        value={availability}
        onChange={e => setAvailability(e.target.value)}
        style={{ width: "100%", marginBottom: "1rem" }}
      />

      <button onClick={handleSave}>Save Profile</button>

      {message && <p style={{ color: "green" }}>{message}</p>}
    </div>
  );
}