import { useState } from "react";
import { apiRequest } from "../api/api";

export default function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("VOLUNTEER");

  async function handleRegister() {
    await apiRequest("/users", "POST", {
      email,
      password,
      role,
    });
    alert("User registered");
  }

  return (
    <div>
      <h2>Register</h2>
      <input placeholder="Email" onChange={e => setEmail(e.target.value)} />
      <input
        type="password"
        placeholder="Password"
        onChange={e => setPassword(e.target.value)}
      />
      <select onChange={e => setRole(e.target.value)}>
        <option value="VOLUNTEER">Volunteer</option>
        <option value="PROMOTER">Promoter</option>
      </select>
      <button onClick={handleRegister}>Register</button>
    </div>
  );
}