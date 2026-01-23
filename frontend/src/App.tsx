import { useState } from "react";
import Login from "./components/Login";
import Register from "./components/Register";
import Opportunities from "./components/Opportunities";

function App() {
  const [token, setToken] = useState(null);

  return (
    <div style={{ padding: "20px" }}>
      <h1>Opportunity4UA - Test UI</h1>

      {!token ? (
        <>
          <Register />
          <hr />
          <Login onLogin={setToken} />
        </>
      ) : (
        <>
          <p>Authenticated</p>
          <Opportunities token={token} />
        </>
      )}
    </div>
  );
}

export default App;
