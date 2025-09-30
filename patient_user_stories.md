User Story 1

Title:
As a patient, I want to view a list of doctors without logging in, so that I can explore my options before registering.

Acceptance Criteria:

The system displays a public list of doctors with basic details (name, specialty).

Patients do not need to log in to access this page.

Search and filter options (e.g., by specialty) are available.

Priority: High
Story Points: 3
Notes:

Sensitive information (like contact details) should not be visible without login.

User Story 2

Title:
As a patient, I want to sign up using my email and password, so that I can book appointments.

Acceptance Criteria:

Patient must enter a valid email and password.

The system validates that the email is unique.

Upon successful signup, the patient can log into the portal.

Priority: High
Story Points: 3
Notes:

Add email verification step if required.

User Story 3

Title:
As a patient, I want to log into the portal, so that I can manage my bookings.

Acceptance Criteria:

Patient must enter valid credentials.

Successful login redirects the patient to their dashboard.

Invalid credentials display an appropriate error message.

Priority: High
Story Points: 2
Notes:

Consider adding password reset functionality later.

User Story 4

Title:
As a patient, I want to log out of the portal, so that I can secure my account.

Acceptance Criteria:

Patient can click logout from the dashboard.

The system ends the session and redirects to the login page.

Session cannot be reused for further access.

Priority: High
Story Points: 2
Notes:

Add auto-logout after inactivity for extra security.

User Story 5

Title:
As a patient, I want to book an hour-long appointment with a doctor, so that I can consult about my health concerns.

Acceptance Criteria:

Patient can select a doctor and choose a date/time.

The system prevents booking overlapping time slots.

A confirmation message is shown once the appointment is successfully booked.

Priority: High
Story Points: 5
Notes:

Ensure doctors cannot be double-booked.