# Customer-service

## 1. **Retrieve All Customers**
#### `GET /customers`
Fetches all customer records.

- **Summary**: Retrieves all customer records from the system. Returns an empty list if no customers are found.
- **Response**:
  - **200 OK**: Successfully retrieved the list of customers.
  - **204 No Content**: No content available (empty list).

#### Example Request:
```http
GET /customers
```
#### Example Response:
```json
[
  {
    "id": "101",
    "type": "personal",
    "name": "John Doe",
    "phone": "+1234567890",
    "email": "john.doe@example.com",
    "address": "123 Main St",
    "dateOfBirth": "1985-04-12"
  },
  {
    "id": "102",
    "type": "business",
    "name": "Jane Smith",
    "phone": "+0987654321",
    "email": "jane.smith@example.com",
    "address": "456 Elm St",
    "dateOfBirth": "1990-08-22"
  }
]
```

## 2. **Retrieve a Specific Customer**
#### `GET /customers/{customer_id}`
Fetches a single customer by ID.

- **Summary**: Fetches a single customer by their ID. Returns 404 if the customer is not found.
- **Parameters**:
  - `customer_id` (Path Variable): The ID of the customer to retrieve.
- **Response**:
  - **200 OK**: Successfully retrieved the customer.
  - **404 Not Found**: Customer not found.

#### Example Request:
```http
GET /customers/101
```
#### Example Response:
```json
{
  "id": "101",
  "type": "personal",
  "name": "John Doe",
  "phone": "+1234567890",
  "email": "john.doe@example.com",
  "address": "123 Main St",
  "dateOfBirth": "1985-04-12"
}
```

## 3. **Create a New Customer**
#### `POST /customers`
Creates a new customer and returns the newly created resource.

- **Summary**: Creates a new customer and returns the created customer along with a location header pointing to the resource.
- **Request Body**:
  - The `Customer` object to create, including properties such as `type`, `name`, `phone`, `email`, etc.
- **Response**:
  - **201 Created**: Successfully created the customer.
  - **404 Not Found**: Creation failed.
```http
POST /customers
```
#### Example Request:
```json
{
  "type": "business",
  "name": "Tech Corp",
  "phone": "+1239876543",
  "email": "contact@techcorp.com",
  "address": "456 Business Rd",
  "dateOfBirth": "2000-01-01"
}
```
### Example Response:
```json
{
  "id": "102",
  "type": "business",
  "name": "Tech Corp",
  "phone": "+1239876543",
  "email": "contact@techcorp.com",
  "address": "456 Business Rd",
  "dateOfBirth": "2000-01-01"
}
```

## 4. **Update an Existing Customer**
#### `PUT /customers/{customer_id}`
Updates an existing customer by ID.

- **Summary**: Updates an existing customer by its ID and returns the updated details.
- **Parameters**:
  - `customer_id` (Path Variable): The ID of the customer to update.
- **Request Body**:
  - The `Customer` object containing updated details, including `type`, `name`, `phone`, `email`, etc.
- **Response**:
  - **200 OK**: Successfully updated the customer.
  - **404 Not Found**: Customer not found.

#### Example Request:
```http
PUT /customers/{customer_id}
```
```json
{
  "type": "personal",
  "name": "John Doe",
  "phone": "+1234567890",
  "email": "john.doe@example.com",
  "address": "789 Customer St",
  "dateOfBirth": "1985-05-15"
}
```
### Example Response:
```json
{
  "id": "101",
  "type": "personal",
  "name": "John Doe",
  "phone": "+1234567890",
  "email": "john.doe@example.com",
  "address": "789 Customer St",
  "dateOfBirth": "1985-05-15"
}
```
## 5. **Delete a Customer**
#### `DELETE /customers/{customer_id}`
Deletes a customer by its ID.

- **Summary**: Deletes a customer by its ID.
- **Parameters**:
  - `customer_id` (Path Variable): The ID of the customer to delete.
- **Response**:
  - **204 No Content**: Successfully deleted the customer.
  - **404 Not Found**: Customer not found.

#### Example Request:
```http
DELETE /customers/101
```
#### Example Response:
```json
{}
```



