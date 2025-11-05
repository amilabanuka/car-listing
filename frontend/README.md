Frontend (Next.js)

This folder is an independent Next.js app. Commands (run from the `frontend` folder):

- Install dependencies:
  npm install

- Run development server:
  npm run dev

- Build for production:
  npm run build

- Start production server (after build):
  npm run start

The backend runs independently (Spring Boot) and exposes an API at http://localhost:8080/api/hello by default. Adjust API URLs in the frontend as needed.

