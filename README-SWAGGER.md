# Swagger / OpenAPI

This project uses `springdoc-openapi` (already in `pom.xml`) to expose OpenAPI documentation and Swagger UI.

How to run the app and access Swagger UI:

1. Run the application (from the project root):

```bash
mvn spring-boot:run
```

2. Open the Swagger UI in your browser:

- http://localhost:8080/swagger-ui/index.html

3. Open the raw OpenAPI JSON/YAML:

- OpenAPI JSON: http://localhost:8080/v3/api-docs

Notes:
- If your application runs on a different port, adjust the URLs accordingly.
- `springdoc-openapi` auto-discovers controllers and annotations.
- The `OpenApiConfig` class provides title, description and contact metadata.

If Swagger UI doesn't show your endpoints:
- Ensure controllers are annotated with Spring MVC annotations (@RestController / @Controller and @RequestMapping).
- Restart the app after adding new controllers or DTOs.
- If you use Spring Security, you may need to permit access to `/v3/api-docs/**` and `/swagger-ui/**` paths in your security configuration.

