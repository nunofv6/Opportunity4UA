import { useEffect, useState } from "react";
import { BrowserRouter, Routes, Route, Navigate, Link } from "react-router-dom";
import { apiRequest } from "./api/api";
import Login from "./components/Login";
import Register from "./components/Register";
import Opportunities from "./components/Opportunities";
import VolunteerProfile from "./components/VolunteerProfile";
import Navbar from "./components/Navbar";
import PromoterProfile from "./components/PromoterProfile";
import CreateOpportunity from "./components/CreateOpportunity";

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
      <Navbar user={user} onLogout={handleLogout} />
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
              path="/opportunities"
              element={<Opportunities token={token} />}
            />
            <Route
              path="/opportunities/create"
              element={<CreateOpportunity token={token} />}
            />
            <Route path="/volunteerprofile" element={<VolunteerProfile token={token} />} />
            <Route path="/promoterprofile" element={<PromoterProfile token={token} />} />
            <Route path="*" element={<Navigate to="/opportunities" />} />
          </>
        )}
      </Routes>
    </BrowserRouter>
  );
}

export default App;