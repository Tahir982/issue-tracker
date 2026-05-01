const MAP = {
  LOW:      { cls: "bg-slate-500/15 text-slate-400 border border-slate-500/25", icon: "↓" },
  MEDIUM:   { cls: "bg-amber-500/15 text-amber-400 border border-amber-500/25",  icon: "→" },
  HIGH:     { cls: "bg-orange-500/15 text-orange-400 border border-orange-500/25", icon: "↑" },
  CRITICAL: { cls: "bg-red-500/15 text-red-400 border border-red-500/25",        icon: "⚑" },
};
export default function PriorityBadge({ priority }) {
  const { cls, icon } = MAP[priority] || {};
  return <span className={`badge ${cls}`}>{icon} {priority}</span>;
}
