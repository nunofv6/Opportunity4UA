import { useEffect, useState } from "react";
import { apiRequest } from "../api/api";
import type { RewardItem } from "../types/types";

export default function Shop({ token }: { token: string }) {
  const [items, setItems] = useState<RewardItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [points, setPoints] = useState(0);

  useEffect(() => {
    async function load() {
      try {
        const [rewards, user] = await Promise.all([
            apiRequest<RewardItem[]>("/rewards", "GET", null, token),
            apiRequest<{ pointBalance: number }>("/users/me", "GET", null, token)
        ]);
        setItems(rewards);
        setPoints(user.pointBalance);
        } catch (err: any) {
        setError(err.message || "Failed to load data");
        } finally {
        setLoading(false);
        }
    }
    load();
  }, [token]);

  async function handleRedeem(item: RewardItem) {
    setMessage("");
    setError("");

    try {
        await apiRequest(`/rewards/${item.id}/redeem`, "POST", null, token);

        setMessage("Reward redeemed successfully 🎉");

        setItems(prev => prev.filter(i => i.id !== item.id));
        setPoints(prev => (prev !== null ? prev - item.costPoints : prev));
    } catch (err: any) {
        setError("Failed to redeem reward");
    }
  }

  if (loading) return <p style={{ textAlign: "center" }}>Loading rewards…</p>;
  if (error) return <p data-testid="reward-error" style={{ textAlign: "center", color: "red" }}>{error}</p>;

  return (
    <div style={{ maxWidth: "1000px", margin: "2rem auto" }}>
      <div
        data-testid="point-balance"
        style={{
            position: "absolute",
            top: "5rem",
            right: "1.5rem",
            background: "#111",
            color: "white",
            padding: "0.5rem 0.9rem",
            borderRadius: "999px",
            fontWeight: 600,
            fontSize: "0.9rem"
        }}
      >
        {points !== null ? `${points} pts` : "—"}
      </div>
      <h2 style={{ marginBottom: "1rem" }}>Rewards Shop</h2>

      {message && (
        <p data-testid="reward-success" style={{ color: "green", marginBottom: "1rem" }}>{message}</p>
      )}

      {items.length === 0 ? (
        <p>No rewards available at the moment.</p>
      ) : (
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fill, minmax(240px, 1fr))",
            gap: "1rem"
          }}
        >
          {items.map(item => (
            <div
              key={item.id}
              style={{
                border: "1px solid #ddd",
                borderRadius: "8px",
                padding: "1rem",
                backgroundColor: "#fafafa",
                display: "flex",
                flexDirection: "column",
                justifyContent: "space-between"
              }}
            >
              <div>
                <h3 style={{ marginTop: 0 }}>{item.name}</h3>
                <p style={{ color: "#555", fontSize: "0.9rem" }}>
                  {item.description}
                </p>
              </div>

              <div>
                <p style={{ fontWeight: 600 }}>
                  Cost: {item.costPoints} pts
                </p>

                <button
                  data-testid={`nav-reward-${item.id}-redeem`}
                  onClick={() => handleRedeem(item)}
                  style={{ width: "100%" }}
                >
                  Redeem
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}