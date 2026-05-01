import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { LogOut, Zap } from "lucide-react";

const ROLE_COLOR = { ADMIN:"bg-red-500/20 text-red-400", PROJECT_MANAGER:"bg-amber-500/20 text-amber-400", DEVELOPER:"bg-brand-500/20 text-brand-400", REPORTER:"bg-emerald-500/20 text-emerald-400" };

export default function Navbar() {
  const { user, logout } = useAuth();
  const nav = useNavigate();
  const handleLogout = () => { logout(); nav("/login"); };
  const initials = user?.fullName?.split(" ").map(n => n[0]).join("").slice(0, 2).toUpperCase();

  return (
    <header className="h-14 border-b border-white/[.06] bg-slate-950/80 backdrop-blur-md flex items-center justify-between px-6 flex-shrink-0">
      <div className="flex items-center gap-2">
        <div className="w-7 h-7 rounded-lg bg-brand-600 flex items-center justify-center">
          <Zap size={14} className="text-white" />
        </div>
        <span className="font-semibold text-sm tracking-tight text-slate-100">TrackIt</span>
      </div>
      <div className="flex items-center gap-3">
        <span className={`badge ${ROLE_COLOR[user?.role] || "bg-slate-700 text-slate-300"}`}>{user?.role}</span>
        <div className="flex items-center gap-2 px-2 py-1.5 rounded-xl bg-white/[.04] border border-white/[.07]">
          <div className="w-7 h-7 rounded-lg bg-gradient-to-br from-brand-500 to-brand-700 flex items-center justify-center text-xs font-bold text-white">{initials}</div>
          <span className="text-sm text-slate-300 hidden sm:block">{user?.fullName}</span>
        </div>
        <button onClick={handleLogout} className="flex items-center gap-1.5 text-xs text-slate-500 hover:text-red-400 transition-colors px-2 py-1.5 rounded-lg hover:bg-red-400/10">
          <LogOut size={14} /> <span className="hidden sm:block">Sign out</span>
        </button>
      </div>
    </header>
  );
}
