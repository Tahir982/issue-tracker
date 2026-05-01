import api from "./axios";
export const getIssues    = pid        => api.get(`/projects/${pid}/issues`);
export const getIssue     = id         => api.get(`/issues/${id}`);
export const createIssue  = (pid, d)   => api.post(`/projects/${pid}/issues`, d);
export const updateIssue  = (id, d)    => api.put(`/issues/${id}`, d);
export const updateStatus = (id, s)    => api.patch(`/issues/${id}/status`, { status: s });
export const assignIssue  = (id, uid)  => api.patch(`/issues/${id}/assign`, { assigneeId: uid });
export const deleteIssue  = id         => api.delete(`/issues/${id}`);
export const myIssues     = ()         => api.get("/issues/my-issues");
