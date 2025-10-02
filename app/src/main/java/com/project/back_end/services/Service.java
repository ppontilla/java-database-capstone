package com.project.back_end.services;

import com.project.back_end.model.*;
import com.project.back_end.repo.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceClass {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    // ✅ Constructor Injection
    public ServiceClass(TokenService tokenService,
                        AdminRepository adminRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository,
                        DoctorService doctorService,
                        PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    /**
     * Validate JWT Token
     */
    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();
        try {
            if (!tokenService.validateToken(token, user)) {
                response.put("message", "Invalid or expired token");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            response.put("message", "Token valid");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error validating token");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Validate Admin login
     */
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();
        try {
            Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
            if (admin != null && admin.getPassword().equals(receivedAdmin.getPassword())) {
                String token = tokenService.generateToken(admin.getUsername());
                response.put("token", token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.put("message", "Invalid username or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            response.put("message", "Error during admin validation");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Filter doctors by name, specialty, and available time
     */
    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
    }

    /**
     * Validate Appointment availability
     */
    public int validateAppointment(Appointment appointment) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(appointment.getDoctorId());
        if (doctorOpt.isEmpty()) {
            return -1; // doctor not found
        }
        List<String> availableTimes = doctorService.getDoctorAvailability(appointment.getDoctorId(),
                appointment.getDate());

        return availableTimes.contains(appointment.getStartTime()) ? 1 : 0;
    }

    /**
     * Validate Patient registration (avoid duplicates)
     */
    public boolean validatePatient(Patient patient) {
        Patient existing = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
        return existing == null; // true if patient does NOT exist
    }

    /**
     * Validate Patient Login
     */
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();
        try {
            Patient patient = patientRepository.findByEmail(login.getEmail());
            if (patient != null && patient.getPassword().equals(login.getPassword())) {
                String token = tokenService.generateToken(patient.getEmail());
                response.put("token", token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.put("message", "Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            response.put("message", "Error during patient login");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Filter Patient Appointments
     */
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        try {
            String email = tokenService.extractEmail(token);
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("message", "Patient not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }

            if (condition != null && name != null) {
                return patientService.filterByDoctorAndCondition(condition, name, patient.getId());
            } else if (condition != null) {
                return patientService.filterByCondition(condition, patient.getId());
            } else if (name != null) {
                return patientService.filterByDoctor(name, patient.getId());
            } else {
                return patientService.getPatientAppointment(patient.getId(), token);
            }

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error while filtering patient appointments");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
