// Reusable fetch wrapper — handles 401 / 403 automatically
async function apiFetch(url, options = {}) {
    try {
        // Default config: send cookies + include CSRF token
        const defaultOptions = {
            credentials: "include", // ✅ Auto-send JSESSIONID
            headers: {
                "Content-Type": "application/json",
                // Get CSRF token from meta tag
                "X-CSRF-TOKEN": document.querySelector('meta[name="_csrf"]')?.content || ""
            }
        };

        // Merge user options with defaults
        const fetchOptions = {
            ...defaultOptions,
            ...options,
            headers: {
                ...defaultOptions.headers,
                ...options.headers
            }
        };

        // Make request
        const response = await fetch(url, fetchOptions);

        // ✅ 1. Intercept Errors
        if (response.status === 401) {
            // ✅ 2. Redirect Logic: 401 = Not logged in → go to login
            alert("⚠️ You are not logged in. Redirecting to login...");
            window.location.href = "/login";
            throw new Error("401 Unauthorized");
        }

        if (response.status === 403) {
            // ✅ 2. Redirect Logic: 403 = Logged in but wrong role → show message
            alert("⛔ ACCESS DENIED: You do not have permission to view this page or perform this action.");
            throw new Error("403 Forbidden — Insufficient permissions");
        }

        // Return data if successful
        if (response.ok) {
            return await response.json();
        }

        throw new Error(`Request failed: ${response.status}`);

    } catch (error) {
        console.error("API Error:", error.message);
        throw error;
    }
}