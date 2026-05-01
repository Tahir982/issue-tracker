import { useState } from "react";
import { createIssue } from "../api/issues";
import Modal from "./Modal";

const TYPES     = ["BUG","TASK","FEATURE","IMPROVEMENT","EPIC"];
const PRIORITIES = ["LOW","MEDIUM","HIGH","CRITICAL"];

export default function IssueForm({ projectId, members = [], onClose, onCreated }) {
  const [form, setForm]     = useState({ title:"", description:"", type:"BUG", priority:"MEDIUM", assigneeId:"", storyPoints:"" });
  const [loading, setLoading] = useState(false);
  const [error,   setError]   = useState("");

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));
  const handleSubmit = async e => {
    e.preventDefault(); setLoading(true); setError("");
    try {
      const payload = { ...form, assigneeId: form.assigneeId ? Number(form.assigneeId) : null, storyPoints: form.storyPoints ? Number(form.storyPoints) : null };
      const res = await createIssue(projectId, payload);
      onCreated(res.data.data); onClose();
    } catch(err) { setError(err.response?.data?.message || "Failed to create issue"); }
    finally { setLoading(false); }
  };

  return (
    <Modal title="Create New Issue" onClose={onClose}>
      {error && <div className="mb-4 px-4 py-3 bg-red-500/10 border border-red-500/20 rounded-xl text-sm text-red-400">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-xs font-semibold text-slate-400 mb-1.5">Title *</label>
          <input name="title" value={form.title} onChange={handleChange} required className="input-field" placeholder="Summarise the issue…" />
        </div>
        <div>
          <label className="block text-xs font-semibold text-slate-400 mb-1.5">Description</label>
          <textarea name="description" value={form.description} onChange={handleChange} rows={3} className="input-field resize-none" placeholder="Steps to reproduce, context…" />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="block text-xs font-semibold text-slate-400 mb-1.5">Type *</label>
            <select name="type" value={form.type} onChange={handleChange} className="input-field">
              {TYPES.map(t => <option key={t}>{t}</option>)}
            </select>
          </div>
          <div>
            <label className="block text-xs font-semibold text-slate-400 mb-1.5">Priority *</label>
            <select name="priority" value={form.priority} onChange={handleChange} className="input-field">
              {PRIORITIES.map(p => <option key={p}>{p}</option>)}
            </select>
          </div>
        </div>
        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="block text-xs font-semibold text-slate-400 mb-1.5">Assignee</label>
            <select name="assigneeId" value={form.assigneeId} onChange={handleChange} className="input-field">
              <option value="">Unassigned</option>
              {members.map(m => <option key={m.id} value={m.id}>{m.fullName}</option>)}
            </select>
          </div>
          <div>
            <label className="block text-xs font-semibold text-slate-400 mb-1.5">Story Points</label>
            <input name="storyPoints" type="number" min="1" max="100" value={form.storyPoints} onChange={handleChange} className="input-field" placeholder="1 – 100" />
          </div>
        </div>
        <div className="flex justify-end gap-2 pt-1">
          <button type="button" onClick={onClose} className="btn-ghost">Cancel</button>
          <button type="submit" disabled={loading} className="btn-primary">{loading ? "Creating…" : "Create Issue"}</button>
        </div>
      </form>
    </Modal>
  );
}
