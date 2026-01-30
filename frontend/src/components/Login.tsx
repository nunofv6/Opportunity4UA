import { useState } from "react";
import { Link } from "react-router-dom";
import { apiRequest } from "../api/api.ts";

export default function Login({ onLogin }: { onLogin: (token: string) => void }) {
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
      setError(err.message);
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Login</h2>

        <input
          data-testid="login-email"
          placeholder="Email"
          onChange={e => setEmail(e.target.value)}
        />
        <input
          type="password"
          data-testid="login-password"
          placeholder="Password"
          onChange={e => setPassword(e.target.value)}
        />

        <button data-testid="login-submit" onClick={handleLogin}>Login</button>

        {error && <div data-testid="login-error" className="error">{error}</div>}

        <div className="auth-footer">
          <Link to="/register">Create an account</Link>
        </div>
      </div>
    </div>
  );
}
