# 💈 Salon Management System

A full-stack web application developed as a 5-member group project for **IE2091 – Information Systems Project (2nd Year, 2nd Semester, SLIIT)**.

This project was selected for **project showcase and awarded 1st Runner-Up**, highlighting its strong system design, modular implementation, and effective use of Agile/Scrum methodology.

---

## 📌 Project Overview

The Salon Management System is a role-based web application designed to streamline and digitize salon operations. It replaces traditional manual processes with an integrated system covering customer management, appointment scheduling, service and package handling, payments, feedback, and administrative control.

The system supports four primary user roles:

- Owner (Admin)
- Staff
- Receptionist
- Customer

Each role operates through a dedicated, access-controlled interface based on Role-Based Access Control (RBAC).

---

## 🎯 Key Objectives

- Digitize end-to-end salon operations
- Improve efficiency in booking and service management
- Centralize customer and staff data
- Implement secure authentication and role-based authorization
- Support scalable modular system design

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.3.3 |
| Frontend | Thymeleaf, HTML, CSS |
| Security | Spring Security 6 |
| ORM | Spring Data JPA (Hibernate) |
| Database | Microsoft SQL Server |
| Build Tool | Maven |
| Additional | Lombok, Spring Validation, DevTools |

---

## 👥 User Roles

| Role | Description |
|---|---|
| **Owner / Admin** | Full system control including users, services, packages, and system-level decisions |
| **Staff** | Manages appointments, and assists customers |
| **Receptionist** | Handles walk-in bookings and daily appointment coordination |
| **Customer** | Self-registration, booking, feedback submission, and profile management |

---

## ✨ Core Features

### 🔐 Authentication & Security
- Customer self-registration with validation
- Role-based login system (Owner, Staff, Receptionist, Customer)
- Spring Security with BCrypt encryption
- Secure logout implementation
- Password reset and password change functionality

---

### 👥 Role-Based Access Control (RBAC)
- Role assignment (Owner, Staff, Receptionist, Customer)
- Feature restriction based on user roles
- Account activation and deactivation
- User activity tracking

---

### 👤 Profile Management
- Customer profile view and update
- Staff profile management
- Receptionist profile management
- Self-service password updates

---

### 🔍 User Management *(My Contribution – Core Epic)*
- Customer account creation and management
- Staff and receptionist account creation
- Role assignment and access control enforcement
- User search by name or role
- View customer account details and service history
- Account activation/deactivation management
- Secure logout handling

---

### 📅 Appointment Management
- Book appointments by selecting services and time slots
- View upcoming and past appointments
- Reschedule and cancel appointments
- Receptionist-assisted booking support

---

### 💳 Service & Package Management
- Create and manage service categories
- CRUD operations for individual services
- Package creation with multiple services
- Browse and manage service packages

---

### 💰 Payments & Reports
- Payment processing per appointment
- Payment status tracking (Paid / Pending / Refunded)
- Basic reporting for appointments and transactions

---

### ⭐ Feedback & Ratings
- Customer feedback submission after service completion
- Rating system for services and staff
- Feedback visibility for administrators

---

### 💇 Style Recommendation System
- Customer preference-based style suggestions
- Rule-based recommendation logic
- Save and manage preferred styles

---

## 🏗️ Project Structure

```
Saloon_Management_System/
├── src/main/java/org/example/
│   ├── config/          # Security, CORS, password config, data initializer
│   ├── controller/      # MVC controllers (Auth, Appointment, Booking, Feedback, etc.)
│   ├── domain/          # JPA entities (Customer, Employee, Appointment, etc.)
│   ├── dto/             # Data Transfer Objects
│   ├── repository/      # Spring Data JPA repositories
│   ├── service/         # Business logic layer
│   └── exception/       # Validation exception handling
├── src/main/resources/
│   ├── templates/       # Thymeleaf HTML views
│   └── application.properties
└── pom.xml
```

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- Microsoft SQL Server (running on port `1433`)

### Setup

1. **Clone the repository**
```bash
   git clone https://github.com/your-username/salon-management-system.git
   cd salon-management-system/Saloon_Management_System
```

2. **Configure the database**

   Create a SQL Server database named `MergedISP`, then update `src/main/resources/application.properties`:
```properties
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=MergedISP;encrypt=true;trustServerCertificate=true
   spring.datasource.username=your_username
   spring.datasource.password=your_password
```

3. **Run the application**
```bash
   mvn spring-boot:run
```

4. **Access the application**

   Open your browser and navigate to: `http://localhost:8080`

> The app uses `spring.jpa.hibernate.ddl-auto=update`, so database tables are created/updated automatically on first run.

---

## 👨‍💻 Team — Group PG19

This was a collaborative group project. The system was divided into epics, each owned by a team member.

| Epic | Description |
|---|---|
| **Admin & Customer Management** | Authentication, RBAC, profile management, user search & history |
| **Appointment & Booking** | Scheduling, rescheduling, daily view, appointment history |
| **Service & Package Management** | Service categories, packages, browsing, purchase tracking |
| **Payments & Reports** | Payment handling, revenue reports, monthly summaries |
| **Feedback & Ratings** | Customer reviews, ratings, public display |
| **Style Recommendation** | Feature-based hairstyle suggestions, saved styles |

---

## 🏆 Achievement

- Selected for IE2091 Project Showcase 2026  
- Awarded 1st Runner-Up  
- IE2091 – Information Systems Project (SLIIT)

---

## 📄 Academic Context

- Module: IE2091 – Information Systems Project  
- Year: 2nd Year, 2nd Semester  
- Institution: Sri Lanka Institute of Information Technology (SLIIT)  
- Methodology: Agile / Scrum-based development  

---

## 📌 License

This project was developed for academic purposes. All rights reserved by the project team.

---
