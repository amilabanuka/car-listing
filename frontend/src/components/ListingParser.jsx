import React, { useMemo, useState } from "react";

export default function ListingParser() {
  const [jsonInput, setJsonInput] = useState("");
  const [listings, setListings] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const submit = async () => {
    setError(null);
    if (!jsonInput.trim()) {
      setError("Please paste a JSON string into the input.");
      setListings([]);
      return;
    }

    setLoading(true);
    try {
      // POST the raw JSON string to the backend ListingController
      // Use full origin to avoid depending on proxy configuration during dev
      const res = await fetch("http://localhost:8080/api/listings/parse", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: jsonInput,
      });

      if (!res.ok) {
        const txt = await res.text();
        throw new Error(txt || `Server returned ${res.status}`);
      }

      const data = await res.json();
      if (!Array.isArray(data)) {
        throw new Error("Expected an array of listings from server.");
      }
      setListings(data);
    } catch (err) {
      setError(err.message || "Failed to parse listings.");
      setListings([]);
    } finally {
      setLoading(false);
    }
  };

  const columns = useMemo(() => {
    const keys = new Set();
    listings.forEach((item) => {
      if (item && typeof item === "object") {
        Object.keys(item).forEach((k) => keys.add(k));
      }
    });
    return Array.from(keys);
  }, [listings]);

  const renderCell = (col, val) => {
    if (val === null || val === undefined) return "";

    // Special rendering for seals: show thumbnails and culture
    if (col === "seals" && Array.isArray(val)) {
      return (
        <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
          {val.map((s, i) => {
            const img = s && (s.image || s.url || s.img);
            const culture = s && (s.culture || s.locale);
            return (
              <div key={i} style={{ display: "flex", alignItems: "center", gap: 6 }}>
                {img ? (
                  <img
                    src={img}
                    alt={culture || `seal-${i}`}
                    style={{ width: 28, height: 14, objectFit: "contain", display: "block" }}
                  />
                ) : null}
                {culture ? (
                  <span style={{ fontSize: 11, color: "#333", background: "#f2f2f2", padding: "2px 6px", borderRadius: 4 }}>
                    {culture}
                  </span>
                ) : null}
              </div>
            );
          })}
        </div>
      );
    }

    // Special rendering for isOfferNew boolean
    if (col === "isOfferNew") {
      if (typeof val === "boolean") {
        return (
          <span style={{ color: val ? "#155724" : "#6c757d", background: val ? "#d4edda" : "transparent", padding: "4px 6px", borderRadius: 4 }}>
            {val ? "Yes" : "No"}
          </span>
        );
      }
      return String(val);
    }

    // If it's an array of strings (like images), render a small preview of first 2
    if (Array.isArray(val) && val.length > 0 && typeof val[0] === "string") {
      return (
        <div style={{ display: "flex", gap: 6, alignItems: "center" }}>
          {val.slice(0, 2).map((u, i) => (
            <img key={i} src={u} alt={`img-${i}`} style={{ width: 60, height: 45, objectFit: "cover", borderRadius: 4 }} />
          ))}
          {val.length > 2 ? <span style={{ fontSize: 12, color: "#666" }}>+{val.length - 2}</span> : null}
        </div>
      );
    }

    // If it's an object/array, stringify with compact formatting
    if (typeof val === "object") {
      try {
        return <pre style={{ margin: 0, whiteSpace: "pre-wrap", fontSize: 12 }}>{JSON.stringify(val, null, 2)}</pre>;
      } catch (e) {
        return String(val);
      }
    }

    // Fallback to string
    return String(val);
  };

  return (
    <div style={{ padding: 16, fontFamily: "Arial, sans-serif" }}>
      <h2>Listing JSON Parser (paste page JSON)</h2>

      <textarea
        value={jsonInput}
        onChange={(e) => setJsonInput(e.target.value)}
        placeholder="Paste raw JSON here (the controller will extract pageProps.listings)"
        rows={12}
        style={{ width: "100%", fontFamily: "monospace", marginBottom: 8 }}
      />

      <div style={{ display: "flex", gap: 8, marginBottom: 12 }}>
        <button onClick={submit} disabled={loading}>
          {loading ? "Parsing..." : "Parse and Show Table"}
        </button>
        <button
          onClick={() => {
            setJsonInput("");
            setListings([]);
            setError(null);
          }}
        >
          Clear
        </button>
      </div>

      {error && <div style={{ color: "darkred", marginBottom: 12 }}>{error}</div>}

      {listings.length === 0 && !error && (
        <div style={{ color: "#666" }}>No listings to show.</div>
      )}

      {listings.length > 0 && (
        <div style={{ overflowX: "auto", borderTop: "1px solid #eee", paddingTop: 8 }}>
          <table style={{ borderCollapse: "collapse", width: "100%" }}>
            <thead>
              <tr>
                {columns.map((col) => (
                  <th
                    key={col}
                    style={{
                      textAlign: "left",
                      borderBottom: "1px solid #ddd",
                      padding: "8px",
                      background: "#fafafa",
                    }}
                  >
                    {col}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {listings.map((row, ri) => (
                <tr key={ri}>
                  {columns.map((col) => {
                    const val = row ? row[col] : undefined;
                    return (
                      <td
                        key={col}
                        style={{ padding: "8px", borderBottom: "1px solid #f0f0f0", verticalAlign: "top" }}
                      >
                        {renderCell(col, val)}
                      </td>
                    );
                  })}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
