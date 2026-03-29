# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
mvn clean install        # Build and run tests
mvn spring-boot:run      # Run the application
mvn test                 # Run tests
mvn package              # Package as JAR
java -jar target/ecommerce-platform-1.0.0-SNAPSHOT.jar  # Run from JAR
```

Default access: `http://localhost:8080`
- Admin: `admin@example.com` / `admin123`

## Architecture

**Layered Spring Boot 3.2.5 architecture:**

```
EcommerceApplication.java (entry point)
├── controller/      # Web layer (Spring MVC) - HomeController, ProductController, CartController, OrderController, ProfileController, LoginController
├── service/         # Business logic - CartService, OrderService, PaymentService, CustomUserDetailsService, DataInitializerService
├── repository/      # Data access (Spring Data JPA)
├── entity/          # JPA entities - User, Role, Product, Category, Cart, CartItem, Order, OrderItem
├── config/          # SecurityConfig, PasswordEncoderConfig, DataInitializer
└── payment/         # Payment gateway abstraction - PaymentGateway interface with StripePaymentGateway, PayPalPaymentGateway, CashOnDeliveryGateway
```

**Key patterns:**
- **Security**: Spring Security 6 with RBAC (ROLE_ADMIN, ROLE_USER). Public routes: `/`, `/products`, `/login`, `/register`. Authenticated: `/cart/**`, `/checkout/**`, `/orders/**`. Admin-only: `/admin/**`
- **Payment**: Strategy pattern via `PaymentGateway` interface for pluggable providers
- **Cart**: Service-layer business logic with stock validation before any quantity change
- **Data**: Auto-seeding on startup via `DataInitializer` if database is empty

## Configuration

**application.properties:**
- MySQL on `localhost:3306/ecommerce_db` (auto-created)
- Hibernate: `ddl-auto=update`
- Default credentials: `root` / `diego123`

**Environment overrides:**
- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- `SERVER_PORT`

## Testing

Tests use JUnit 5 + Mockito. Test location: `src/test/java/com/ecommerce/service/CartServiceTest.java`

Run single test class:
```bash
mvn test -Dtest=CartServiceTest
```
