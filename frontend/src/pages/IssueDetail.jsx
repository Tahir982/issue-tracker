import { useEffect, useState, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getIssue, updateStatus, assignIssue, deleteIssue } from "../api/issues";
import { getComments, addComment, deleteComment } from "../api/comments";
import { getMembers } from "../api/projects";
import { useAuth } from "../context/AuthContext";
import StatusBadge   from "../components/StatusBadge";
import PriorityBadge from "../components/PriorityBadge";
import TypeBadge     from "../components/TypeBadge";
import Spinner       from "../components/Spinner";
import { ArrowLeft, Send, Trash2, UserCircle, Calendar, Layers } from "lucide-react";

const STATUSES   = ["OPEN","IN_PROGRESS","IN_REVIEW","RESOLVED","CLOSED"];
const STATUS_NEXT = { OPEN:"IN_PROGRESS", IN_PROGRESS:"IN_REVIEW", IN_REVIEW:"RESOLVED", RESOLVED:"CLOSED" };

export default function IssueDetail() {
  const { pid, id } = useParams();
  const { user } = useAuth();
  const nav = useNavigate();

  const [issue,    setIssue]    = useState(null);
  const [comments, setComments] = useState([]);
  const [members,  setMembers]  = useState([]);
  const [text,     setText]     = useState("");
  const [loading,  setLoading]  = useState(true);
  const [posting,  setPosting]  = useState(false);
  const bottomRef = useRef(null);

  useEffect(() => {
    Promise.all([getIssue(id), getComments(id), getMembers(pid)])
      .then(([is, cm, mb]) => { setIssue(is.data.data); setComments(cm.data.data); setMembers(mb.data.data); })
      .finally(() => setLoading(false));
  }, [id, pid]);

  useEffect(() => { bottomRef.current?.scrollIntoView({ behavior:"smooth" }); }, [comments]);

  const handleStatusChange = async s => {
    const r = await updateStatus(id, s); setIssue(r.data.data);
  };
  const handleAssign = async e => {
    const r = await assignIssue(id, e.target.value ? Number(e.target.value) : null); setIssue(r.data.data);
  };
  const handleComment = async e => {
    e.preventDefault(); if (!text.trim()) return;
    setPosting(true);
    const r = await addComment(id, { content: text }); setComments(c => [...c, r.data.data]); setText(""); setPosting(false);
  };
  const handleDeleteComment = async cid => {
    await deleteComment(cid); setComments(c => c.filter(x => x.id !== cid));
  };
  const handleDeleteIssue = async () => {
    if (!confirm("Delete this issue?")) return;
    await deleteIssue(id); nav(`/projects/${pid}`);
  };

  if (loading) return <div className="flex justify-center py-20"><Spinner size="lg" /></div>;
  if (!issue) return <p className="text-slate-500 text-center py-20">Issue not found.</p>;

  const nextStatus = STATUS_NEXT[issue.status];
  const fmt = d => d ? new Date(d).toLocaleDateString("en-GB", { day:"2-digit", month:"short", year:"numeric" }) : "—";

  return (
    <div className="max-w-5xl mx-auto space-y-5">
      {/* Back */}
      <button onClick={() => nav(`/projects/${pid}`)} className="flex items-center gap-2 text-sm text-slate-500 hover:text-slate-300 transition-colors">
        <ArrowLeft size={15} /> Back to project
      </button>

      <div className="grid grid-cols-1 xl:grid-cols-[1fr_280px] gap-5">
        {/* Main panel */}
        <div className="space-y-4">
          <div className="glass p-6">
            <div className="flex items-center gap-2 mb-3">
              <span className="font-mono text-xs text-slate-500">{issue.issueKey}</span>
              <TypeBadge type={issue.type} />
            </div>
            <h1 className="text-xl font-bold text-slate-100 mb-4">{issue.title}</h1>
            <div className="flex flex-wrap gap-2 mb-5">
              <StatusBadge status={issue.status} />
              <PriorityBadge priority={issue.priority} />
            </div>
            {issue.description && (
              <div className="bg-slate-900/50 rounded-xl p-4 text-sm text-slate-300 leading-relaxed whitespace-pre-wrap border border-white/[.04]">
                {issue.description}
              </div>
            )}
            {nextStatus && (
              <button onClick={() => handleStatusChange(nextStatus)}
                className="mt-4 btn-primary text-xs py-2">
                → Move to {nextStatus.replace("_", " ")}
              </button>
            )}
          </div>

          {/* Comments */}
          <div className="glass p-5">
            <h3 className="text-sm font-semibold text-slate-300 mb-4">Comments ({comments.length})</h3>
            <div className="space-y-3 max-h-96 overflow-y-auto mb-4 pr-1">
              {comments.length === 0 && <p className="text-sm text-slate-600 text-center py-6">No comments yet. Be the first!</p>}
              {comments.map(c => (
                <div key={c.id} className="flex gap-3">
                  <div className="w-7 h-7 rounded-full bg-gradient-to-br from-brand-500 to-brand-700 flex items-center justify-center text-xs font-bold text-white flex-shrink-0 mt-0.5">
                    {c.author.fullName.charAt(0)}
                  </div>
                  <div className="flex-1 bg-slate-900/50 border border-white/[.05] rounded-xl p-3">
                    <div className="flex items-center justify-between mb-1">
                      <span className="text-xs font-semibold text-slate-300">{c.author.fullName}</span>
                      <div className="flex items-center gap-2">
                        {c.edited && <span className="text-xs text-slate-600 italic">edited</span>}
                        <span className="text-xs text-slate-600">{fmt(c.createdAt)}</span>
                        {(user.id === c.author.id || user.role === "ADMIN") && (
                          <button onClick={() => handleDeleteComment(c.id)} className="text-slate-600 hover:text-red-400 transition-colors"><Trash2 size={12} /></button>
                        )}
                      </div>
                    </div>
                    <p className="text-sm text-slate-300 leading-relaxed">{c.content}</p>
                  </div>
                </div>
              ))}
              <div ref={bottomRef} />
            </div>
            <form onSubmit={handleComment} className="flex gap-2">
              <input value={text} onChange={e => setText(e.target.value)} className="input-field flex-1" placeholder="Add a comment…" />
              <button type="submit" disabled={posting || !text.trim()} className="btn-primary px-4">
                {posting ? "…" : <Send size={15} />}
              </button>
            </form>
          </div>
        </div>

        {/* Sidebar */}
        <div className="space-y-3">
          <div className="glass p-4 space-y-4">
            <h3 className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Details</h3>

            <div>
              <label className="block text-xs text-slate-500 mb-1.5">Status</label>
              <select value={issue.status} onChange={e => handleStatusChange(e.target.value)} className="input-field text-xs py-2">
                {STATUSES.map(s => <option key={s}>{s}</option>)}
              </select>
            </div>

            <div>
              <label className="block text-xs text-slate-500 mb-1.5">Assignee</label>
              <select value={issue.assignee?.id || ""} onChange={handleAssign} className="input-field text-xs py-2">
                <option value="">Unassigned</option>
                {members.map(m => <option key={m.id} value={m.id}>{m.fullName}</option>)}
              </select>
            </div>

            <div className="grid grid-cols-2 gap-3 pt-1 border-t border-white/[.05]">
              {[
                { icon: UserCircle, label:"Reporter",     val: issue.reporter?.fullName },
                { icon: Calendar,   label:"Due Date",     val: issue.dueDate ? new Date(issue.dueDate).toLocaleDateString("en-GB") : "—" },
                { icon: Layers,     label:"Story Points", val: issue.storyPoints ?? "—" },
                { icon: Calendar,   label:"Created",      val: fmt(issue.createdAt) },
              ].map(({ icon: Icon, label, val }) => (
                <div key={label}>
                  <p className="text-xs text-slate-600 flex items-center gap-1 mb-0.5"><Icon size={10} />{label}</p>
                  <p className="text-xs text-slate-300 font-medium truncate">{val}</p>
                </div>
              ))}
            </div>
          </div>

          <button onClick={handleDeleteIssue} className="w-full py-2 rounded-xl text-xs text-red-400 border border-red-500/20 hover:bg-red-500/10 transition-colors font-medium">
            Delete Issue
          </button>
        </div>
      </div>
    </div>
  );
}
