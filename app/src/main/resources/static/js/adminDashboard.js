/*
  This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form


  Attach a click listener to the "Add Doctor" button
  When clicked, it opens a modal form using openModal('addDoctor')


  When the DOM is fully loaded:
    - Call loadDoctorCards() to fetch and display all doctors


  Function: loadDoctorCards
  Purpose: Fetch all doctors and display them as cards

    Call getDoctors() from the service layer
    Clear the current content area
    For each doctor returned:
    - Create a doctor card using createDoctorCard()
    - Append it to the content div

    Handle any fetch errors by logging them


  Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  On any input change, call filterDoctorsOnChange()


  Function: filterDoctorsOnChange
  Purpose: Filter doctors based on name, available time, and specialty

    Read values from the search bar and filters
    Normalize empty values to null
    Call filterDoctors(name, time, specialty) from the service

    If doctors are found:
    - Render them using createDoctorCard()
    If no doctors match the filter:
    - Show a message: "No doctors found with the given filters."

    Catch and display any errors with an alert


  Function: renderDoctorCards
  Purpose: A helper function to render a list of doctors passed to it

    Clear the content area
    Loop through the doctors and append each card to the content area


  Function: adminAddDoctor
  Purpose: Collect form data and add a new doctor to the system

    Collect input values from the modal form
    - Includes name, email, phone, password, specialty, and available times

    Retrieve the authentication token from localStorage
    - If no token is found, show an alert and stop execution

    Build a doctor object with the form values

    Call saveDoctor(doctor, token) from the service

    If save is successful:
    - Show a success message
    - Close the modal and reload the page

    If saving fails, show an error message
*/

// Import required modules
import { openModal } from "../components/modals.js";
import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";

// Attach listener to the "Add Doctor" button
document.getElementById("addDocBtn").addEventListener("click", () => {
  openModal("addDoctor");
});

// Load doctors when DOM is fully loaded
window.addEventListener("DOMContentLoaded", loadDoctorCards);

/**
 * Fetch and display all doctors
 */
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error loading doctors:", error);
  }
}

/**
 * Event listeners for filters and search
 */
document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);

/**
 * Filter doctors based on search bar and dropdown filters
 */
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar").value.trim() || "";
  const time = document.getElementById("filterTime").value || "";
  const specialty = document.getElementById("filterSpecialty").value || "";

  try {
    const filtered = await filterDoctors(name, time, specialty);
    if (filtered && filtered.length > 0) {
      renderDoctorCards(filtered);
    } else {
      document.getElementById("content").innerHTML =
        "<p>No doctors found with the given filters.</p>";
    }
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("Failed to filter doctors. Please try again.");
  }
}

/**
 * Helper function to render a list of doctor cards
 * @param {Array} doctors
 */
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

/**
 * Handle Add Doctor form submission
 * Called when form is submitted in the "Add Doctor" modal
 */
export async function adminAddDoctor(event) {
  event.preventDefault();

  const name = document.getElementById("docName").value.trim();
  const email = document.getElementById("docEmail").value.trim();
  const password = document.getElementById("docPassword").value.trim();
  const phone = document.getElementById("docPhone").value.trim();
  const specialty = document.getElementById("docSpecialty").value.trim();

  // Get all checked availability checkboxes
  const availabilityCheckboxes = document.querySelectorAll('input[name="availability"]:checked');
  const availableTime = Array.from(availabilityCheckboxes).map((checkbox) => checkbox.value);

  const token = localStorage.getItem("token");

  if (!token) {
    alert("Unauthorized! Please log in as admin.");
    return;
  }

  const doctor = {
    name,
    email,
    password,
    phone,
    specialty,
    availableTime,
  };

  try {
    const result = await saveDoctor(doctor, token);

    if (result.success) {
      alert(result.message || "Doctor added successfully!");
      document.getElementById("addDoctorForm").reset(); // Reset the form
      document.getElementById("addDoctorModal").style.display = "none"; // Close the modal
      loadDoctorCards(); // Reload the doctor list
    } else {
      alert(result.message || "Failed to add doctor.");
    }
  } catch (error) {
    console.error("Error saving doctor:", error);
    alert("Something went wrong while adding the doctor.");
  }
}
