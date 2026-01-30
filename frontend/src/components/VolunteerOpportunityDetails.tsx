import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { apiRequest } from "../api/api.ts";
import type { Opportunity } from "../types/types.ts";

export default function OpportunityDetails({ token }: { token: string }) {
  const { id } = useParams();
  const [opportunity, setOpportunity] = useState<Opportunity | null>(null);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

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

  async function applyToOpportunity(opportunityId: number) {
    setLoading(true);
    setError("");
    setMessage("");

    try {
      await apiRequest(
        `/application/${opportunityId}/apply`,
        "POST",
        null,
        token
      );
      setMessage("Application submitted successfully");
    } catch (err: any) {
      setError(err.message || "Failed to apply");
    } finally {
      setLoading(false);
    }
  }

  const availableSlots =
    opportunity.maxVolunteers - opportunity.currentVolunteers;

  const canApply =
    opportunity.status === "OPEN" && availableSlots > 0;

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

      <button
        data-testid="nav-opportunity-apply"
        onClick={() => applyToOpportunity(opportunity.id)}
        disabled={!canApply || loading}
        style={{
          marginTop: "1rem",
          padding: "0.6rem 1.2rem",
          cursor: canApply ? "pointer" : "not-allowed",
          opacity: canApply ? 1 : 0.6
        }}
      >
        {loading ? "Applying..." : "Apply"}
      </button>
      {message && (
        <p style={{ color: "green", marginTop: "1rem" }}>{message}</p>
      )}
      {error && (
        <p style={{ color: "red", marginTop: "1rem" }}>{error}</p>
      )}
    </div>
  );
}