import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { apiRequest } from "../api/api";

export default function OpportunityDetails({ token }) {
  const { id } = useParams();
  const [opportunity, setOpportunity] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    async function load() {
      try {
        const data = await apiRequest(
          `/opportunity/${id}`,
          "GET",
          null,
          token
        );
        setOpportunity(data);
      } catch {
        setError("Opportunity not found");
      }
    }
    load();
  }, [id, token]);

  if (error) {
    return <p style={{ textAlign: "center" }}>{error}</p>;
  }

  if (!opportunity) {
    return <p style={{ textAlign: "center" }}>Loading...</p>;
  }

  return (
    <div style={{ maxWidth: "800px", margin: "2rem auto" }}>
      <h2>{opportunity.title}</h2>

      <p>{opportunity.description}</p>

      <p>
        <strong>Required skills:</strong>{" "}
        {opportunity.requiredSkills || "Any"}
      </p>

      <p>
        <strong>Start:</strong>{" "}
        {new Date(opportunity.startDate).toLocaleString()}
      </p>

      <p>
        <strong>End:</strong>{" "}
        {new Date(opportunity.endDate).toLocaleString()}
      </p>

      <p>
        <strong>Total slots:</strong>{" "}
        {opportunity.maxVolunteers}
      </p>

      <p>
        <strong>Available slots:</strong>{" "}
        {opportunity.maxVolunteers - opportunity.currentVolunteers}
      </p>

      <p>
        <strong>Points:</strong> {opportunity.points}
      </p>

      {/* Next user story */}
      <button disabled>Apply (coming soon)</button>
    </div>
  );
}