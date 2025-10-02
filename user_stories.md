# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

ADMIN

User Story 1

Title:
As an admin, I want to log into the portal with my username and password, so that I can securely manage the platform.

Acceptance Criteria:

Admin must enter a valid username and password.

If credentials are correct, the system grants access to the admin dashboard.

If credentials are invalid, the system displays an error message.

Priority: High
Story Points: 3
Notes:

Consider adding account lockout after multiple failed attempts.

User Story 2

Title:
As an admin, I want to log out of the portal, so that I can protect system access and prevent unauthorized use.

Acceptance Criteria:

Admin can click the logout button from any page.

The system ends the current session and redirects to the login page.

The session cannot be reused to access protected resources.

Priority: High
Story Points: 2
Notes:

Add automatic logout after a period of inactivity.

User Story 3

Title:
As an admin, I want to add doctors to the portal, so that they can register patients, manage appointments, and use the system effectively.

Acceptance Criteria:

Admin can open the "Add Doctor" form.

Required fields (name, email, specialty, etc.) must be filled before saving.

A success message is displayed when the doctor is added successfully.

Priority: High
Story Points: 5
Notes:

Validate email format and uniqueness before saving.

User Story 4

Title:
As an admin, I want to delete a doctor’s profile from the portal, so that I can remove inactive or unauthorized users.

Acceptance Criteria:

Admin can select a doctor’s profile from the doctor list.

The system prompts for confirmation before deletion.

Deleted doctors no longer appear in the doctor list or login system.

Priority: Medium
Story Points: 3
Notes:

Prevent deletion if the doctor still has active appointments.

User Story 5

Title:
As an admin, I want to run a stored procedure in the MySQL CLI to get the number of appointments per month, so that I can track usage statistics.

Acceptance Criteria:

Stored procedure can be executed via MySQL CLI.

The procedure returns the count of appointments grouped by month.

Output is accurate and reflects data from the appointments table.

Priority: Medium
Story Points: 4
Notes:

Consider automating this report inside the admin dashboard in the future.

DOCTOR

User Story 1

Title:
As a doctor, I want to log into the portal, so that I can manage my appointments.

Acceptance Criteria:

Doctor must enter valid credentials.

Successful login redirects to the doctor dashboard.

Invalid credentials show an error message.

Priority: High
Story Points: 2
Notes:

Consider adding multi-factor authentication for added security.

User Story 2

Title:
As a doctor, I want to log out of the portal, so that I can protect my data.

Acceptance Criteria:

Doctor can log out from any page.

The system ends the session and redirects to the login page.

Session cannot be reused after logout.

Priority: High
Story Points: 2
Notes:

Add auto-logout after inactivity for data security.

User Story 3

Title:
As a doctor, I want to view my appointment calendar, so that I can stay organized.

Acceptance Criteria:

Calendar view displays upcoming appointments with patient names, dates, and times.

Doctors can view appointments by day, week, or month.

Past appointments are marked as completed or archived.

Priority: High
Story Points: 5
Notes:

Future upgrade: add color-coded statuses (confirmed, canceled, completed).

User Story 4

Title:
As a doctor, I want to mark my unavailability, so that patients only see available slots when booking.

Acceptance Criteria:

Doctor can select dates and times when unavailable.

The system hides unavailable slots from patients during booking.

Confirmation is shown once unavailability is saved.

Priority: High
Story Points: 4
Notes:

Add recurring unavailability (e.g., weekends, fixed leave days).

User Story 5

Title:
As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information.

Acceptance Criteria:

Doctor can edit specialization, contact details, and profile picture.

Updates are reflected immediately in the patient-facing doctor list.

Invalid formats (e.g., incorrect phone number) show validation errors.

Priority: Medium
Story Points: 3
Notes:

Consider admin approval for profile changes in future.

Patient

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

