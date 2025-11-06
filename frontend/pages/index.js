import Link from 'next/link';
import Layout from '../src/components/Layout';

export default function Home() {
  return (
    <Layout>
      <div style={{ fontFamily: 'Arial, sans-serif', padding: 24 }}>
        <h1>Car Listing - Frontend (Next.js)</h1>
        <p>This is a placeholder Next.js frontend. Build your pages under the <code>pages/</code> directory.</p>
        <p>To call the backend API (running on port 8080), visit <a href="http://localhost:8080/api/hello" target="_blank" rel="noopener noreferrer">http://localhost:8080/api/hello</a></p>

        <h2>Open a page</h2>
        <ul>
          <li><Link href="/add-listing">Add listing</Link></li>
          <li><Link href="/brand">Brand</Link></li>
        </ul>

        <p style={{ marginTop: 20, color: '#666' }}>You can split more features into their own pages — I created the basic pages for the listing parser and brand management.</p>
      </div>
    </Layout>
  )
}
