import { useEffect, useState } from "react";
import { apiRequest } from "../api/api.ts";
import { Link } from "react-router-dom";
import type { Opportunity } from "../types/types";

export default function Opportunities({ token }: { token: string }) {
  const [opportunities, setOpportunities] = useState<Opportunity[]>([]);

  useEffect(() => {
    async function load() {
      const data = await apiRequest<Opportunity[]>(
        "/opportunity/open",
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
          <h3>
            <Link to={`/opportunities/${opportunity.id}`}>
              {opportunity.title}
            </Link>
          </h3>

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

          <Link to={`/opportunities/${opportunity.id}`}>
            View details →
          </Link>

          {/* Placeholder for next user story */}
          <button disabled>Apply (coming soon)</button>
        </div>
      ))}
    </div>
  );
}
