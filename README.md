# 🚀 Proyecto Spring Boot - Seguridad y Manejo de Sesiones

Este proyecto es una API REST construida con **Spring Boot** que implementa **autenticación**, **autorización** y **manejo de sesiones** utilizando **Spring Security**.  
🔐 Permite proteger rutas, manejar sesiones concurrentes, controlar accesos y obtener información del usuario autenticado.

---

## ✨ Características principales

- 🔒 Autenticación mediante formulario de login.
- 🛡️ Protección de rutas según autenticación.
- 👥 Manejo de sesiones concurrentes (una sesión por usuario).
- ⏳ Redirección automática al expirar la sesión.
- 📋 Registro y seguimiento de sesiones activas con `SessionRegistry`.
- 🚪 Logout manual con invalidación de sesión y eliminación de cookies.
- 📬 Acceso a información de la sesión del usuario autenticado.

---

## 📌 Endpoints disponibles

| Método | Ruta              | Autenticación | Descripción                                       |
|--------|-------------------|---------------|---------------------------------------------------|
| GET    | `/v1/index`       | ✅ Sí         | Respuesta protegida, requiere login              |
| GET    | `/v1/index2`      | ❌ No         | Acceso público                                    |
| GET    | `/v1/session`     | ✅ Sí         | Devuelve información del usuario y la sesión     |
| GET    | `/logout`         | ✅ Sí         | Cierra sesión y redirige a `/login?logout=true`  |

---

## 🔐 Seguridad

- ✅ **URL permitida sin login**: `/v1/index2`
- 🔐 **Resto de las rutas**: requieren autenticación.
- 🎯 **Login exitoso**: redirige a `/v1/session`.
- ⌛ **Sesión expirada**: redirige a `/login?expired=true`.
- 🔚 **Logout**: elimina la sesión y redirige a `/login?logout=true`.

---

## 🧰 Tecnologías usadas

- ☕ Java 17+
- ⚙️ Spring Boot 3+
- 🔐 Spring Security
- 📦 Maven

---

## 🗂️ Estructura destacada

- `SecurityConfig.java`: configuración de seguridad (filtros, login, logout, sesiones).
- `CustomerController.java`: controlador con endpoints seguros y públicos.
- Beans:
  - `SessionRegistry`: controla sesiones activas.
  - `ConcurrentSessionFilter`: gestiona expiración de sesión.
  - `HttpSessionEventPublisher`: publica eventos de sesión para control interno.

---

## ▶️ Cómo ejecutar

1. 🧬 Cloná el repositorio:
   ```bash
   git clone https://github.com/simon-zubigaray/Spring-Boot-Seguridad-y-Manejo-de-Sesiones
   cd Spring-Boot-Seguridad-y-Manejo-de-Sesiones
   ```

2. 🚀 Ejecutá la aplicación con Maven o desde tu IDE:
   ```bash
   ./mvnw spring-boot:run
   ```

3. 🌐 Accedé a:
   - `http://localhost:8080/v1/index2` (público)
   - `http://localhost:8080/login` (login)
   - `http://localhost:8080/v1/index` (protegido)

---

## 📩 Contacto

Para consultas o colaboración, podés escribirme a:  
**zubigarayjuansimon@gmail.com**