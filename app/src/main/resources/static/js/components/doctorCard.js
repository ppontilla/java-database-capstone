/*
Import the overlay function for booking appointments from loggedPatient.js

  Import the deleteDoctor API function to remove doctors (admin role) from docotrServices.js

  Import function to fetch patient details (used during booking) from patientServices.js

  Function to create and return a DOM element for a single doctor card
    Create the main container for the doctor card
    Retrieve the current user role from localStorage
    Create a div to hold doctor information
    Create and set the doctor’s name
    Create and set the doctor's specialization
    Create and set the doctor's email
    Create and list available appointment times
    Append all info elements to the doctor info container
    Create a container for card action buttons
    === ADMIN ROLE ACTIONS ===
      Create a delete button
      Add click handler for delete button
     Get the admin token from localStorage
        Call API to delete the doctor
        Show result and remove card if successful
      Add delete button to actions container
   
    === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
      Create a book now button
      Alert patient to log in before booking
      Add button to actions container
  
    === LOGGED-IN PATIENT ROLE ACTIONS === 
      Create a book now button
      Handle booking logic for logged-in patient   
        Redirect if token not available
        Fetch patient data with token
        Show booking overlay UI with doctor and patient info
      Add button to actions container
   
  Append doctor info and action buttons to the car
  Return the complete doctor card element
*/
// Import necessary functions
import { showBookingOverlay } from './loggedPatient.js';      // Overlay for booking appointments
import { deleteDoctor } from './doctorServices.js';           // API function to delete doctors (admin)
import { fetchPatientDetails } from './patientServices.js';   // Fetch patient details for booking

/**
 * Creates and returns a DOM element representing a single doctor card,
 * with role-based actions like delete or book appointment.
 *
 * @param {Object} doctor - Doctor object with properties: id, name, specialization, email, availableTimes (array)
 * @returns {HTMLElement} - The constructed doctor card element
 */
function createDoctorCard(doctor) {
  // Create main container div for doctor card
  const card = document.createElement('div');
  card.classList.add('doctor-card');
  card.dataset.doctorId = doctor.id;

  // Get current user role from localStorage
  const role = localStorage.getItem('userRole');
  const token = localStorage.getItem('token');

  // Doctor info container
  const infoDiv = document.createElement('div');
  infoDiv.classList.add('doctor-info');

  // Doctor Name
  const nameEl = document.createElement('h3');
  nameEl.textContent = doctor.name;
  infoDiv.appendChild(nameEl);

  // Doctor Specialization
  const specEl = document.createElement('p');
  specEl.textContent = `Specialization: ${doctor.specialization}`;
  infoDiv.appendChild(specEl);

  // Doctor Email
  const emailEl = document.createElement('p');
  emailEl.textContent = `Email: ${doctor.email}`;
  infoDiv.appendChild(emailEl);

  // Available Appointment Times
  const timesEl = document.createElement('p');
  timesEl.textContent = 'Available Times: ' + (doctor.availableTimes.join(', ') || 'No available times');
  infoDiv.appendChild(timesEl);

  // Action buttons container
  const actionsDiv = document.createElement('div');
  actionsDiv.classList.add('doctor-card-actions');

  // === ADMIN ROLE ACTIONS ===
  if (role === 'admin') {
    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = 'Delete Doctor';
    deleteBtn.classList.add('delete-doctor-btn');

    deleteBtn.addEventListener('click', async () => {
      if (!token) {
        alert('You must be logged in as admin to perform this action.');
        return;
      }
      const confirmed = confirm(`Are you sure you want to delete Dr. ${doctor.name}?`);
      if (!confirmed) return;

      try {
        const result = await deleteDoctor(doctor.id, token);
        if (result.success) {
          alert(`Doctor ${doctor.name} deleted successfully.`);
          card.remove();  // Remove card from DOM
        } else {
          alert(`Failed to delete doctor: ${result.message || 'Unknown error'}`);
        }
      } catch (error) {
        console.error('Error deleting doctor:', error);
        alert('Error deleting doctor, please try again later.');
      }
    });

    actionsDiv.appendChild(deleteBtn);
  }

  // === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
  else if (role === 'patient' || !role) {
    const bookBtn = document.createElement('button');
    bookBtn.textContent = 'Book Now';
    bookBtn.classList.add('book-now-btn');

    bookBtn.addEventListener('click', () => {
      alert('Please log in or sign up to book an appointment.');
    });

    actionsDiv.appendChild(bookBtn);
  }

  // === LOGGED-IN PATIENT ROLE ACTIONS ===
  else if (role === 'loggedPatient') {
    const bookBtn = document.createElement('button');
    bookBtn.textContent = 'Book Now';
    bookBtn.classList.add('book-now-btn');

    bookBtn.addEventListener('click', async () => {
      if (!token) {
        alert('Session expired. Please log in again.');
        window.location.href = '/pages/patientLogin.html';  // Redirect to login page
        return;
      }

      try {
        const patient = await fetchPatientDetails(token);
        if (!patient) {
          alert('Failed to retrieve patient information.');
          return;
        }

        // Show booking overlay with doctor and patient info
        showBookingOverlay(doctor, patient);
      } catch (error) {
        console.error('Error fetching patient details:', error);
        alert('Error occurred. Please try again.');
      }
    });

    actionsDiv.appendChild(bookBtn);
  }

  // Append info and actions to card
  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}

export { createDoctorCard };
