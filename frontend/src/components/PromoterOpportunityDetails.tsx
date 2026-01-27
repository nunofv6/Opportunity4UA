import { useEffect, useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { apiRequest } from "../api/api";
import type { Opportunity, Application } from "../types/types";

export default function PromoterOpportunityDetails({ token }: { token: string }) {
  const { id } = useParams();

  const [opportunity, setOpportunity] = useState<Opportunity | null>(null);
  const [applications, setApplications] = useState<Application[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const opportunityId = useMemo(() => Number(id), [id]);

  useEffect(() => {
    async function load() {
      setLoading(true);
      setError("");

      if (!opportunityId || Number.isNaN(opportunityId)) {
        setError("Invalid opportunity id");
        setLoading(false);
        return;
      }

      try {
        const opp = await apiRequest<Opportunity>(
          `/opportunity/${opportunityId}`,
          "GET",
          null,
          token
        );
        setOpportunity(opp);

        const apps = await apiRequest<Application[]>(
          `/opportunity/${opportunityId}/applications`,
          "GET",
          null,
          token
        );
        setApplications(apps);
      } catch (err: any) {
        setError(err?.message || "Failed to load opportunity details");
      } finally {
        setLoading(false);
      }
    }

    load();
  }, [opportunityId, token]);

  if (loading) return <p style={{ textAlign: "center" }}>Loading...</p>;
  if (error) return <p style={{ textAlign: "center", color: "red" }}>{error}</p>;
  if (!opportunity) return <p style={{ textAlign: "center" }}>Not found</p>;

  const availableSlots =
    opportunity.maxVolunteers - (opportunity.currentVolunteers ?? 0);

  return (
    <div style={{ maxWidth: "900px", margin: "2rem auto" }}>
      <div style={{ marginBottom: "1rem" }}>
        <Link to="/my-opportunities">← Back to my opportunities</Link>
      </div>

      <h2 style={{ marginBottom: "0.5rem" }}>{opportunity.title}</h2>

      <div
        style={{
          border: "1px solid #ddd",
          borderRadius: "8px",
          padding: "1rem",
          marginBottom: "1.5rem",
        }}
      >
        <p style={{ marginTop: 0 }}>{opportunity.description}</p>

        <p>
          <strong>Status:</strong> {opportunity.status}
        </p>

        <p>
          <strong>When:</strong>{" "}
          {new Date(opportunity.startDate).toLocaleString()} —{" "}
          {new Date(opportunity.endDate).toLocaleString()}
        </p>

        <p>
          <strong>Slots:</strong>{" "}
          {(opportunity.currentVolunteers ?? 0)} / {opportunity.maxVolunteers}{" "}
          <span style={{ marginLeft: "0.5rem", color: "#555" }}>
            (available: {availableSlots})
          </span>
        </p>

        <p>
          <strong>Points:</strong> {opportunity.points}
        </p>

        <p>
          <strong>Required skills:</strong>{" "}
          {opportunity.requiredSkills || "Any"}
        </p>
      </div>

      <h3 style={{ marginBottom: "0.75rem" }}>Applicants</h3>

      {applications.length === 0 ? (
        <p>No applications yet.</p>
      ) : (
        <div style={{ border: "1px solid #ddd", borderRadius: "8px" }}>
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "2fr 1.2fr 1fr",
              gap: "0.75rem",
              padding: "0.75rem 1rem",
              borderBottom: "1px solid #ddd",
              fontWeight: 600,
              background: "#fafafa",
              borderTopLeftRadius: "8px",
              borderTopRightRadius: "8px",
            }}
          >
            <div>Volunteer</div>
            <div>Applied at</div>
            <div>Status</div>
          </div>

          {applications.map((app) => (
            <div
              key={app.id}
              style={{
                display: "grid",
                gridTemplateColumns: "2fr 1.2fr 1fr",
                gap: "0.75rem",
                padding: "0.75rem 1rem",
                borderBottom: "1px solid #eee",
              }}
            >
              <div>{app.volunteer?.email ?? `User #${app.volunteer?.id}`}</div>
              <div>{new Date(app.applicationDate).toLocaleString()}</div>
              <div>
                <strong>{app.status}</strong>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Next story placeholder */}
      <p style={{ marginTop: "1rem", color: "#666" }}>
        Next: accept/reject applications.
      </p>
    </div>
  );
}