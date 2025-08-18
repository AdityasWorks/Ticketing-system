# Full-Stack Ticketing System

This is a full-stack web application designed to simulate a real-world IT support or customer service ticketing system. It allows users to raise, manage, and resolve support tickets with proper role-based access and administrative control.

The project was built according to the provided assignment specification, implementing all "Must-Have" features and several "Good-to-Have" features.

---

## Tech Stack

### Backend
* **Language:** Java 17
* **Framework:** Spring Boot
* **Security:** Spring Security (JWT Authentication)
* **Database:** PostgreSQL
* **Data Access:** Spring Data JPA (Hibernate)
* **File Storage:** Azure Blob Storage integration
* **Email:** Spring Boot Mail + Mailtrap for testing

### Frontend
* **Framework:** Next.js (App Router)
* **Language:** TypeScript
* **Styling:** Tailwind CSS
* **API Communication:** Axios

### Deployment
* **Containerization:** Docker & Docker Compose

---

## Features Implemented

### ✅ Must-Have Features

* **Authentication & Authorization**
    * Secure login with JSON Web Tokens (JWT).
    * Role-based access control for `USER`, `SUPPORT_AGENT`, and `ADMIN` roles.
    * Users can only manage their own tickets.
* **User Dashboard**
    * Users can raise new tickets with a subject, description, and priority.
    * Users can view a list of all their created tickets and their current status.
    * Users can add comments to their tickets.
    * Users can view the full history of a ticket, including all comments from themselves, agents, and admins.
* **Ticket Management**
    * Full ticket lifecycle support (`OPEN` → `IN_PROGRESS` → `RESOLVED` → `CLOSED`).
    * Support for a full comment thread on each ticket with author and timestamp information.
    * Tracks the ticket requester (owner) and assignee.
* **Admin Panel**
    * **User Management:** Admins can view all users, add new users with a specific role, remove users, and change the role of any existing user.
    * **Ticket Management:** Admins can view all tickets, force reassign any ticket, and forcibly change the status of any ticket.
* **Access Control**
    * Secured endpoints ensure that only authorized roles can perform specific actions (e.g., only admins can manage users, only assigned agents can update ticket status).

### ☑️ Good-to-Have Features

* **Email Notifications**: The system automatically sends an email to a user when their ticket is created.
* **File Attachments**: Users can securely upload a file/screenshot when creating a ticket. The system is integrated with Azure Blob Storage (or a local emulator).

---

## Local Setup and Running Instructions

The project is designed to be run locally without requiring a Docker environment.

### Prerequisites
* Java (JDK 17 or later)
* Maven 3.8+
* Node.js 18+ and npm
* PostgreSQL running locally

### Backend Setup

1.  **Navigate to the backend directory:**
    ```bash
    cd ticketingsystem
    ```
2.  **Configure Environment:**
    The application requires environment variables for its secrets. Set the following variables in your shell before running the application:
    ```bash
    # For PostgreSQL Database
    export DB_PASSWORD="your_postgres_password"

    # For JWT Security
    export JWT_SECRET_KEY="your_long_and_secure_base64_encoded_jwt_secret"
    use this if you want to save this this is randomly generated ```26vMztoL0UQqNUZAKmIIbErDKH5r7V4l26vMztoL0UQqNUZAKmIIbErDKH5r7V4l```
    
    # For Email Notifications (using a free Mailtrap.io account is recommended for testing)
    export MAILTRAP_USERNAME="your_mailtrap_username"
    export MAILTRAP_PASSWORD="your_mailtrap_password"
    
    # For File Uploads (Optional - The app will run without this)
    export AZURE_STORAGE_CONNECTION_STRING="your_azure_blob_storage_connection_string"
    ```
3.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    or just use "run java" in the IDE
    The backend API will be available at `http://localhost:8080`.

### Frontend Setup

1.  **Navigate to the frontend directory:**
    ```bash
    cd frontend
    ```
2.  **Install dependencies:**
    ```bash
    npm install
    ```
3.  **Run the development server:**
    ```bash
    npm run dev
    ```
    The frontend will be available at `http://localhost:3000`.

### Database Seeder

On the first startup against an empty database, the application will automatically create three users for easy testing:
* **Admin:** `admin@example.com` / `admin`
* **Support Agent:** `support@example.com` / `support`
* **Regular User:** `user@example.com` / `user`


### Docker

Soon dockerfiles will be update on the github https://github.com/AdityasWorks/Ticketing-system