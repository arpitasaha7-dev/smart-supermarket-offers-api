# Smart Supermarket Offers API

A Java + Spring Boot + MySQL REST API for supermarket registration and promotional offer discovery. Built around the internship assignment's three required endpoints and expanded with useful filtering, validation, pagination, coupon metadata, and API documentation.

## Requirements
- JDK 17+
- Maven 3.9+
- MySQL 8+

## Run locally
1. Create a MySQL user/database or use the configured local root account.
2. Set environment variables `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`. Do not commit real credentials.
3. Run `mvn spring-boot:run`.
4. Open Swagger UI at `http://localhost:8080/swagger-ui/index.html`.

Default local development admin: `offeradmin` / `ChangeThis123!`. Change this before use. This starter uses in-memory HTTP Basic authentication for demonstration; before production, replace it with persisted users, hashed passwords, proper roles, HTTPS, rate limiting, and token-based authentication. The current endpoints are intentionally public for easy assignment demonstration; configure writes to require an authenticated supermarket owner/admin before production.

## Endpoints
| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/supermarkets` | Register a supermarket |
| GET | `/api/supermarkets` | List supermarkets |
| GET | `/api/supermarkets/{id}` | Get supermarket |
| POST | `/api/supermarkets/{id}/offers` | Create an offer for a supermarket |
| GET | `/api/offers` | Browse currently available offers |
| GET | `/api/offers/{id}` | Get offer details |

### Example: register supermarket
`POST /api/supermarkets`
```json
{
  "name": "Fresh Basket",
  "city": "Kolkata",
  "contactEmail": "offers@freshbasket.example"
}
```

### Example: create offer
`POST /api/supermarkets/1/offers`
```json
{
  "title": "Weekend Rice Offer",
  "description": "Save on selected 5 kg rice packs.",
  "category": "Groceries",
  "originalPrice": 450.00,
  "offerPrice": 375.00,
  "startDate": "2026-10-22",
  "endDate": "2026-10-31",
  "couponCode": "RICE75",
  "usageLimit": 100,
  "active": true
}
```
Dates must be today or later when submitted. Change the example dates if testing later.

### Browse/filter offers
`GET /api/offers?city=Kolkata&category=Groceries&minDiscount=10&search=rice&page=0&size=10&sort=endDate`

- `category`, `city`, `search`, `minDiscount` are optional.
- `page` starts at 0; `size` must be 1–100.
- `sort` supports `endDate`, `offerPrice`, or `createdAt`.
- Only active, in-date offers with remaining usage capacity are returned.
- The response is a Spring Data page containing `content`, pagination metadata, and sorting metadata.

## Database schema
- `supermarkets`: identity, name, city, unique contact email, creation timestamp.
- `offers`: offer details, prices, date range, coupon metadata, usage counters, active flag, and foreign key to a supermarket.
- Relationship: one supermarket can publish many offers (one-to-many).
- Indexes on date range and category improve common browse filters.
- `schema.sql` is a reference schema. Spring Data JPA can create/update the local schema through `ddl-auto=update`; for a production system use versioned migrations (Flyway/Liquibase) and `ddl-auto=validate`.

## Architecture
Controller → Service → Repository → MySQL.
- Controller handles HTTP routes and input validation.
- Service contains business rules and availability filters.
- Repository uses Spring Data JPA and a parameterized JPQL query.
- Global exception handler returns readable JSON errors.
- DTOs should be introduced before production to avoid exposing persistence entities directly.

## Minimum endpoint behavior
1. Register supermarket: required name/city/valid unique email; return 201; duplicate email returns 409.
2. Create offer: supermarket must exist; required title, description, category, valid prices and dates; offer price cannot exceed original price; return 201.
3. Browse offers: return only active offers whose start/end dates include today and whose usage limit has not been reached; allow pagination and optional filters.

## Security approach
- Validate every request and reject invalid values.
- Use parameterized ORM queries; do not concatenate SQL from user input.
- Use unique constraints and foreign keys for data integrity.
- Enforce role-based authorization so only a supermarket owner/admin can create or change its offers.
- Use HTTPS, rate limiting, request-size limits, audit logs, secrets from environment/secret manager, and secure CORS policy in production.
- Use BCrypt for stored passwords; never store plain-text credentials.
- The included HTTP Basic/in-memory admin is a local demonstration only. It is not a production identity system. Public browse endpoints are intentional.

## Front-end design approach
A lightweight React or HTML/CSS/JavaScript client can consume this API:
- Public offers page: search bar, category chips, city filter, discount filter, sort dropdown, paginated offer cards.
- Offer detail: supermarket name, original/discounted price, savings percentage, validity dates and coupon code.
- Supermarket dashboard: registration/login, create/edit/deactivate offer, view usage counts.
- Responsive layout, accessible labels, loading states, empty states, and friendly validation messages.
- Keep frontend separate from the backend; all business rules remain enforced by the API.

## Additional features included or planned
Included in starter: category/city/search/minimum-discount filters, sorting, pagination, computed discount percentage, date-aware availability, coupon metadata, usage-limit availability, duplicate-email protection, OpenAPI Swagger UI, structured validation errors.
Good next enhancements: coupon redemption endpoint with atomic usage counter, favourites, email/push expiry reminders, featured/trending offers, geolocation radius, analytics dashboard, audit log, integration tests, and JWT authentication.

## Known implementation notes
- Coupon code and redemption count are stored, and exhausted offers are hidden, but an atomic coupon redemption endpoint is not implemented in this starter.
- Authentication is demonstration-grade; write endpoints should be restricted by role before production.
- Entity responses are used for simplicity. Add request/response DTOs and explicit field allow-lists before public deployment.
