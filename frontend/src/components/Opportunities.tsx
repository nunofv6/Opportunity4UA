import { useEffect, useState } from "react";
import { apiRequest } from "../api/api";

export default function Opportunities({ token }) {
  const [opportunities, setOpportunities] = useState([]);

  useEffect(() => {
    async function load() {
      const data = await apiRequest("/opportunities", "GET", null, token);
      setOpportunities(data);
    }
    load();
  }, [token]);

  return (
    <div>
      <h2>Opportunities</h2>
      <ul>
        {opportunities.map(o => (
          <li key={o.id}>
            <strong>{o.title}</strong> — {o.points} points
          </li>
        ))}
      </ul>
    </div>
  );
}
