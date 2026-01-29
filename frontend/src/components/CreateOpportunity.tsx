import { useState } from "react";
import { apiRequest } from "../api/api.ts";

export default function CreateOpportunity({ token } : { token: string }) {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [requiredSkills, setRequiredSkills] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [maxVolunteers, setMaxVolunteers] = useState(1);
  const [points, setPoints] = useState(10);
  const [message, setMessage] = useState("");

  async function handleCreate() {
    try {
      await apiRequest(
        "/opportunity",
        "POST",
        {
          title,
          description,
          requiredSkills,
          maxVolunteers,
          points,
          startDate,
          endDate
        },
        token
      );
    } catch (err: any) {
      const errorMessage =
        err?.response?.data?.message ||
        err?.response?.data?.error ||
        "Erro ao criar a oportunidade.";

      setMessage(errorMessage);
    }
    setMessage("Opportunity created successfully");
    setTitle("");
    setDescription("");
  }

  return (
    <div style={{ maxWidth: "600px", margin: "2rem auto" }}>
      <h2>Create Opportunity</h2>

      <label>Title</label>
      <input
        value={title}
        onChange={e => setTitle(e.target.value)}
        style={{ width: "100%", marginBottom: "1rem" }}
      />

      <label>Description</label>
      <textarea
        value={description}
        onChange={e => setDescription(e.target.value)}
        rows={4}
        style={{ width: "100%", marginBottom: "1rem" }}
      />

      <label>Required Skills</label>
      <textarea
        value={requiredSkills}
        onChange={e => setRequiredSkills(e.target.value)}
        rows={4}
        style={{ width: "100%", marginBottom: "1rem" }}
      />

      <label>Start Date & Time</label>
      <input
        type="datetime-local"
        value={startDate}
        onChange={e => setStartDate(e.target.value)}
        style={{ width: "100%", marginBottom: "1rem" }}
      />

      <label>End Date & Time</label>
        <input
        type="datetime-local"
        value={endDate}
        onChange={e => setEndDate(e.target.value)}
        style={{ width: "100%", marginBottom: "1rem" }}
      />

      <label>Max Volunteers</label>
      <input
        type="number"
        min={1}
        value={maxVolunteers}
        onChange={e => setMaxVolunteers(Number(e.target.value))}
        style={{ width: "100%", marginBottom: "1rem" }}
      />

      <label>Points</label>
      <input
        type="number"
        min={1}
        value={points}
        onChange={e => setPoints(Number(e.target.value))}
        style={{ width: "100%", marginBottom: "1rem" }}
      />

      <button onClick={handleCreate}>Create</button>

      {message && <p style={{ color: "green" }}>{message}</p>}
    </div>
  );
}