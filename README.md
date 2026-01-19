# File Upload Application

A full-stack web application for uploading, downloading, and managing files, with special support for Excel file operations.

## Tech Stack

### Frontend

- **Angular** 21
- **Tailwind CSS** 4.1.12
- **DaisyUI** 5.5.14
- **TypeScript** 5.9.2

### Backend

- **Spring Boot** 3.5.9
- **Java** 25
- **Maven**
- **Apache POI** 5.2.5 (for Excel operations)

## Features

- File upload and download
- File listing
- Excel file reading and writing
- RESTful API
- Responsive UI with Tailwind CSS and DaisyUI components

## Project Structure

```
├── frontend/
│   ├── src/app/
│   │   ├── components/     # UI components (file-card, file-modal, etc.)
│   │   └── service/        # API service (configservice.ts)
│   └── package.json
├── backend/
│   ├── src/main/java/com/example/backend/
│   │   ├── controller/     # REST controllers
│   │   ├── services/       # Business logic
│   │   └── entity/         # Data models
│   └── pom.xml
└── README.md
```

## Prerequisites

- **Node.js** (for frontend)
- **npm** or **yarn**
- **Java 25**
- **Maven**

## Installation & Setup

### Backend Setup

1. Navigate to the backend directory:

   ```bash
   cd backend
   ```

2. Run the Spring Boot application:

   ```bash
   ./mvnw spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:

   ```bash
   cd frontend
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Start the development server:

   ```bash
   npm start
   ```

   The frontend will start on `http://localhost:4200`

## API Endpoints

- `GET /list` - Get list of uploaded files
- `POST /upload` - Upload a file
- `GET /download/{filename}` - Download a file
- `GET /read?filename={filename}` - Read Excel file content
- `POST /write` - Write data to Excel file

## Usage

1. Start both backend and frontend servers
2. Open `http://localhost:4200` in your browser
3. Use the file input to select and upload files
4. View uploaded files in the list
5. Download files by clicking on them

## Development

### Running Tests

#### Backend

```bash
cd backend
./mvnw test
```

#### Frontend

```bash
cd frontend
npm test
```

### Building for Production

#### Frontend

```bash
cd frontend
npm run build
```

#### Backend

```bash
cd backend
./mvnw clean package
```

## Contributing

This project was generated with the assistance of AI tools.

> 100 % AI
