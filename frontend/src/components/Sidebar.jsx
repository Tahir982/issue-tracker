import { NavLink } from "react-router-dom";
import { LayoutDashboard, FolderKanban, Bug } from "lucide-react";

const links = [
  { to: "/",         label: "Dashboard", icon: LayoutDashboard },
  { to: "/projects", label: "Projects",  icon: FolderKanban },
];

export default function Sidebar() {
  return (
    <aside className="w-52 flex-shrink-0 bg-slate-950 border-r border-white/[.05] flex flex-col">
      <div className="h-14 border-b border-white/[.05] flex items-center px-4">
        <span className="text-xs font-semibold text-slate-500 uppercase tracking-widest">Menu</span>
      </div>
      <nav className="flex-1 p-3 space-y-0.5">
        {links.map(({ to, label, icon: Icon }) => (
          <NavLink key={to} to={to} end={to === "/"}
            className={({ isActive }) => `nav-link${isActive ? " active" : ""}`}>
            <Icon size={17} />{label}
          </NavLink>
        ))}
      </nav>
      <div className="p-4 border-t border-white/[.05]">
        <div className="flex items-center gap-2">
          <Bug size={14} className="text-slate-600" />
          <span className="text-xs text-slate-600">TrackIt v1.0 · PAF-IAST</span>
        </div>
      </div>
    </aside>
  );
}
