import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getProject, getMembers, addMember } from "../api/projects";
import { getIssues }  from "../api/issues";
import { getUsers }   from "../api/auth";
import IssueCard  from "../components/IssueCard";
import IssueForm  from "../components/IssueForm";
import Spinner    from "../components/Spinner";
import StatusBadge from "../components/StatusBadge";
import { Plus, Users, AlertCircle, Clock, CheckCircle2, Filter } from "lucide-react";

const STATUSES = ["ALL","OPEN","IN_PROGRESS","IN_REVIEW","RESOLVED","CLOSED"];

export default function ProjectDetail() {
  const { id } = useParams();
  const [project, setProject]     = useState(null);
  const [issues,  setIssues]      = useState([]);
  const [members, setMembers]     = useState([]);
  const [allUsers, setAllUsers]   = useState([]);
  const [filter,  setFilter]      = useState("ALL");
  const [showForm, setShowForm]   = useState(false);
  const [loading,  setLoading]    = useState(true);

  useEffect(() => {
    Promise.all([getProject(id), getIssues(id), getMembers(id), getUsers()])
      .then(([p, is, ms, us]) => {
        setProject(p.data.data); setIssues(is.data.data);
        setMembers(ms.data.data); setAllUsers(us.data.data);
      }).finally(() => setLoading(false));
  }, [id]);

  const filtered = filter === "ALL" ? issues : issues.filter(i => i.status === filter);
  const counts   = { OPEN: issues.filter(i => i.status==="OPEN").length, IN_PROGRESS: issues.filter(i => i.status==="IN_PROGRESS").length, RESOLVED: issues.filter(i => i.status==="RESOLVED").length };

  if (loading) return <div className="flex justify-center py-20"><Spinner size="lg" /></div>;
  if (!project) return <p className="text-slate-500 text-center py-20">Project not found.</p>;

  return (
    <div className="max-w-6xl mx-auto space-y-5">
      {/* Header */}
      <div className="glass p-5 flex items-start justify-between gap-4">
        <div className="flex items-center gap-4">
          <div className="w-12 h-12 rounded-2xl bg-brand-500/15 flex items-center justify-center font-bold text-brand-400">
            {project.projectKey.slice(0,2)}
          </div>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-lg font-bold text-slate-100">{project.name}</h1>
              <span className="font-mono text-xs text-slate-500 bg-slate-800/80 px-2 py-0.5 rounded">{project.projectKey}</span>
            </div>
            <p className="text-sm text-slate-500 mt-0.5">{project.description || "No description"}</p>
          </div>
        </div>
        <div className="flex items-center gap-3 text-xs text-slate-500 flex-shrink-0">
          <span className="flex items-center gap-1.5"><AlertCircle size={13} className="text-sky-400" /> {counts.OPEN} open</span>
          <span className="flex items-center gap-1.5"><Clock size={13} className="text-amber-400" /> {counts.IN_PROGRESS} in progress</span>
          <span className="flex items-center gap-1.5"><CheckCircle2 size={13} className="text-emerald-400" /> {counts.RESOLVED} resolved</span>
        </div>
      </div>

      {/* Members strip */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Users size={14} className="text-slate-500" />
          <span className="text-xs text-slate-500 font-medium">{members.length} member{members.length !== 1 ? "s" : ""}</span>
          <div className="flex -space-x-1.5 ml-1">
            {members.slice(0,6).map(m => (
              <div key={m.id} title={m.fullName}
                className="w-6 h-6 rounded-full bg-gradient-to-br from-brand-500 to-brand-700 border border-slate-900 flex items-center justify-center text-[9px] font-bold text-white flex-shrink-0">
                {m.fullName.charAt(0)}
              </div>
            ))}
          </div>
        </div>
        <button onClick={() => setShowForm(true)} className="btn-primary flex items-center gap-2 text-xs py-2 px-3">
          <Plus size={14} /> New Issue
        </button>
      </div>

      {/* Filter tabs */}
      <div className="flex gap-1 overflow-x-auto pb-1">
        {STATUSES.map(s => (
          <button key={s} onClick={() => setFilter(s)}
            className={`px-3 py-1.5 rounded-lg text-xs font-medium whitespace-nowrap transition-all ${
              filter === s ? "bg-brand-600 text-white" : "bg-white/[.04] text-slate-400 hover:bg-white/[.08] hover:text-slate-200 border border-white/[.06]"
            }`}>
            {s === "ALL" ? `All (${issues.length})` : s.replace("_", " ")}
          </button>
        ))}
      </div>

      {/* Issues grid */}
      {filtered.length === 0 ? (
        <div className="glass p-12 text-center">
          <Filter size={36} className="mx-auto text-slate-700 mb-3" />
          <p className="text-slate-400 font-medium">No issues found</p>
          <p className="text-sm text-slate-600 mt-1">{filter === "ALL" ? "Create your first issue" : `No issues with status ${filter}`}</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-3">
          {filtered.map(i => <IssueCard key={i.id} issue={i} projectId={id} />)}
        </div>
      )}

      {showForm && (
        <IssueForm projectId={id} members={members} onClose={() => setShowForm(false)}
          onCreated={newIssue => setIssues(p => [newIssue, ...p])} />
      )}
    </div>
  );
}
