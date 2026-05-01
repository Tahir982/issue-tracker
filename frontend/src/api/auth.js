import api from "./axios";
export const login    = d => api.post("/auth/login", d);
export const register = d => api.post("/auth/register", d);
export const getMe    = () => api.get("/users/me");
export const getUsers = () => api.get("/users");
