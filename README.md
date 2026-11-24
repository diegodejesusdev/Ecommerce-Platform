# 🛒 E-commerce Platform

Tienda en línea monolítica construida con **Java 17 + Spring Boot 3.2** siguiendo el patrón **MVC**, con vistas renderizadas en servidor (**Thymeleaf**) y persistencia en **MySQL**. Cubre el flujo completo de compra: catálogo, carrito, checkout con pago, y ciclo de vida del pedido.

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.2-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=thymeleaf&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)

## ¿Qué hace?

**Catálogo**
- Lista de productos paginada y ordenada por nombre, con **búsqueda por nombre** y **filtro por categoría**.
- Página de detalle de cada producto con su stock disponible.

**Carrito**
- Agregar, actualizar cantidades y quitar productos; el carrito se guarda en base de datos por usuario.
- **Valida el stock** antes de agregar o aumentar cantidades y muestra mensajes flash de éxito o error.

**Checkout y pagos**
- Convierte el carrito en un pedido con dirección de envío y **descuenta el stock** de cada producto.
- Procesa el pago mediante una **abstracción de pasarelas** (`PaymentGateway`): Stripe, PayPal y contra entrega. Stripe y PayPal son **simulaciones** (generan un ID de transacción); la interfaz está lista para conectar los SDK reales.

**Pedidos**
- Máquina de estados: `PENDING → CONFIRMED → SHIPPED → DELIVERED`, o `CANCELLED` desde `PENDING`/`CONFIRMED`. Cada transición valida el estado actual.
- Al **cancelar**, el stock de los productos se **restaura**.
- "Mis pedidos" con historial paginado y vista de detalle.

**Usuarios y seguridad**
- Login con formulario, contraseñas cifradas con **BCrypt** y roles `ROLE_USER` / `ROLE_ADMIN` (Spring Security 6).
- El catálogo es público; carrito, checkout y pedidos requieren sesión.
- Perfil de usuario con **cambio de contraseña** (verifica la contraseña actual).

**Datos de ejemplo**
- Al primer arranque se crean un usuario administrador y productos de muestra (`DataInitializerService`).

## Arquitectura

```
src/main/java/com/ecommerce/
├── config/       SecurityConfig, PasswordEncoderConfig, DataInitializer
├── controller/   Home, Product, Cart, Order, Profile, Login (Spring MVC)
├── entity/       User, Role, Product, Category, Cart, CartItem, Order, OrderItem
├── payment/      PaymentGateway + Stripe / PayPal / CashOnDelivery (patrón Strategy)
├── repository/   Repositorios Spring Data JPA
└── service/      CartService, OrderService, PaymentService, CustomUserDetailsService
src/main/resources/
├── templates/    Vistas Thymeleaf (catálogo, carrito, checkout, pedidos, perfil)
├── static/css/   Estilos
└── data.sql      Roles y usuario inicial
```

## Rutas principales

| Ruta | Descripción | Acceso |
|---|---|---|
| `/`, `/products`, `/products/{id}` | Inicio, catálogo y detalle | Público |
| `/login` | Inicio de sesión | Público |
| `/cart`, `/cart/add`, `/cart/update`, `/cart/remove` | Carrito | Autenticado |
| `/orders/checkout` | Formulario y envío del pedido con pago | Autenticado |
| `/orders/my-orders`, `/orders/{id}` | Historial y detalle | Autenticado |
| `/orders/list`, `/orders/{id}/confirm\|ship\|deliver\|cancel` | Gestión de pedidos | Autenticado |
| `/profile`, `/profile/edit`, `/profile/change-password` | Perfil | Autenticado |

## Cómo ejecutarlo

Requisitos: JDK 17+, Maven 3.8+ y MySQL 8 en ejecución.

```bash
git clone https://github.com/diegodejesusdev/Ecommerce-Platform.git
cd Ecommerce-Platform

# La base ecommerce_db se crea sola (createDatabaseIfNotExist=true).
# Define tus credenciales de MySQL:
export DB_USERNAME=root
export DB_PASSWORD=tu_password
# Opcional: DB_URL y SERVER_PORT

mvn spring-boot:run
```

Abre `http://localhost:8080`. El usuario administrador de ejemplo se muestra en el log del primer arranque.

```bash
mvn test      # pruebas unitarias (CartServiceTest)
mvn package   # genera el JAR en target/
```

## Pendientes conocidos

- No hay página de registro: los usuarios se crean con los datos iniciales.
- Las acciones de gestión de pedidos (confirmar, enviar, entregar, listar todos) solo exigen sesión iniciada; falta restringirlas al rol `ADMIN`.
- Stripe y PayPal están simulados.
- Pruebas solo para `CartService`; faltan pruebas de integración de pedidos y pagos.

## Licencia

MIT
