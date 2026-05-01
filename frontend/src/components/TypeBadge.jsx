const MAP = {
  BUG:         "bg-red-500/10 text-red-400",
  TASK:        "bg-brand-500/10 text-brand-400",
  FEATURE:     "bg-emerald-500/10 text-emerald-400",
  IMPROVEMENT: "bg-teal-500/10 text-teal-400",
  EPIC:        "bg-purple-500/10 text-purple-400",
};
const ICONS = { BUG:"🐛", TASK:"✓", FEATURE:"✦", IMPROVEMENT:"↑", EPIC:"⚡" };
export default function TypeBadge({ type }) {
  return <span className={`badge ${MAP[type] || "bg-slate-700 text-slate-400"}`}>{ICONS[type]} {type}</span>;
}
