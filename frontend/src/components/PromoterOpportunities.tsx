import { useEffect, useState } from "react";
import { apiRequest } from "../api/api.ts";
import type { Opportunity } from "../types/types";
import { Link } from "react-router-dom";

export default function PromoterOpportunities({ token }: { token: string }) {
  const [opportunities, setOpportunities] = useState<Opportunity[]>([]);

  useEffect(() => {
    async function load() {
      const data = await apiRequest(
        "/opportunity/me",
        "GET",
        null,
        token
      );
      setOpportunities(data);
    }
    load();
  }, [token]);

  async function handleClose(id: number) {
    await apiRequest(
      `/opportunity/${id}/close`,
      "PUT",
      null,
      token
    );

    setOpportunities(prev =>
      prev.map(o =>
        o.id === id ? { ...o, status: "CLOSED" } : o
      )
    );
  }

  return (
    <div style={{ maxWidth: "800px", margin: "2rem auto" }}>
      <h2>My Opportunities</h2>

      {opportunities.map(opportunity => (
        <div
          key={opportunity.id}
          style={{
            border: "1px solid #ddd",
            padding: "1rem",
            marginBottom: "1rem",
            borderRadius: "6px"
          }}
        >
          <h3>{opportunity.title}</h3>

          <p>
            Status: <strong>{opportunity.status}</strong>
          </p>

          <div style={{ display: "flex", gap: "1rem" }}>
            <Link to={`/my-opportunities/${opportunity.id}`}>
              View details
            </Link>

            {opportunity.status === "OPEN" && (
              <button onClick={() => handleClose(opportunity.id)}>
                Close opportunity
              </button>
            )}
          </div>
        </div>
      ))}
    </div>
  );
}