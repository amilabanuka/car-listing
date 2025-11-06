import React, { useState, useEffect } from "react";
import API_BASE from "../config/api";

export default function BrandsPanel() {
  const [brands, setBrands] = useState([]);
  const [brandsLoading, setBrandsLoading] = useState(false);
  const [brandsError, setBrandsError] = useState(null);
  const [brandSaving, setBrandSaving] = useState(false);
  const [brandForm, setBrandForm] = useState({ brandId: "", name: "" });

  useEffect(() => {
    fetchBrands();
  }, []);

  const fetchBrands = async () => {
    setBrandsError(null);
    setBrandsLoading(true);
    try {
      const res = await fetch(`${API_BASE}/api/brands`);
      if (!res.ok) {
        const txt = await res.text();
        throw new Error(txt || `Server returned ${res.status}`);
      }
      const data = await res.json();
      if (!Array.isArray(data)) throw new Error("Invalid response for brands");
      setBrands(data);
    } catch (err) {
      setBrandsError(err.message || "Failed to load brands");
      setBrands([]);
    } finally {
      setBrandsLoading(false);
    }
  };

  const saveBrand = async () => {
    setBrandsError(null);
    const idRaw = brandForm.brandId;
    const name = (brandForm.name || "").trim();
    const brandId = idRaw === "" ? null : parseInt(idRaw, 10);
    if (brandId === null || Number.isNaN(brandId) || name.length === 0) {
      setBrandsError("Please provide a numeric brand id and a non-empty name.");
      return;
    }

    setBrandSaving(true);
    try {
      const res = await fetch(`${API_BASE}/api/brands`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ brandId, name }),
      });
      if (res.status === 409) {
        const txt = await res.text();
        throw new Error(txt || "Brand id already exists");
      }
      if (!res.ok) {
        const txt = await res.text();
        throw new Error(txt || `Server returned ${res.status}`);
      }
      // success - refresh brands
      setBrandForm({ brandId: "", name: "" });
      await fetchBrands();
    } catch (err) {
      setBrandsError(err.message || "Failed to save brand");
    } finally {
      setBrandSaving(false);
    }
  };

  return (
    <div>
      <h3 style={{ marginTop: 0 }}>Brands</h3>

      <div style={{ border: "1px solid #eee", padding: 12, borderRadius: 6, marginBottom: 12 }}>
        <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
          <div style={{ flex: 1 }}>
            <label style={{ display: "block", fontSize: 12, color: "#333" }}>Brand name</label>
            <input
              value={brandForm.name}
              onChange={(e) => setBrandForm((s) => ({ ...s, name: e.target.value }))}
              style={{ width: "100%", padding: "6px 8px" }}
              placeholder="e.g. Toyota"
              tabIndex={1}
            />
          </div>

          <div>
            <label style={{ display: "block", fontSize: 12, color: "#333" }}>Brand id (integer)</label>
            <input
              value={brandForm.brandId}
              onChange={(e) => setBrandForm((s) => ({ ...s, brandId: e.target.value }))}
              style={{ width: 120, padding: "6px 8px" }}
              placeholder="e.g. 42"
              tabIndex={2}
            />
          </div>

          <div>
            <button onClick={saveBrand} disabled={brandSaving}>
              {brandSaving ? "Saving..." : "Save"}
            </button>
          </div>
        </div>

        {brandsError && <div style={{ color: "darkred", marginTop: 8 }}>{brandsError}</div>}
      </div>

      <div>
        <h4 style={{ marginBottom: 8 }}>Existing brands</h4>
        {brandsLoading ? (
          <div>Loading brands...</div>
        ) : brands.length === 0 ? (
          <div style={{ color: "#666" }}>No brands saved yet.</div>
        ) : (
          <table style={{ borderCollapse: "collapse", width: "100%" }}>
            <thead>
              <tr>
                <th style={{ textAlign: "left", borderBottom: "1px solid #ddd", padding: "6px" }}>Name</th>
                <th style={{ textAlign: "left", borderBottom: "1px solid #ddd", padding: "6px" }}>Brand id</th>
              </tr>
            </thead>
            <tbody>
              {brands.map((b, i) => (
                <tr key={i}>
                  <td style={{ padding: "6px", borderBottom: "1px solid #f0f0f0" }}>{b.name}</td>
                  <td style={{ padding: "6px", borderBottom: "1px solid #f0f0f0" }}>{b.brandId != null ? String(b.brandId) : "-"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
