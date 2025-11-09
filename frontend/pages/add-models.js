import { useState } from 'react';

export default function AddModelsPage() {
  const [jsonText, setJsonText] = useState('');
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setResponse(null);

    // Client-side validate JSON to avoid obvious 400s
    try {
      JSON.parse(jsonText);
    } catch (err) {
      setError('Invalid JSON: ' + err.message);
      return;
    }

    setLoading(true);
    try {
      const res = await fetch('http://localhost:8080/api/models/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: jsonText
      });

      let body;
      try {
        body = await res.json();
      } catch (err) {
        body = await res.text();
      }

      setResponse({ status: res.status, body });
    } catch (err) {
      setError('Network error: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  const handlePasteSample = () => {
    const sample = `{
  "models": {
    "model": {
      "values": [
        { "id": 75089, "name": "D2", "makeId": 51798, "vehicleTypeId": "C" },
        { "id": 75090, "name": "KWB", "makeId": 51798, "vehicleTypeId": "C" }
      ]
    }
  }
}`;
    setJsonText(sample);
  };

  return (
    <div style={{ padding: 20, maxWidth: 960, margin: '0 auto', fontFamily: 'Arial, sans-serif' }}>
      <h1>Add Models</h1>
      <p>Paste a JSON string containing a <code>models</code> object and send it to the backend.</p>

      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: 8 }}>
          <textarea
            value={jsonText}
            onChange={(e) => setJsonText(e.target.value)}
            placeholder='Paste JSON here'
            rows={18}
            style={{ width: '100%', fontFamily: 'monospace', fontSize: 13, padding: 8 }}
          />
        </div>

        <div style={{ display: 'flex', gap: 8 }}>
          <button type="submit" disabled={loading} style={{ padding: '8px 12px' }}>
            {loading ? 'Sending...' : 'Send to backend'}
          </button>
          <button type="button" onClick={handlePasteSample} style={{ padding: '8px 12px' }}>
            Paste sample
          </button>
          <button type="button" onClick={() => { setJsonText(''); setResponse(null); setError(null); }} style={{ padding: '8px 12px' }}>
            Clear
          </button>
        </div>
      </form>

      <div style={{ marginTop: 16 }}>
        {error && (
          <div style={{ color: 'crimson', whiteSpace: 'pre-wrap' }}>
            <strong>Error:</strong> {error}
          </div>
        )}

        {response && (
          <div style={{ marginTop: 12 }}>
            <h3>Response (HTTP {response.status})</h3>
            <pre style={{ background: '#f6f8fa', padding: 12, overflowX: 'auto' }}>{typeof response.body === 'string' ? response.body : JSON.stringify(response.body, null, 2)}</pre>
          </div>
        )}
      </div>

      <hr style={{ marginTop: 20 }} />
      <div style={{ fontSize: 13, color: '#555' }}>
        <p>Notes:</p>
        <ul>
          <li>This page sends the raw JSON string as the request body with Content-Type: application/json.</li>
          <li>Ensure the backend is running at <code>http://localhost:8080</code>.</li>
        </ul>
      </div>
    </div>
  );
}

