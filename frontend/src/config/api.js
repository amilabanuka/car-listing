// Central API base config for the frontend.
// Use NEXT_PUBLIC_API_BASE to override in development/production environments.
const API_BASE = process.env.NEXT_PUBLIC_API_BASE || "http://localhost:8080";
export default API_BASE;

