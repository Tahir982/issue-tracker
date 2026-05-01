export default function StatCard({ label, value, icon: Icon, color = "brand" }) {
  const colors = { brand:"text-brand-400 bg-brand-500/10", emerald:"text-emerald-400 bg-emerald-500/10", amber:"text-amber-400 bg-amber-500/10", red:"text-red-400 bg-red-500/10", slate:"text-slate-400 bg-slate-500/10", violet:"text-violet-400 bg-violet-500/10" };
  const [text, bg] = (colors[color] || colors.brand).split(" ");
  return (
    <div className="glass p-5 flex items-center gap-4">
      <div className={`w-11 h-11 rounded-xl ${bg} flex items-center justify-center flex-shrink-0`}>
        <Icon size={20} className={text} />
      </div>
      <div>
        <p className="text-2xl font-bold text-slate-100">{value ?? 0}</p>
        <p className="text-xs text-slate-500 mt-0.5">{label}</p>
      </div>
    </div>
  );
}
