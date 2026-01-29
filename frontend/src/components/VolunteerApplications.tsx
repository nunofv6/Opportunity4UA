import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { apiRequest } from "../api/api";
import type { CreateApplication } from "../types/types";

export default function VolunteerApplications({ token }: { token: string }) {
  const [applications, setApplications] = useState<CreateApplication[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function load() {
      try {
        const data = await apiRequest<CreateApplication[]>(
          "/application/me",
          "GET",
          null,
          token
        );
        console.log("RAW applications response:", data);
        setApplications(data);
      } catch (err: any) {
        setError(err?.message || "Failed to load applications");
      } finally {
        setLoading(false);
      }
    }
    load();
  }, [token]);

  if (loading) return <p style={{ textAlign: "center" }}>Loading...</p>;
  if (error) return <p style={{ textAlign: "center", color: "red" }}>{error}</p>;

  return (
    <div style={{ maxWidth: "900px", margin: "2rem auto" }}>
      <h2>My Applications</h2>

      {applications.length === 0 ? (
        <p>You haven't applied to any opportunities yet.</p>
      ) : (
        <div style={{ border: "1px solid #ddd", borderRadius: "8px" }}>
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "2fr 1.5fr 1fr",
              gap: "0.75rem",
              padding: "0.75rem 1rem",
              borderBottom: "1px solid #ddd",
              fontWeight: 600,
              background: "#fafafa",
            }}
          >
            <div>Opportunity</div>
            <div>Applied at</div>
            <div>Status</div>
          </div>

          {applications.map(app => (
            <div
              key={app.id}
              style={{
                display: "grid",
                gridTemplateColumns: "2fr 1.5fr 1fr",
                gap: "0.75rem",
                padding: "0.75rem 1rem",
                borderBottom: "1px solid #eee",
              }}
            >
              <div>
                <Link to={`/opportunities/${app.opportunityId}`}>
                  {app.opportunityTitle}
                </Link>
              </div>
              <div>{new Date(app.appliedAt).toLocaleString()}</div>
              <div>
                <strong>{app.status}</strong>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}