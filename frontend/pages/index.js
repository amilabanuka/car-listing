import ListingParser from '../src/components/ListingParser';

export default function Home() {
  return (
    <div style={{fontFamily: 'Arial, sans-serif', padding: 24}}>
      <h1>Car Listing - Frontend (Next.js)</h1>
      <p>This is a placeholder Next.js frontend. Build your pages under the <code>pages/</code> directory.</p>
      <p>To call the backend API (running on port 8080), visit <a href="http://localhost:8080/api/hello" target="_blank" rel="noopener noreferrer">http://localhost:8080/api/hello</a></p>

      {/* Render the JSON parser UI */}
      <ListingParser />
    </div>
  )
}
