import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { register as registerApi } from "../api/auth";
import { useAuth } from "../context/AuthContext";
import { Zap } from "lucide-react";

export default function Register() {
  const [form, setForm] = useState({ username:"", email:"", password:"", fullName:"" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const nav = useNavigate();

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));
  const handleSubmit = async e => {
    e.preventDefault(); setLoading(true); setError("");
    try {
      const r = await registerApi(form);
      login(r.data.data.token, r.data.data.user);
      nav("/");
    } catch(err) {
      const d = err.response?.data;
      setError(d?.data ? Object.values(d.data).join(", ") : d?.message || "Registration failed");
    } finally { setLoading(false); }
  };

  return (
    <div className="min-h-screen bg-slate-950 flex items-center justify-center p-4">
      <div className="w-full max-w-sm">
        <div className="flex flex-col items-center mb-8">
          <div className="w-12 h-12 rounded-2xl bg-brand-600 flex items-center justify-center mb-4 shadow-lg shadow-brand-600/40">
            <Zap size={24} className="text-white" />
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Create account</h1>
          <p className="text-sm text-slate-500 mt-1">Join TrackIt today</p>
        </div>
        <div className="glass p-6">
          {error && <div className="mb-4 px-4 py-3 bg-red-500/10 border border-red-500/20 rounded-xl text-sm text-red-400">{error}</div>}
          <form onSubmit={handleSubmit} className="space-y-3">
            {[["fullName","Full Name","text","Jane Doe"],["username","Username","text","jane_doe"],["email","Email","email","jane@example.com"],["password","Password","password","Min. 6 characters"]].map(([name,label,type,ph]) => (
              <div key={name}>
                <label className="block text-xs font-semibold text-slate-400 mb-1.5">{label}</label>
                <input name={name} type={type} value={form[name]} onChange={handleChange} required className="input-field" placeholder={ph} />
              </div>
            ))}
            <button type="submit" disabled={loading} className="btn-primary w-full mt-1">
              {loading ? "Creating account…" : "Create account"}
            </button>
          </form>
          <p className="text-center text-sm text-slate-500 mt-4">
            Have an account? <Link to="/login" className="text-brand-400 hover:text-brand-300 transition-colors">Sign in</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
