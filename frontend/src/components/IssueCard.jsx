import { useNavigate } from "react-router-dom";
import StatusBadge   from "./StatusBadge";
import PriorityBadge from "./PriorityBadge";
import TypeBadge     from "./TypeBadge";
import { MessageSquare, UserCircle } from "lucide-react";

export default function IssueCard({ issue, projectId }) {
  const nav = useNavigate();
  return (
    <div onClick={() => nav(`/projects/${projectId}/issues/${issue.id}`)}
      className="glass p-4 cursor-pointer hover:border-brand-500/30 hover:bg-white/[.06] transition-all group">
      <div className="flex items-center justify-between gap-2 mb-2.5">
        <span className="font-mono text-xs text-slate-500 group-hover:text-brand-400 transition-colors">{issue.issueKey}</span>
        <TypeBadge type={issue.type} />
      </div>
      <p className="text-sm font-medium text-slate-200 mb-3 line-clamp-2 leading-snug">{issue.title}</p>
      <div className="flex items-center justify-between">
        <div className="flex gap-1.5 flex-wrap">
          <StatusBadge   status={issue.status} />
          <PriorityBadge priority={issue.priority} />
        </div>
        <div className="flex items-center gap-3 text-xs text-slate-500">
          <span className="flex items-center gap-1"><UserCircle size={12} />{issue.assignee?.fullName?.split(" ")[0] ?? "—"}</span>
          <span className="flex items-center gap-1"><MessageSquare size={12} />{issue.commentCount}</span>
        </div>
      </div>
    </div>
  );
}
