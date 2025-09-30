1 A user (Admin, Doctor, or Patient) initiates an action, such as opening the Admin Dashboard, booking an appointment, or viewing prescriptions.

2 The request is sent either to a Thymeleaf-based MVC controller (for dashboards) or to a REST controller (for API endpoints).

3 The controller validates and interprets the request, then forwards it to the appropriate service layer method.

4 The service layer applies business logic (e.g., verifying user roles, checking appointment availability, or processing prescription details).

5 Depending on the operation, the service layer calls the corresponding repository—either JPA repositories for MySQL data or MongoDB repositories for prescription documents.

6 The repository interacts with the database, retrieves or updates records, and returns results to the service layer.

7 The service layer passes the processed result back to the controller, which then renders a Thymeleaf template for dashboards or sends a JSON response for REST API calls.