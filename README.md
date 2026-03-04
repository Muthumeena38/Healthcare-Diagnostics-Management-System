# 🏥 DiagnoCare - Healthcare Diagnostics Management System

A full-stack web application for managing healthcare diagnostics — built with Spring Boot, MySQL, and vanilla JavaScript.

## 🌐 Live Demo
👉 [https://hdms-j63x.onrender.com](https://hdms-j63x.onrender.com)

## 📋 Features

### 👤 Patient
- Register and login securely
- Book lab tests online
- View uploaded reports
- Download PDF reports
- See doctor remarks on reports

### 👨‍⚕️ Doctor
- Register with medical license number
- Login only after admin approval
- Search patient by ID
- View full patient report history
- Add remarks to reports

### 🔧 Admin
- Upload patient reports (PDF)
- Verify and approve/reject doctors
- Manage tests, bookings and users

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.2, Java 17 |
| Database | MySQL |
| Security | JWT Authentication |
| Frontend | HTML, CSS, JavaScript |
| Cloud DB | Railway MySQL |
| Deployment | Render |

## 🚀 How to Run Locally

### Prerequisites
- Java 17+
- MySQL
- Maven

### Steps
1. Clone the repository
```bash
git clone https://github.com/Muthumeena38/Healthcare-Diagnostics-Management-System.git
cd Healthcare-Diagnostics-Management-System
```

2. Create local properties file
```
src/main/resources/application-local.properties
```
Add your local MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/diagnocare_hdms
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Run with local profile
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

4. Open browser
```
http://localhost:8080
```

## 👥 Default Test Accounts

| Role | Email | Password |
|---|---|---|
| Admin | admin@diagnocare.com | admin123 |
| Doctor | doctor@diagnocare.com | doctor123 |
| Patient | patient@diagnocare.com | patient123 |

## 📁 Project Structure
```
src/main/
├── java/com/diagnocare/hdms/
│   ├── controller/     # REST API endpoints
│   ├── model/          # Database entities
│   ├── repository/     # JPA repositories
│   ├── service/        # Business logic
│   ├── security/       # JWT authentication
│   └── dto/            # Data transfer objects
└── resources/
    ├── static/         # Frontend HTML/CSS/JS
    └── application.properties
```

## 🔒 Security Features
- JWT token based authentication
- Role based access control (Admin, Doctor, Patient)
- Doctor verification with medical license number
- Admin approval required before doctor can login

## 📄 License
This project is for educational purposes.

---
