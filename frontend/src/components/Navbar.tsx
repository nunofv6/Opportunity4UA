import { Link } from "react-router-dom";

function Navbar({ user, onLogout } : { user: { role: string } | null; onLogout: () => void }) {
  if (!user) return null;

  return (
    <nav style={styles.nav}>
      <div>
        {user.role === "VOLUNTEER" && (
          <>
            <Link to="/opportunities" style={styles.link}>Opportunities</Link>
            <Link to="/my-applications" style={styles.link}>My Applications</Link>
            <Link to="/volunteerprofile" style={styles.link}>My Profile</Link>
            <Link to="/my-rewards" style={styles.link}>My Rewards</Link>
            <Link to="/shop">Rewards Shop</Link>
          </>
        )}

        {user.role === "PROMOTER" && (
          <>
            <Link to="/my-opportunities" style={{ marginRight: "1rem" }}>My Opportunities</Link>
            <Link to="/promoterprofile" style={styles.link}>My Profile</Link>
            <Link to="/opportunities/create">Create Opportunity</Link>
          </>
        )}
      </div>

      <button onClick={onLogout} style={styles.button}>
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
