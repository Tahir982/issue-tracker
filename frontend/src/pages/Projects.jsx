import { useEffect, useState } from "react";
import { getProjects, createProject } from "../api/projects";
import { useNavigate } from "react-router-dom";
import Spinner from "../components/Spinner";
import Modal   from "../components/Modal";
import { Plus, FolderKanban, ArrowRight, Users, AlertCircle } from "lucide-react";

export default function Projects() {
  const [projects, setProjects] = useState([]);
  const [loading,  setLoading]  = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm]         = useState({ name:"", description:"", projectKey:"" });
  const [saving, setSaving]     = useState(false);
  const [error, setError]       = useState("");
  const nav = useNavigate();

  useEffect(() => { getProjects().then(r => setProjects(r.data.data)).finally(() => setLoading(false)); }, []);

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));
  const handleCreate = async e => {
    e.preventDefault(); setSaving(true); setError("");
    try {
      const r = await createProject(form);
      setProjects(p => [r.data.data, ...p]);
      setShowForm(false); setForm({ name:"", description:"", projectKey:"" });
    } catch(err) { setError(err.response?.data?.message || "Failed to create project"); }
    finally { setSaving(false); }
  };

  if (loading) return <div className="flex justify-center py-20"><Spinner size="lg" /></div>;

  return (
    <div className="max-w-5xl mx-auto space-y-5">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-xl font-bold text-slate-100">Projects</h1>
          <p className="text-sm text-slate-500 mt-0.5">{projects.length} project{projects.length !== 1 ? "s" : ""}</p>
        </div>
        <button onClick={() => setShowForm(true)} className="btn-primary flex items-center gap-2"><Plus size={16} /> New Project</button>
      </div>

      {projects.length === 0 ? (
        <div className="glass p-12 text-center">
          <FolderKanban size={40} className="mx-auto text-slate-600 mb-3" />
          <p className="text-slate-400 font-medium">No projects yet</p>
          <p className="text-sm text-slate-600 mt-1">Create your first project to get started</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-3">
          {projects.map(p => (
            <div key={p.id} onClick={() => nav(`/projects/${p.id}`)}
              className="glass p-5 cursor-pointer hover:border-brand-500/30 hover:bg-white/[.06] transition-all group">
              <div className="flex items-start justify-between mb-3">
                <div className="w-9 h-9 rounded-xl bg-brand-500/15 flex items-center justify-center font-bold text-brand-400 text-sm">
                  {p.projectKey.slice(0,2)}
                </div>
                <span className="text-xs font-mono text-slate-500 bg-slate-800/60 px-2 py-0.5 rounded-md">{p.projectKey}</span>
              </div>
              <h3 className="font-semibold text-slate-200 mb-1 group-hover:text-brand-300 transition-colors">{p.name}</h3>
              <p className="text-xs text-slate-500 mb-4 line-clamp-2 min-h-[2rem]">{p.description || "No description provided"}</p>
              <div className="flex items-center justify-between text-xs text-slate-500">
                <div className="flex items-center gap-3">
                  <span className="flex items-center gap-1"><AlertCircle size={11} className="text-sky-500" />{p.openIssues} open</span>
                  <span className="flex items-center gap-1"><Users size={11} />{p.members?.length ?? 0}</span>
                </div>
                <ArrowRight size={14} className="text-slate-600 group-hover:text-brand-400 transition-colors" />
              </div>
            </div>
          ))}
        </div>
      )}

      {showForm && (
        <Modal title="Create New Project" onClose={() => setShowForm(false)}>
          {error && <div className="mb-4 px-4 py-3 bg-red-500/10 border border-red-500/20 rounded-xl text-sm text-red-400">{error}</div>}
          <form onSubmit={handleCreate} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-slate-400 mb-1.5">Project Name *</label>
              <input name="name" value={form.name} onChange={handleChange} required className="input-field" placeholder="Issue Tracker System" />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-400 mb-1.5">Project Key *</label>
              <input name="projectKey" value={form.projectKey} onChange={handleChange} required className="input-field" placeholder="ITS (uppercase, no spaces)" />
              <p className="text-xs text-slate-600 mt-1">Used as prefix for issues, e.g. ITS-1</p>
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-400 mb-1.5">Description</label>
              <textarea name="description" value={form.description} onChange={handleChange} rows={3} className="input-field resize-none" placeholder="What is this project about?" />
            </div>
            <div className="flex justify-end gap-2 pt-1">
              <button type="button" onClick={() => setShowForm(false)} className="btn-ghost">Cancel</button>
              <button type="submit" disabled={saving} className="btn-primary">{saving ? "Creating…" : "Create Project"}</button>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
