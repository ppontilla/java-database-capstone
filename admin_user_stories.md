
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


