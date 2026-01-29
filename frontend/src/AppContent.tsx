import { useEffect, useState } from "react";
import { Routes, Route, Navigate, useNavigate, useLocation } from "react-router-dom";
import { apiRequest } from "./api/api.ts";
import Login from "./components/Login";
import Register from "./components/Register";
import Opportunities from "./components/Opportunities";
import VolunteerProfile from "./components/VolunteerProfile";
import Navbar from "./components/Navbar";
import PromoterProfile from "./components/PromoterProfile";
import CreateOpportunity from "./components/CreateOpportunity";
import PromoterOpportunities from "./components/PromoterOpportunities";
import VolunteerOpportunityDetails from "./components/VolunteerOpportunityDetails.tsx";
import PromoterOpportunityDetails from "./components/PromoterOpportunityDetails.tsx";
import VolunteerApplications from "./components/VolunteerApplications.tsx";
import Rewards from "./components/Rewards.tsx";
import Shop from "./components/Shop.tsx";

export function AppContent() {
  const navigate = useNavigate();
  const location = useLocation();

  const [token, setToken] = useState(() => {
    return localStorage.getItem("authToken");
  });
  const [user, setUser] = useState(null);

  useEffect(() => {
    if (token && !user) {
        apiRequest("/users/me", "GET", null, token)
        .then(u => {
            setUser(u);

            // 🔒 Redirect ONLY if coming from auth pages
            if (location.pathname === "/login" || location.pathname === "/register") {
            if (u.role === "VOLUNTEER") {
                navigate("/opportunities", { replace: true });
            } else if (u.role === "PROMOTER") {
                navigate("/my-opportunities", { replace: true });
            }
            }
        })
        .catch(() => {
            localStorage.removeItem("authToken");
            setToken(null);
            setUser(null);
            navigate("/login", { replace: true });
        });
    }
  }, [token, user, location.pathname, navigate]);

  function handleLogin(token : string) {
    localStorage.setItem("authToken", token);
    setToken(token);
  }

  function handleLogout() {
    localStorage.removeItem("authToken");
    setToken(null);
    setUser(null);
    navigate("/login", { replace: true });
  }

  return (
    <>
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
            <Route path="/opportunities" element={<Opportunities token={token} />} />
            <Route path="/opportunities/create" element={<CreateOpportunity token={token} />} />
            <Route path="/opportunities/:id" element={<VolunteerOpportunityDetails token={token} />} />

            <Route path="/volunteerprofile" element={<VolunteerProfile token={token} />} />
            <Route path="/promoterprofile" element={<PromoterProfile token={token} />} />
            <Route path="/my-opportunities" element={<PromoterOpportunities token={token} />} />
            <Route path="/my-opportunities/:id" element={<PromoterOpportunityDetails token={token} />} />
            <Route
              path="/my-applications"
              element={<VolunteerApplications token={token} />}
            />
            <Route path="/my-rewards" element={<Rewards token={token} />} />
            <Route
              path="/shop"
              element={<Shop token={token} />}
            />
            <Route path="*" element={<Navigate to="/opportunities" />} />
          </>
        )}
      </Routes>
    </>
  );
}