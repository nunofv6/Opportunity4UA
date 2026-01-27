import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { apiRequest } from "../api/api.ts";

export default function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("VOLUNTEER");
  const navigate = useNavigate();

  async function handleRegister() {
    await apiRequest("/users", "POST", {
      email,
      password,
      role,
    });
    navigate("/login");
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Register</h2>

        <input
          placeholder="Email"
          onChange={e => setEmail(e.target.value)}
        />
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

        <div className="auth-footer">
          <Link to="/login">Already have an account?</Link>
        </div>
      </div>
    </div>
  );
}