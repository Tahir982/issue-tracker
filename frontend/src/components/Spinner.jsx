export default function Spinner({ size = "md" }) {
  const sz = size === "sm" ? "w-4 h-4" : size === "lg" ? "w-12 h-12" : "w-8 h-8";
  return <div className={`${sz} rounded-full border-[3px] border-brand-500 border-t-transparent animate-spin`} />;
}
