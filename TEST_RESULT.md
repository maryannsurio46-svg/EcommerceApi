# API Test Log - Task 6.2 & 6.3

## Test Date: April 23, 2026
## Tester: Mary Ann Surio
## Base URL: http://localhost:8080/api/v1/products


## Validation Test Results

### 1. Product Name Validation

| Test Case | Request | Expected Result | Actual Result | Status |
|-----------|---------|-----------------|---------------|--------|
| Missing name | POST {"price":99.99,"category":"Electronics"} | 400 Bad Request - "Product name is required" | 400 Bad Request | ✅ PASS |
| Name too short (2 chars) | POST {"name":"ab","price":99.99,"category":"Electronics"} | 400 Bad Request - "Product name must be at least 3 characters" | 400 Bad Request | ✅ PASS |
| Valid name (3+ chars) | POST {"name":"Test Product","price":99.99,"category":"Electronics"} | 201 Created | 201 Created | ✅ PASS |

### 2. Price Validation

| Test Case | Request | Expected Result | Actual Result | Status |
|-----------|---------|-----------------|---------------|--------|
| Missing price | POST {"name":"Test","category":"Electronics"} | 400 Bad Request - "Product price is required" | 400 Bad Request | ✅ PASS |
| Negative price | POST {"name":"Test","price":-10,"category":"Electronics"} | 400 Bad Request - "Product price must be positive" | 400 Bad Request | ✅ PASS |
| Zero price | POST {"name":"Test","price":0,"category":"Electronics"} | 400 Bad Request - "Product price must be positive" | 400 Bad Request | ✅ PASS |
| Valid price | POST {"name":"Test","price":99.99,"category":"Electronics"} | 201 Created | 201 Created | ✅ PASS |

### 3. Category Validation

| Test Case | Request | Expected Result | Actual Result | Status |
|-----------|---------|-----------------|---------------|--------|
| Missing category | POST {"name":"Test","price":99.99} | 400 Bad Request - "Product category is required" | 400 Bad Request | ✅ PASS |
| Valid category | POST {"name":"Test","price":99.99,"category":"Electronics"} | 201 Created | 201 Created | ✅ PASS |

### 4. Stock Quantity Validation

| Test Case | Request | Expected Result | Actual Result | Status |
|-----------|---------|-----------------|---------------|--------|
| Negative stock | POST {"name":"Test","price":99.99,"category":"Electronics","stockQuantity":-5} | 400 Bad Request - "Stock quantity cannot be negative" | 400 Bad Request | ✅ PASS |
| Missing stock | POST {"name":"Test","price":99.99,"category":"Electronics"} | 201 Created (defaults to 0) | 201 Created with stockQuantity=0 | ✅ PASS |
| Valid stock | POST {"name":"Test","price":99.99,"category":"Electronics","stockQuantity":10} | 201 Created | 201 Created | ✅ PASS |


## Sample API Call Screenshots

### Successful POST Request (201 Created)



## Test Results(screenshots)

### 1. Product name(required, minimum lenght)✅
### (![alt text](<product name(required. minimum lenght).png>))

### 2. Price(must be positive number)✅
### ![alt text](<Price(must be positive number).png>)

### 3. Category(required)✅
### c:\Users\acer\Pictures\Screenshots\category.png

### 3. Stock Quantity(most be non negative)✅
### ![alt text](<Stock Quantity.png>)

