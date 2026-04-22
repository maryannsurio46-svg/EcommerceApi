# HTTP Status Codes Documentation

## Success Status Codes

| Status Code | Description | When it's used | Endpoint |
|-------------|-------------|----------------|----------|
| *200 OK* | Request succeeded | When successfully retrieving or updating products | GET, PUT, PATCH |
| *201 Created* | Resource created | When a new product is successfully added | POST |
| *204 No Content* | Request succeeded, no content | When a product is successfully deleted | DELETE |

## Error Status Codes

| Status Code | Description | When it's used |
|-------------|-------------|----------------|
| *400 Bad Request* | Invalid request data | Missing fields, invalid price, wrong filter type |
| *404 Not Found* | Resource not found | Product ID doesn't exist |
| *500 Internal Server Error* | Server error | Unexpected server error |

## Why These Status Codes?

### Success Codes:
- *200 OK* - Standard success response for GET and PUT operations
- *201 Created* - Indicates a new resource was created
- *204 No Content* - Appropriate for DELETE operations

### Error Codes:
- *400 Bad Request* - Client sent invalid data
- *404 Not Found* - Requested resource doesn't exist
- *500 Internal Server Error* - Unexpected server-side issue

## Response Format

All error responses follow this JSON format:

```json
{
    "timestamp": "2026-04-22T14:30:00Z",
    "status": 404,
    "error": "Not Found",
    "message": "Product not found with id: 999"
}