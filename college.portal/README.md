# Landmine Soft — College LMS Portal

A complete college management backend built with Spring Boot 3.2,
PostgreSQL, JWT authentication, and role-based access control.

## Live Demo
- **API Base URL:** https://lms-college-portal.onrender.com
- **Swagger UI:** https://lms-college-portal.onrender.com/swagger-ui.html
- **Health Check:** https://lms-college-portal.onrender.com/api/health

## Tech Stack
- Java 17 + Spring Boot 3.2
- Spring Security + JWT (access + refresh tokens)
- Spring Data JPA + Hibernate
- PostgreSQL (Render cloud)
- Docker (deployment)
- Swagger/OpenAPI

## Features
- Student / Faculty / Admin registration and login
- JWT access token (24hr) + refresh token (7 days)
- Token blacklisting on logout
- Role-based access control (RBAC)
- Password reset via email (Gmail SMTP)
- Profile view and update
- Admin dashboard with stats

## Local Setup
### Prerequisites
- Java 17, Maven, PostgreSQL

### Steps
git clone https://github.com/YOUR_USERNAME/lms-college-portal.git
cd lms-college-portal
# Set env vars or edit application.properties
mvn spring-boot:run

### API Docs
http://localhost:8080/swagger-ui.html

## Deployment
Deployed on Render using Docker.
See Dockerfile in project root.

## Developer
Sambit Kumar Mohapatra | MCA Graduate | Indira Gandhi Institute of Technology, Sarang
GitHub: https://github.com/SambitKumarMohapatra