# Calories Management Application

Java Enterprise project with registration/authorization and role-based access control (USER, ADMIN). Administrators can create/edit/delete users, and users can manage their profile and meal data through a UI (via AJAX) and through a REST interface with basic authorization. Meals can be filtered by date and time. The color of meal table entries depends on whether the total calories for the day exceed the norm (an editable parameter in the user profile). The entire REST interface is covered by JUnit tests using Spring MVC Test and Spring Security Test.

## ⚙️ Tech Stack

### Backend
- Spring Core, Spring Security, Spring MVC, Spring Data JPA
- Hibernate ORM, Hibernate Validator
- Jackson, Ehcache, SLF4J

### Frontend
- JSP, JSTL, Bootstrap
- jQuery, DataTables, WebJars

### Database & Testing
- PostgreSQL, HSQLDB
- JUnit 5, Hamcrest, AssertJ
- Spring MVC Test, Spring Security Test

### Server
- Apache Tomcat

## 📋 Environment requirements

- Java 17+
- Maven 3.9+
- Git 2.25+
- Apache Tomcat 9+
- PostgreSQL 13

## 📚 API Documentation

[Interactive Swagger UI](http://localhost:8080/topjava/swagger-ui.html)