/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  async rewrites() {
    return [
      // Proxy API requests during development to the backend running on port 8080
      { source: '/api/:path*', destination: 'http://localhost:8080/api/:path*' },
    ];
  },
};

module.exports = nextConfig;
