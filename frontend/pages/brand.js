import Layout from '../src/components/Layout';
import ListingParser from '../src/components/ListingParser';

export default function BrandPage() {
  return (
    <Layout>
      <div>
        <h1 style={{ marginTop: 0 }}>Brand</h1>
        <ListingParser initialTab="brands" />
      </div>
    </Layout>
  );
}
