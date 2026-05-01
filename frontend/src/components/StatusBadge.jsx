const MAP = {
  OPEN:        "bg-sky-500/15 text-sky-400 border border-sky-500/25",
  IN_PROGRESS: "bg-amber-500/15 text-amber-400 border border-amber-500/25",
  IN_REVIEW:   "bg-violet-500/15 text-violet-400 border border-violet-500/25",
  RESOLVED:    "bg-emerald-500/15 text-emerald-400 border border-emerald-500/25",
  CLOSED:      "bg-slate-500/15 text-slate-400 border border-slate-500/25",
};
export default function StatusBadge({ status }) {
  return <span className={`badge ${MAP[status] || "bg-slate-700 text-slate-400"}`}>{status?.replace("_", " ")}</span>;
}
