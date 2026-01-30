import { Link } from "react-router-dom";

function Navbar({ user, onLogout } : { user: { role: string } | null; onLogout: () => void }) {
  if (!user) return null;

  return (
    <nav style={styles.nav}>
      <div>
        {user.role === "VOLUNTEER" && (
          <>
            <Link data-testid="nav-opportunities" to="/opportunities" style={styles.link}>Opportunities</Link>
            <Link data-testid="nav-my-applications" to="/my-applications" style={styles.link}>My Applications</Link>
            <Link data-testid="nav-my-profile" to="/volunteerprofile" style={styles.link}>My Profile</Link>
            <Link data-testid="nav-my-rewards" to="/my-rewards" style={styles.link}>My Rewards</Link>
            <Link data-testid="nav-rewards-shop" to="/shop">Rewards Shop</Link>
          </>
        )}

        {user.role === "PROMOTER" && (
          <>
            <Link data-testid="nav-my-opportunities" to="/my-opportunities" style={{ marginRight: "1rem" }}>My Opportunities</Link>
            <Link data-testid="nav-my-profile" to="/promoterprofile" style={styles.link}>My Profile</Link>
            <Link data-testid="nav-create-opportunity" to="/opportunities/create">Create Opportunity</Link>
          </>
        )}
      </div>

      <button data-testid="nav-logout" onClick={onLogout} style={styles.button}>
        Logout
      </button>
    </nav>
  );
}

const styles = {
  nav: {
    padding: "1rem",
    background: "#eee",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  link: {
    marginRight: "1rem",
    textDecoration: "none",
    fontWeight: "bold",
  },
  button: {
    cursor: "pointer",
  },
};

export default Navbar;
