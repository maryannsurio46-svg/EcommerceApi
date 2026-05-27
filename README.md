🛒 Ecommerce API – Spring Boot Security Lab

This project is a Spring Boot REST API for an e-commerce system implementing:

🔐 Session-Based Authentication (Spring Security)
🧾 Bean Validation (DTO validation)
🛡️ Role-Based Access Control
🍪 Cookie-based session handling (JSESSIONID)
📡 RESTful API design with secured endpoints


📌 Tech Stack
Java 21
Spring Boot 3.x
Spring Security
Spring Data JPA
MySQL
Bean Validation (Jakarta Validation)
Maven


🔐 Security Architecture (Session-Based Authentication)
This application uses Spring Security Session-Based Authentication instead of JWT.
How it works:
User logs in via /login with username and password.
Spring Security validates credentials using UserDetailsService.
If successful:
A server-side session is created.
A JSESSIONID cookie is generated.
The browser stores the cookie automatically.
For every request:
The cookie is sent automatically.
Spring Security validates the session.
If session is valid → access granted
If not → request is rejected (401 Unauthorized).


🧾 Validation Rules
All incoming requests are validated using Bean Validation (Jakarta Validation).
👤 User Validation
Field	Rule
username	Not blank, 4–20 characters
password	Not blank, minimum 8 characters
role	Required


📦 Product Validation (if applicable)
Field	Rule
name	Not blank
price	Must be positive
quantity	Must be zero or positive 


❗ Validation Error Response
{
  "timestamp": "2026-05-26T10:30:00",
  "status": 400,
  "errors": [
    "username: Username is required",
    "password: Password must be at least 8 characters"
  ]
}


📡 API Reference
🔐 Authentication
Register User
POST /api/auth/register
✔ Public endpoint
✔ Validates input
✔ Password is hashed using BCrypt


Login
POST /login
✔ Spring Security form login
✔ Creates session (JSESSIONID cookie)


Logout
POST /logout
✔ Invalidates session
✔ Deletes JSESSIONID cookie


📦 Orders (Protected)
Create Order
POST /api/v1/orders
✔ Requires authentication
✔ Uses session cookie


Get Orders
GET /api/v1/orders
✔ Requires authentication


🔐 Security Rules Summary
Endpoint	                 Access
/api/auth/register	         Public
/login                       Public
/logout                 	Authenticated
/api/v1/orders/**	        Authenticated


🧠 Code Quality Standards
✅ Security Configuration
Fully commented Spring Security configuration
Session-based authentication enabled
CSRF protection enabled
Logout invalidates session and deletes cookie


✅ Validation Handling
Uses @Valid on request DTOs
Custom validation messages
Global exception handler (@RestControllerAdvice)
Clean JSON error responses


❗ Example Error Response
{
  "status": 400,
  "errors": [
    "username: Username is required",
    "password: Password must be at least 8 characters"
  ]
}


🛠️ Key Features
🔐 Secure session-based authentication
🍪 Cookie-based login (JSESSIONID)
🧾 Input validation with DTOs
🛡️ Role-based access control
⚠️ Global exception handling
📡 REST API with protected endpoints


🧪 Testing Flow (Postman)
POST /api/auth/register
POST /login (store JSESSIONID)
GET /api/v1/orders
Remove cookie → request fails (401)
Re-login → request succeeds


🏁 Conclusion

This project demonstrates:

Secure Spring Boot authentication using sessions
Proper request validation
Clean REST API structure
Production-style error handling