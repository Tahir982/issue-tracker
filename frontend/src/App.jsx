import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";
import Layout        from "./components/Layout";
import Login         from "./pages/Login";
import Register      from "./pages/Register";
import Dashboard     from "./pages/Dashboard";
import Projects      from "./pages/Projects";
import ProjectDetail from "./pages/ProjectDetail";
import IssueDetail   from "./pages/IssueDetail";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login"    element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route element={<ProtectedRoute />}>
            <Route element={<Layout />}>
              <Route index                                element={<Dashboard />} />
              <Route path="/projects"                     element={<Projects />} />
              <Route path="/projects/:id"                 element={<ProjectDetail />} />
              <Route path="/projects/:pid/issues/:id"     element={<IssueDetail />} />
            </Route>
          </Route>
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
