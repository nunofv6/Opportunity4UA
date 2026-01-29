import { useEffect, useState } from "react";
import { apiRequest } from "../api/api";
import type { RewardItem } from "../types/types";

export default function Shop({ token }: { token: string }) {
  const [items, setItems] = useState<RewardItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    async function load() {
      try {
        const data = await apiRequest<RewardItem[]>(
          "/rewards",
          "GET",
          null,
          token
        );
        setItems(data);
      } catch (err: any) {
        setError(err?.message || "Failed to load rewards");
      } finally {
        setLoading(false);
      }
    }
    load();
  }, [token]);

  async function handleRedeem(id: number) {
    setMessage("");
    try {
      await apiRequest(
        `/rewards/${id}/redeem`,
        "POST",
        null,
        token
      );

      setMessage("Reward redeemed successfully 🎉");

      // opcional: remover da lista (se não quiseres resgates repetidos)
      setItems(prev => prev.filter(item => item.id !== id));
    } catch (err: any) {
      setError(err?.message || "Failed to redeem reward");
    }
  }

  if (loading) return <p style={{ textAlign: "center" }}>Loading rewards…</p>;
  if (error) return <p style={{ textAlign: "center", color: "red" }}>{error}</p>;

  return (
    <div style={{ maxWidth: "1000px", margin: "2rem auto" }}>
      <h2 style={{ marginBottom: "1rem" }}>Rewards Shop</h2>

      {message && (
        <p style={{ color: "green", marginBottom: "1rem" }}>{message}</p>
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
                  onClick={() => handleRedeem(item.id)}
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