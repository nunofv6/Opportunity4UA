import { useState } from "react";
import { Link } from "react-router-dom";
import { apiRequest } from "../api/api";

export default function Login({ onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  async function handleLogin() {
    try {
      const token = await apiRequest("/users/login", "POST", {
        email,
        password,
      });
      onLogin(token);
    } catch (err: any) {
      setError(err.message || "Login failed");
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Login</h2>

        <input
          placeholder="Email"
          onChange={e => setEmail(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          onChange={e => setPassword(e.target.value)}
        />

        <button onClick={handleLogin}>Login</button>

        {error && <div className="error">{error}</div>}

        <div className="auth-footer">
          <Link to="/register">Create an account</Link>
        </div>
      </div>
    </div>
  );
}
