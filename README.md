Car Listing (multi-module)

This repository has been converted into a Maven multi-module project with two modules:

- backend: Java module (jar) that contains the application's Main class (Spring Boot application).
  - Main class: `lk.amila.fio.Main`
  - Built JAR: `backend/target/backend-1.0-SNAPSHOT.jar`

- frontend: static resource module (jar) that packages files under `src/main/resources`.
  - Example static file: `frontend/src/main/resources/static/index.html`
  - Built JAR: `frontend/target/frontend-1.0-SNAPSHOT.jar`

Quick commands (Windows cmd):

- Build the whole project:
  mvn -DskipTests clean package

- Run the backend (Spring Boot):
  cd backend
  mvn spring-boot:run

  Or run the built JAR directly:
  java -jar target\backend-1.0-SNAPSHOT.jar

- Test the backend (once started):
  curl http://localhost:8080/api/hello
  # Expected output: Hello from Spring Boot backend!

- Stop the backend:
  If started with `mvn spring-boot:run`, press Ctrl+C in that terminal.
  If started with `java -jar`, find the process and kill it (Windows):

  netstat -aon | findstr ":8080"
  taskkill /PID <PID> /F

- Inspect frontend static file:
  The `index.html` file is available at `frontend/src/main/resources/static/index.html`.
  To serve it locally, you can extract the jar or use any static file server, e.g. Python's http.server:

  cd frontend\src\main\resources\static
  python -m http.server 8080

Notes & next steps:
- Spring Boot is added to the backend and a simple controller is available at `/api/hello`.
- If you want a full web app, consider adding a front-end build (React/Vite) and have the backend serve the production build.
- I removed the duplicate runnable Main from the root module and left a pointer to the backend Main; you can delete the file if desired.
