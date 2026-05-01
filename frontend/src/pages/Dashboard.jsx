import { useEffect, useState } from "react";
import { getStats } from "../api/dashboard";
import StatCard from "../components/StatCard";
import Spinner  from "../components/Spinner";
import { useAuth } from "../context/AuthContext";
import { FolderKanban, AlertCircle, Clock, CheckCircle2, Archive, User2, FileText, Flame, Bug, Zap } from "lucide-react";

const PRI_COLOR = { CRITICAL:"#f87171", HIGH:"#fb923c", MEDIUM:"#fbbf24", LOW:"#94a3b8" };
const TYPE_ICONS = { BUG:"🐛", TASK:"✓", FEATURE:"✦", IMPROVEMENT:"↑", EPIC:"⚡" };

export default function Dashboard() {
  const [stats, setStats]   = useState(null);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => { getStats().then(r => setStats(r.data.data)).finally(() => setLoading(false)); }, []);

  if (loading) return <div className="flex justify-center py-20"><Spinner size="lg" /></div>;
  if (!stats)  return <p className="text-slate-500 text-center py-20">Could not load stats.</p>;

  const TOP = [
    { label:"Total Projects",   value:stats.totalProjects,    icon:FolderKanban, color:"brand"   },
    { label:"Open Issues",      value:stats.openIssues,       icon:AlertCircle,  color:"red"     },
    { label:"In Progress",      value:stats.inProgressIssues, icon:Clock,        color:"amber"   },
    { label:"Resolved",         value:stats.resolvedIssues,   icon:CheckCircle2, color:"emerald" },
    { label:"Assigned to Me",   value:stats.myAssignedIssues, icon:User2,        color:"violet"  },
    { label:"Reported by Me",   value:stats.myReportedIssues, icon:FileText,     color:"slate"   },
  ];

  return (
    <div className="space-y-6 max-w-6xl mx-auto">
      <div>
        <h1 className="text-xl font-bold text-slate-100">Good to see you, {user?.fullName?.split(" ")[0]} 👋</h1>
        <p className="text-sm text-slate-500 mt-0.5">Here's your project overview.</p>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-3 xl:grid-cols-6 gap-3">
        {TOP.map(c => <StatCard key={c.label} {...c} />)}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {/* Issues by Priority */}
        <div className="glass p-5">
          <h2 className="text-sm font-semibold text-slate-300 mb-4 flex items-center gap-2"><Flame size={15} className="text-orange-400" /> Issues by Priority</h2>
          <div className="space-y-3">
            {stats.issuesByPriority && Object.entries(stats.issuesByPriority).map(([pri, cnt]) => {
              const pct = stats.totalIssues > 0 ? Math.round((cnt / stats.totalIssues) * 100) : 0;
              return (
                <div key={pri}>
                  <div className="flex justify-between text-xs mb-1.5">
                    <span className="text-slate-400 font-medium">{pri}</span>
                    <span className="text-slate-500 font-mono">{cnt}</span>
                  </div>
                  <div className="h-1.5 bg-slate-800 rounded-full overflow-hidden">
                    <div className="h-full rounded-full transition-all duration-700" style={{ width:`${pct}%`, backgroundColor: PRI_COLOR[pri] || "#94a3b8" }} />
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Issues by Type */}
        <div className="glass p-5">
          <h2 className="text-sm font-semibold text-slate-300 mb-4 flex items-center gap-2"><Bug size={15} className="text-red-400" /> Issues by Type</h2>
          <div className="grid grid-cols-2 gap-2">
            {stats.issuesByType && Object.entries(stats.issuesByType).map(([type, cnt]) => (
              <div key={type} className="bg-white/[.03] border border-white/[.06] rounded-xl p-3">
                <div className="text-base mb-1">{TYPE_ICONS[type] || "•"}</div>
                <div className="text-lg font-bold text-slate-200">{cnt}</div>
                <div className="text-xs text-slate-500">{type}</div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
