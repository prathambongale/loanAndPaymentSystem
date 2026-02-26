# LNP System

## Overview

The LNP System is a Spring Boot application designed to manage loan and payment services. This document provides instructions on how to build, run, and test the application, along with additional setup notes.

---

## How to Build and Run the Application

1. **Prerequisites:**
   - Ensure you have Java 17 or higher installed.
   - Install Maven (minimum version 3.6.0).

2. **Run the Application:**

   ```bash
   mvn spring-boot:run
   ```

3. **If you face issues with step 2. The first Build the Application using below command and then try step 2 again:**

   ```bash
   mvn clean install
   ```

The application will start on the default port `8080`. You can access it at `http://localhost:8080`.

---

## How to Test the APIs

### Using Swagger

We recommend using Swagger for API testing and documentation. Once the application is running, navigate to the following URL in your browser:

```
http://localhost:8080/swagger-ui.html
```

Swagger provides an interactive interface to test the APIs and view their documentation.

### Alternative Methods

You can also use tools like Postman or `curl` to test the APIs. Refer to the Swagger documentation for endpoint details.

---

## Additional Configuration and Setup Notes

1. **H2 Console:**
   The application uses an in-memory H2 database for development and testing. You can access the H2 console to view and manage the database at:

   ```
   http://localhost:8080/h2-console
   ```

   - **JDBC URL:** `jdbc:h2:mem:testdb`
   - **Username:** `sa`
   - **Password:** (leave blank by default)

2. **Application Properties:**
   You can configure application settings in the `application.properties` file located in the `src/main/resources` directory.

---

## Notes

- Ensure all dependencies are installed before running the application.

---

For further assistance, please contact <prathamesh@digitamygdala.com>.
