## MySQL Database Design

Table: patients

id: INT, Primary Key, AUTO_INCREMENT

first_name: VARCHAR(50), NOT NULL

last_name: VARCHAR(50), NOT NULL

dob: DATE, NOT NULL

gender: ENUM('Male', 'Female', 'Other'), NOT NULL

email: VARCHAR(100), UNIQUE, NOT NULL

phone: VARCHAR(15), UNIQUE, NOT NULL

address: TEXT, NULL

Table: doctors

id: INT, Primary Key, AUTO_INCREMENT

first_name: VARCHAR(50), NOT NULL

last_name: VARCHAR(50), NOT NULL

specialization: VARCHAR(100), NOT NULL

email: VARCHAR(100), UNIQUE, NOT NULL

phone: VARCHAR(15), UNIQUE, NOT NULL

clinic_location_id: INT, Foreign Key → clinic_locations(id)

available_from: TIME, NOT NULL

available_to: TIME, NOT NULL

Table: appointments

id: INT, Primary Key, AUTO_INCREMENT

doctor_id: INT, Foreign Key → doctors(id)

patient_id: INT, Foreign Key → patients(id)

appointment_time: DATETIME, NOT NULL

status: INT, NOT NULL, DEFAULT 0

0 = Scheduled, 1 = Completed, 2 = Cancelled

notes: TEXT, NULL

Table: admin

id: INT, Primary Key, AUTO_INCREMENT

username: VARCHAR(50), UNIQUE, NOT NULL

email: VARCHAR(100), UNIQUE, NOT NULL

password_hash: VARCHAR(255), NOT NULL

role: ENUM('admin', 'staff'), NOT NULL, DEFAULT 'staff'

Table: clinic_locations

id: INT, Primary Key, AUTO_INCREMENT

name: VARCHAR(100), NOT NULL

address: TEXT, NOT NULL

phone: VARCHAR(15), NOT NULL

Table: payments

id: INT, Primary Key, AUTO_INCREMENT

appointment_id: INT, Foreign Key → appointments(id)

amount: DECIMAL(10,2), NOT NULL

payment_date: DATETIME, NOT NULL

payment_method: ENUM('cash', 'credit_card', 'insurance'), NOT NULL

status: ENUM('paid', 'pending', 'failed'), NOT NULL, DEFAULT 'pending'

Table: prescriptions (Optional but valuable)

id: INT, Primary Key, AUTO_INCREMENT

appointment_id: INT, Foreign Key → appointments(id)

prescribed_by: INT, Foreign Key → doctors(id)

prescription_date: DATE, NOT NULL

notes: TEXT, NOT NULL

