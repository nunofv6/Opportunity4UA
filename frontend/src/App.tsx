import { useEffect, useState } from "react";
import { BrowserRouter, Routes, Route, Navigate, Link } from "react-router-dom";
import { apiRequest } from "./api/api";
import Login from "./components/Login";
import Register from "./components/Register";
import Opportunities from "./components/Opportunities";
import Profile from "./components/Profile";

function App() {
  const [token, setToken] = useState(() => {
    return localStorage.getItem("authToken");
  });
  const [user, setUser] = useState(null);

  useEffect(() => {
    if (token) {
      apiRequest("/users/me", "GET", null, token)
        .then(setUser)
        .catch(() => {
          localStorage.removeItem("authToken");
          setToken(null);
          setUser(null);
        });
    }
  }, [token]);

  function handleLogin(token) {
    localStorage.setItem("authToken", token);
    setToken(token);
  }

  function handleLogout() {
    localStorage.removeItem("authToken");
    setToken(null);
    setUser(null);
  }

  return (
    <BrowserRouter>
      {user && (
        <div style={{ padding: "1rem", background: "#eee" }}>
          {user.role === "VOLUNTEER" && (
            <>
              <Link to="/" style={{ marginRight: "1rem" }}>
                Opportunities
              </Link>
              <Link to="/profile">My Profile</Link>
            </>
          )}
          {user.role === "PROMOTER" && (
            <>
              <Link to="/my-opportunities">
                My Opportunities
              </Link>
            </>
          )}
          <button onClick={handleLogout}>Logout</button>
        </div>
      )}
      <Routes>
        {!token ? (
          <>
            <Route path="/login" element={<Login onLogin={handleLogin} />} />
            <Route path="/register" element={<Register />} />
            <Route path="*" element={<Navigate to="/login" />} />
          </>
        ) : (
          <>
            <Route
              path="/"
              element={<Opportunities token={token} />}
            />
            <Route path="/profile" element={<Profile token={token} />} />
            <Route path="*" element={<Navigate to="/" />} />
          </>
        )}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
