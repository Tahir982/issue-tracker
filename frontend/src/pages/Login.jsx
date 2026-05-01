import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { login as loginApi } from "../api/auth";
import { useAuth } from "../context/AuthContext";
import { Zap } from "lucide-react";

export default function Login() {
  const [form, setForm] = useState({ username:"", password:"" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const nav = useNavigate();

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));
  const handleSubmit = async e => {
    e.preventDefault(); setLoading(true); setError("");
    try {
      const r = await loginApi(form);
      login(r.data.data.token, r.data.data.user);
      nav("/");
    } catch(err) { setError(err.response?.data?.message || "Invalid credentials"); }
    finally { setLoading(false); }
  };

  return (
    <div className="min-h-screen bg-slate-950 flex items-center justify-center p-4">
      <div className="w-full max-w-sm">
        <div className="flex flex-col items-center mb-8">
          <div className="w-12 h-12 rounded-2xl bg-brand-600 flex items-center justify-center mb-4 shadow-lg shadow-brand-600/40">
            <Zap size={24} className="text-white" />
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Welcome back</h1>
          <p className="text-sm text-slate-500 mt-1">Sign in to TrackIt</p>
        </div>
        <div className="glass p-6">
          {error && <div className="mb-4 px-4 py-3 bg-red-500/10 border border-red-500/20 rounded-xl text-sm text-red-400">{error}</div>}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-slate-400 mb-1.5">Username</label>
              <input name="username" value={form.username} onChange={handleChange} required className="input-field" placeholder="your_username" />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-400 mb-1.5">Password</label>
              <input name="password" type="password" value={form.password} onChange={handleChange} required className="input-field" placeholder="••••••••" />
            </div>
            <button type="submit" disabled={loading} className="btn-primary w-full mt-2">
              {loading ? "Signing in…" : "Sign in"}
            </button>
          </form>
          <p className="text-center text-sm text-slate-500 mt-4">
            No account? <Link to="/register" className="text-brand-400 hover:text-brand-300 transition-colors">Create one</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
