import { useEffect, useState } from "react";
import { apiRequest } from "../api/api";

export default function Opportunities({ token }) {
  const [opportunities, setOpportunities] = useState([]);

  useEffect(() => {
    async function load() {
      const data = await apiRequest(
        "/opportunity",
        "GET",
        null,
        token
      );
      setOpportunities(data);
    }
    load();
  }, [token]);

  if (opportunities.length === 0) {
    return (
      <div style={{ maxWidth: "800px", margin: "2rem auto" }}>
        <h2>Available Opportunities</h2>
        <p>No opportunities available at the moment.</p>
      </div>
    );
  }

  return (
    <div style={{ maxWidth: "800px", margin: "2rem auto" }}>
      <h2>Available Opportunities</h2>

      {opportunities.map(opportunity => (
        <div
          key={opportunity.id}
          style={{
            border: "1px solid #ddd",
            borderRadius: "6px",
            padding: "1rem",
            marginBottom: "1rem"
          }}
        >
          <h3>{opportunity.title}</h3>

          <p>{opportunity.description}</p>

          <p>
            <strong>Required skills:</strong>{" "}
            {opportunity.requiredSkills || "Any"}
          </p>

          <p>
            <strong>When:</strong>{" "}
            {new Date(opportunity.startDate).toLocaleString()} —{" "}
            {new Date(opportunity.endDate).toLocaleString()}
          </p>

          <p>
            <strong>Volunteers needed:</strong>{" "}
            {opportunity.maxVolunteers}
          </p>

          <p>
            <strong>Points:</strong> {opportunity.points}
          </p>

          {/* Placeholder for next user story */}
          <button disabled>Apply (coming soon)</button>
        </div>
      ))}
    </div>
  );
}
