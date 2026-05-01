import { createContext, useContext, useState, useEffect } from "react";
import { getMe } from "../api/auth";

const Ctx = createContext(null);

export function AuthProvider({ children }) {
  const [user,    setUser   ] = useState(() => { try { return JSON.parse(localStorage.getItem("user")); } catch { return null; } });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (localStorage.getItem("token") && !user) {
      getMe().then(r => setUser(r.data.data))
             .catch(() => { localStorage.removeItem("token"); localStorage.removeItem("user"); })
             .finally(() => setLoading(false));
    } else { setLoading(false); }
  }, []);

  const login = (token, userData) => {
    localStorage.setItem("token", token);
    localStorage.setItem("user", JSON.stringify(userData));
    setUser(userData);
  };
  const logout = () => { localStorage.removeItem("token"); localStorage.removeItem("user"); setUser(null); };

  return <Ctx.Provider value={{ user, login, logout, loading }}>{children}</Ctx.Provider>;
}

export const useAuth = () => useContext(Ctx);
