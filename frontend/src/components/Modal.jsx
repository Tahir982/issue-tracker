import { X } from "lucide-react";
export default function Modal({ title, onClose, children }) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm">
      <div className="glass w-full max-w-lg p-6">
        <div className="flex items-center justify-between mb-5">
          <h2 className="font-semibold text-base text-slate-100">{title}</h2>
          <button onClick={onClose} className="w-8 h-8 rounded-lg flex items-center justify-center text-slate-400 hover:text-slate-200 hover:bg-white/[.08] transition-colors"><X size={16} /></button>
        </div>
        {children}
      </div>
    </div>
  );
}
