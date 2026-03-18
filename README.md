# E-commerce Platform

A robust, full-stack E-commerce solution built with the Spring Boot 3 ecosystem. This application provides a platform for browsing products, managing a shopping cart, and secure user authentication.

## 🛡️ Key Features
- **Secure Authentication**: RBAC (Role-Based Access Control) using Spring Security.
- **Product Catalog**: Dynamic browsing with categories, product details, and stock management.
- **Shopping Cart**: Real-time cart management for logged-in users.
- **Order Management**: Checkout process and order tracking (ready for extension).
- **Responsive UI**: Modern interface built with Thymeleaf, HTML5, and CSS3.
- **Auto-Initialization**: Automatic seeding of sample data (admin user and products) on the first run.

## 🛠️ Tech Stack
- **Backend**: Java 17, Spring Boot 3.2.5, Spring Data JPA (Hibernate).
- **Security**: Spring Security 6 with BCrypt password encoding.
- **Database**: MySQL 8.0+.
- **Frontend**: Thymeleaf template engine, HTML5, CSS3.
- **Build Tool**: Maven 3.8+.

## 🚀 Getting Started

### Prerequisites
- **JDK 17** or higher.
- **MySQL Server** (ensure it's running).
- **Maven** (optional, if using `./mvnw`).

### Installation & Setup
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/ecommerce-platform.git
   cd "E-commerce Platform"
   ```

2. **Database Configuration**:
   - Create a database named `ecommerce_db` in MySQL:
     ```sql
     CREATE DATABASE ecommerce_db;
     ```
   - Update `src/main/resources/application.properties` with your MySQL credentials:
     ```properties
     spring.datasource.username=YOUR_USERNAME
     spring.datasource.password=YOUR_PASSWORD
     ```

3. **Build the project**:
   ```bash
   mvn clean install
   ```

4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the platform**:
   - URL: `http://localhost:8080`
   - **Default Admin Credentials**:
     - Email: `admin@example.com`
     - Password: `admin123`

## 📜 Scripts & Commands
- `mvn clean install`: Builds the project and runs tests.
- `mvn spring-boot:run`: Launches the application.
- `mvn test`: Executes unit tests.
- `mvn package`: Creates a runnable JAR in the `target/` directory.

## ⚙️ Environment Variables
The application primarily uses `application.properties` for configuration. You can override these using environment variables:
- `SPRING_DATASOURCE_URL`: JDBC URL for MySQL (Default: `jdbc:mysql://localhost:3306/ecommerce_db`)
- `SPRING_DATASOURCE_USERNAME`: MySQL username (Default: `root`)
- `SPRING_DATASOURCE_PASSWORD`: MySQL password (Default: `diego123`)
- `SERVER_PORT`: Port on which the application runs (Default: `8080`)

## 🧪 Testing
The project includes unit tests for core services (e.g., `CartServiceTest`).
To run all tests:
```bash
mvn test
```
*Note: Some tests are located in `src/main/java` instead of the standard `src/test/java` directory.*

## 📁 Project Structure
```text
src/main/java/com/ecommerce/
├── config/             # Security and App configuration
├── controller/         # Web controllers (MVC)
├── entity/             # JPA Entities (Database models)
├── repository/         # Spring Data JPA repositories
└── service/            # Business logic and services
src/main/resources/
├── static/css/         # Static assets (CSS)
├── templates/          # Thymeleaf HTML templates
└── application.properties # Main configuration file
```

## 📄 License
TODO: Add license information (e.g., MIT, Apache 2.0). 
Currently, no LICENSE file exists in the repository.