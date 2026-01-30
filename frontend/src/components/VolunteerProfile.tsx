import { useEffect, useState } from "react";
import { apiRequest } from "../api/api.ts";

export default function VolunteerProfile({ token } : { token: string }) {
  const [email, setEmail] = useState("");
  const [skills, setSkills] = useState("");
  const [availability, setAvailability] = useState("");
  const [pointBalance, setPointBalance] = useState<number | null>(null);
  const [message, setMessage] = useState("");
  const [redeemedItems, setRedeemedItems] = useState<any[]>([]);

  useEffect(() => {
    async function loadProfile() {
      const user = await apiRequest("/users/me", "GET", null, token);
      setEmail(user.email || "");
      setSkills(user.skills || "");
      setAvailability(user.availability || "");
      setPointBalance(user.pointBalance || 0);

      const rewards = await apiRequest(
        "/rewards/items/me",
        "GET",
        null,
        token
      );
      setRedeemedItems(rewards);
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
            marginTop: "0.5rem",
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
        data-testid="volunteer-profile-skills"
        value={skills}
        onChange={e => setSkills(e.target.value)}
        rows={4}
        style={{ width: "100%", marginBottom: "1rem" }}
      />

      <label>Availability</label>
      <input
        data-testid="volunteer-profile-availability"
        value={availability}
        onChange={e => setAvailability(e.target.value)}
        style={{ width: "100%", marginBottom: "1rem" }}
      />

      <div style={{ marginBottom: "1rem" }}>
        <label style={{ fontWeight: "600", display: "block" }}>
            Points
        </label>
        <div
            style={{
            marginTop: "0.5rem",
            backgroundColor: "#f5f5f5",
            borderRadius: "4px",
            color: "#333"
            }}
        >
            {pointBalance ?? 0}
        </div>
      </div>
      <div style={{ marginBottom: "1.5rem" }}>
        <h3 style={{ marginBottom: "0.75rem" }}>My Redeemed Items</h3>

        {redeemedItems.length === 0 ? (
          <p style={{ color: "#666" }}>
            You have not redeemed any items yet.
          </p>
        ) : (
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
              gap: "1rem"
            }}
          >
            {redeemedItems.map(item => (
              <div
                key={item.id}
                style={{
                  border: "1px solid #ddd",
                  borderRadius: "8px",
                  padding: "1rem",
                  backgroundColor: "#fafafa"
                }}
              >
                <h4 style={{ marginTop: 0 }}>{item.name}</h4>

                <p style={{ fontSize: "0.9rem", color: "#555" }}>
                  {item.description}
                </p>

                <p style={{ marginTop: "0.75rem", fontWeight: "600" }}>
                  Cost: {item.costPoints} pts
                </p>
              </div>
            ))}
          </div>
        )}
      </div>

      <button data-testid="nav-volunteer-profile-save" onClick={handleSave}>Save Profile</button>

      {message && <p style={{ color: "green" }}>{message}</p>}
    </div>
  );
}