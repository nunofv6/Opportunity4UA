import { useEffect, useState } from "react";
import { apiRequest } from "../api/api";
import type { Reward } from "../types/types";

export default function Rewards({ token }: { token: string }) {
  const [rewards, setRewards] = useState<Reward[]>([]);

  useEffect(() => {
    apiRequest<Reward[]>("/rewards/me", "GET", null, token)
      .then(setRewards);
  }, [token]);

  return (
    <div style={{ maxWidth: "800px", margin: "2rem auto" }}>
      <h2>My Rewards</h2>

      {rewards.length === 0 ? (
        <p>No rewards yet.</p>
      ) : (
        rewards.map(r => (
          <div key={r.id} style={{ borderBottom: "1px solid #eee", padding: "0.75rem 0" }}>
            <p>
              <strong>{r.points} points</strong> — {r.opportunity.title}
            </p>
            <small>{new Date(r.awardedAt).toLocaleString()}</small>
          </div>
        ))
      )}
    </div>
  );
}