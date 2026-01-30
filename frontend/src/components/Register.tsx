import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { apiRequest } from "../api/api.ts";

export default function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("VOLUNTEER");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  async function handleRegister() {
    try {
      await apiRequest("/users", "POST", {
        email,
        password,
        role,
      });
      navigate("/login");
    } catch (err: any) {
      setError(err.message);
    }
  }
  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Register</h2>

        <input
          placeholder="Email"
          data-testid="register-email"
          onChange={e => setEmail(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          data-testid="register-password"
          onChange={e => setPassword(e.target.value)}
        />

        <select data-testid="register-role" onChange={e => setRole(e.target.value)}>
          <option value="VOLUNTEER">Volunteer</option>
          <option value="PROMOTER">Promoter</option>
        </select>

        <button data-testid="register-submit" onClick={handleRegister}>Register</button>

        {error && <div data-testid="register-error" className="error">{error}</div>}

        <div className="auth-footer">
          <Link to="/login">Already have an account?</Link>
        </div>
      </div>
    </div>
  );
}