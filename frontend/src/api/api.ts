const API_BASE_URL = "http://localhost:8080/api";

export async function apiRequest<T = any>(
  endpoint: string,
  method: string = "GET",
  body: any = null,
  token: string | null = null
): Promise<T> {
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  };

  if (token) {
    headers["X-Auth-Token"] = token;
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : null,
  });

  if (!response.ok) {
    let message = `Request failed (${response.status})`;

    try {
      const errorData = await response.json();
      message = errorData.message || message;
    } catch {
      // fallback se não for JSON
      const text = await response.text();
      if (text) message = text;
    }

    throw new Error(message);
  }

  const contentType = response.headers.get("content-type") || "";

  if (!contentType.includes("application/json")) {
    return (await response.text()) as T;
  }

  return (await response.json()) as T;
}