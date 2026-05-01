import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
export default function ProtectedRoute() {
  const { user, loading } = useAuth();
  if (loading) return (
    <div className="flex h-screen items-center justify-center bg-slate-950">
      <div className="w-10 h-10 rounded-full border-[3px] border-brand-500 border-t-transparent animate-spin" />
    </div>
  );
  return user ? <Outlet /> : <Navigate to="/login" replace />;
}
