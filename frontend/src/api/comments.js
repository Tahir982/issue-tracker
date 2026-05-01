import api from "./axios";
export const getComments   = issueId    => api.get(`/issues/${issueId}/comments`);
export const addComment    = (id, d)    => api.post(`/issues/${id}/comments`, d);
export const updateComment = (id, d)    => api.put(`/comments/${id}`, d);
export const deleteComment = id         => api.delete(`/comments/${id}`);
