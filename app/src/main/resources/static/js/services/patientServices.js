// Import the API base URL from the configuration
import { API_BASE_URL } from "../config/config.js";

// Define the base endpoint for all patient-related API requests
const PATIENT_API = `${API_BASE_URL}/patient`;

/**
 * Function: patientSignup
 * Purpose: Registers a new patient
 * @param {Object} data - Patient details (name, email, password, etc.)
 * @returns {Promise<{success: boolean, message: string}>}
 */
export async function patientSignup(data) {
  try {
    const response = await fetch(`${PATIENT_API}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(result.message || "Signup failed");
    }

    return { success: true, message: result.message };
  } catch (error) {
    console.error("Error :: patientSignup ::", error);
    return { success: false, message: error.message };
  }
}

/**
 * Function: patientLogin
 * Purpose: Authenticates patient and returns raw response for token handling
 * @param {Object} data - Login credentials (email and password)
 * @returns {Promise<Response>} - Raw fetch response
 */
export async function patientLogin(data) {
  // Remove this log in production
  console.log("patientLogin ::", data);

  return await fetch(`${PATIENT_API}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  });
}

/**
 * Function: getPatientData
 * Purpose: Retrieves logged-in patient's data using auth token
 * @param {string} token - Authentication token
 * @returns {Promise<Object|null>} - Patient object or null on failure
 */
export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/${token}`);
    const data = await response.json();

    return response.ok ? data.patient : null;
  } catch (error) {
    console.error("Error fetching patient details:", error);
    return null;
  }
}

/**
 * Function: getPatientAppointments
 * Purpose: Fetch appointments for either patient or doctor dashboard
 * @param {string} id - Patient ID
 * @param {string} token - Auth token
 * @param {string} user - Requestor role: 'patient' or 'doctor'
 * @returns {Promise<Array|null>} - Appointments array or null
 */
export async function getPatientAppointments(id, token, user) {
  try {
    const response = await fetch(`${PATIENT_API}/${id}/${user}/${token}`);
    const data = await response.json();

    if (response.ok) {
      return data.appointments;
    }
    return null;
  } catch (error) {
    console.error("Error fetching patient appointments:", error);
    return null;
  }
}

/**
 * Function: filterAppointments
 * Purpose: Filter appointments by condition and name
 * @param {string} condition - Appointment status (e.g., 'pending', 'consulted')
 * @param {string} name - Doctor/patient name
 * @param {string} token - Auth token
 * @returns {Promise<{appointments: Array}>}
 */
export async function filterAppointments(condition, name, token) {
  try {
    const response = await fetch(
      `${PATIENT_API}/filter/${condition}/${name}/${token}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json"
        }
      }
    );

    if (response.ok) {
      const data = await response.json();
      return { appointments: data.appointments || [] };
    } else {
      console.error("Failed to filter appointments:", response.statusText);
      return { appointments: [] };
    }
  } catch (error) {
    console.error("Error filtering appointments:", error);
    alert("Something went wrong!");
    return { appointments: [] };
  }
}
