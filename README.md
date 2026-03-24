# E-commerce Platform

A robust, full-stack E-commerce solution built with the Spring Boot 3 ecosystem. This application provides a platform for browsing products, managing a shopping cart, and secure user authentication.

## 🛡️ Key Features
- **Secure Authentication**: Role-Based Access Control (RBAC) using Spring Security.
- **Product Catalog**: Dynamic browsing with categories, product details, and stock management.
- **Shopping Cart**: Real-time cart management for logged-in users.
- **Order Management**: Checkout process and order tracking (ready for extension).
- **Responsive UI**: Modern interface built with Thymeleaf, HTML5, and CSS3.
- **Auto-Initialization**: Automatic seeding of sample data (admin user and products) on the first run.

## 🛠️ Tech Stack
- **Language**: Java 17
- **Framework**: Spring Boot 3.2.5
- **Data Access**: Spring Data JPA (Hibernate)
- **Security**: Spring Security 6 with BCrypt password encoding
- **Database**: MySQL 8.0+
- **Template Engine**: Thymeleaf
- **Frontend**: HTML5, CSS3
- **Package Manager / Build Tool**: Maven 3.8+

## 🚀 Getting Started

### Prerequisites
- **JDK 17** or higher.
- **MySQL Server** 8.0+.
- **Maven** 3.8+ (optional if using the included `./mvnw`).

### Installation & Setup
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/ecommerce-platform.git
   cd "E-commerce Platform"
   ```

2. **Database Configuration**:
   - The application is configured to create the database automatically if it does not exist (`createDatabaseIfNotExist=true`).
   - Ensure your MySQL server is running.
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
   - **Using Maven**:
     ```bash
     mvn spring-boot:run
     ```
   - **Using JAR**:
     ```bash
     java -jar target/ecommerce-platform-1.0.0-SNAPSHOT.jar
     ```

5. **Access the platform**:
   - URL: `http://localhost:8080`
   - **Default Admin Credentials**:
     - Email: `admin@example.com`
     - Password: `admin123`

## 📜 Scripts & Commands
- `mvn clean install`: Cleans, builds the project, and runs tests.
- `mvn spring-boot:run`: Launches the application using the Spring Boot Maven plugin.
- `mvn test`: Executes unit and integration tests.
- `mvn package`: Packages the application into a runnable JAR in the `target/` directory.

## ⚙️ Environment Variables
The application primarily uses `src/main/resources/application.properties`. You can override these using environment variables:
- `SPRING_DATASOURCE_URL`: JDBC URL for MySQL (Default: `jdbc:mysql://localhost:3306/ecommerce_db`)
- `SPRING_DATASOURCE_USERNAME`: MySQL username (Default: `root`)
- `SPRING_DATASOURCE_PASSWORD`: MySQL password (Default: `diego123`)
- `SERVER_PORT`: Port on which the application runs (Default: `8080`)

## 🧪 Testing
The project includes unit tests for core services. 
Note: Currently, some tests are located in `src/main/java` (e.g., `CartServiceTest.java`) instead of the standard `src/test/java` directory.

To run all tests:
```bash
mvn test
```

## 📁 Project Structure
```text
src/main/java/com/ecommerce/
├── EcommerceApplication.java # Application Entry Point
├── config/                  # Security, PasswordEncoder, and Data Initialization configs
├── controller/              # Spring MVC Controllers (Web layer)
├── entity/                  # JPA Entities (Database models)
├── repository/              # Spring Data JPA Repositories (Data layer)
└── service/                 # Business Logic and Services
src/main/resources/
├── static/                  # Static assets (CSS, JS, Images)
├── templates/               # Thymeleaf HTML templates
├── application.properties    # Main configuration file
└── data.sql                 # SQL script for data initialization (if used)
```

## 📄 License
- TODO: Add license information (e.g., MIT, Apache 2.0). 
- Currently, no LICENSE file exists in the repository.

## 🏗️ TODOs & Roadmap
- [ ] Move tests from `src/main/java` to `src/test/java`.
- [ ] Implement full order processing logic.
- [ ] Add support for multiple payment gateways.
- [ ] Add a formal `LICENSE` file.