import Layout from '../src/components/Layout';
import ListingParser from '../src/components/ListingParser';

export default function AddListingPage() {
  return (
    <Layout>
      <div>
        <h1 style={{ marginTop: 0 }}>Add listing</h1>
        <ListingParser initialTab="listings" />
      </div>
    </Layout>
  );
}
