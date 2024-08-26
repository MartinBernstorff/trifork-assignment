# Healthcare Organization API

This project implements a Spring Boot application that provides an API for healthcare organization data.

## Running the Project

To run the project:

`make run`

Example API requests can be found in `Examples.http`.

## API Endpoints

The application provides two main endpoints:

1. **Lookup Endpoint `/{id}`**: Returns the organization with the given ID.
2. **Traverse Endpoint `/{id}/topmost-parent`**: Finds the top-level parent organization for any given organization.

## Implementation Notes

- The code follows a functional core, imperative shell approach, utilizing Optional for null-like-handling. This enables both a stream- and iterator based approach, highlighted at the Controller level. Traversal was written imperatively rather than recursively, since Java does not have tail-call-optimization.
- Testing includes mocking of the repository layer to ensure proper functionality of the service layer, while avoiding the need for a full Spring Boot application context.

## Areas for Future Improvement

Given more time, potential enhancements could include:

- Additional testing, including smoke/integration tests for endpoints
- Logging
- Metrics and health checks
- Security considerations (e.g., rate limiting, authentication)

I look forward to discussing the implementation details, design choices, and potential improvements during the interview.
