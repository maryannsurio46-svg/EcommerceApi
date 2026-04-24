# Ecommerce API - Product Catalog Service

## Project Overview

This is a RESTful API backend for an e-commerce product catalog. The API provides complete CRUD (Create, Read, Update, Delete) operations for managing products, including filtering capabilities by category, price, and name.

### Features
- Full CRUD operations for products
- In-memory data storage (no database required)
- Input validation for all requests
- Proper HTTP status codes
- Global exception handling
- Filter products by category, price, or name

### Technologies Used
- *Spring Boot* (v4.0.5)
- *Java* 21
- *Maven* (build tool)
- *Lombok* (reduces boilerplate code)

---

## Setup Instructions

### Prerequisites
- Java 22 or higher installed
- Maven (or use the included Maven wrapper)

### How to Run the Application

#### Option 1: Using Maven (if installed)
# Run the application
mvn spring-boot:run

# Option 3:Using VS Code

1. Open the project in VS Code
2. Navigate to EcommerceApiApplication.java
3. Click the Run button above the main method

Verify the Application is Running
Open your browser and navigate to:
http://localhost:8080/api/v1/products

You should see a JSON response containing the list of products.

#### Option 1: Using Maven Wrapper (Recommended)
---
```bash
# Clone the repository
git clone https://github.com/maryannsuri046-svg/EcommerceApi.git

# Navigate to project directory
cd EcommerceApi

# Run the application
.\mvnw.cmd spring-boot:run


# API END POINT REFERENCES

| Method | Path | Description | Expected Response |
|--------|      |-------------|-------------------|
| GET    | /api/v1/products | Retrieve all products | 200 OK (JSON array) |
| GET    | /api/v1/products/{id} | Retrieve product by ID | 200 OK (JSON object), 404 Not Found |
| GET    | /api/v1/products/filter?filterType=category&filterValue={value} | Filter products by category | 200 OK (JSON array), 400 Bad Request |
| GET    | /api/v1/products/filter?filterType=price&filterValue={value} | Filter products by max price    | 200 OK (JSON array), 400 Bad Request |
| GET    | /api/v1/products/filter?filterType=name&filterValue={value} | Filter products by name search   | 200 OK (JSON array), 400 Bad Request |
| POST   | /api/v1/products| Create a new product | 201 Created (JSON object), 400 Bad Request  |
| PUT    | /api/v1/products/{id}| Full product update (replace entire product) | 200 OK (JSON object), 404 Not Found |
| PATCH  | /api/v1/products/{id}| Partial product update (update specific fields) | 200 OK (JSON object), 404 Not Found |
| DELETE | /api/v1/products/{id}| Delete a product | 204 No Content, 404 Not Found |



SAMPLE REQUEST/RESPONSE EXAMPLES

GET All Products
#curl http://localhost:8080/api/v1/products

#[
 # {"id": 1, "name": "Gaming Laptop", "price": 1299.99, "category": "Electronics"},
  #{"id": 2, "name": "Wireless Mouse", "price": 29.99, "category": "Accessories"}
#]

Filter Products by Price (under $100)
#curl "http://localhost:8080/api/v1/products/filter?filterType=price&filterValue=100"

#[
 # {"id": 2, "name": "Wireless Mouse", "price": 29.99},
  #{"id": 3, "name": "Mechanical Keyboard", "price": 89.99}
#

Filter Products by Name
#curl "http://localhost:8080/api/v1/products/filter?filterType=name&filterValue=laptop"

#[{"id": 1, "name": "Gaming Laptop", "price": 1299.99}]


POST - Create New Product
#curl -X POST http://localhost:8080/api/v1/products -H "Content-Type: application/json" -d "{\"name\":\"Smart Watch\",\"price\":199.99,\"category\":\"Electronics\"}"{"id": 11, "name": "Smart Watch", "price": 199.99, "category": "Electronics"}


PUT - Update Product (Full Update)
#curl -X PUT http://localhost:8080/api/v1/products/1 -H "Content-Type: application/json" -d "{\"id\":1,\"name\":\"Gaming Laptop Pro\",\"price\":2499.99,\"category\":\"Electronics\"}"{"id": 1, "name": "Gaming Laptop Pro", "price": 2499.99, "category": "Electronics"}


PATCH - Partial Update
#curl -X PATCH http://localhost:8080/api/v1/products/1 -H "Content-Type: application/json" -d "{\"price\":2299.99}"{"id": 1, "name": "Gaming Laptop Pro", "price": 2299.99, "category": "Electronics"}


DELETE - Delete Product
#curl -X DELETE http://localhost:8080/api/v1/products/11



Error Response Example (404 Not Found)
#curl http://localhost:8080/api/v1/products/999{"timestamp": "2026-04-24T...", "status": 404, "error": "Not Found", "message": "Product not found with id: 999"}