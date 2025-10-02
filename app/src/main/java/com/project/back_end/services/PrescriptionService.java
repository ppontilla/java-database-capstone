package com.project.back_end.services;

import com.project.back_end.model.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    // ✅ Constructor Injection
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * Save a new prescription
     * @param prescription The prescription object to be saved
     * @return ResponseEntity with status and message
     */
    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
        Map<String, String> response = new HashMap<>();

        try {
            // ✅ Check if prescription already exists for the same appointment
            List<Prescription> existing = prescriptionRepository.findByAppointmentId(prescription.getAppointmentId());
            if (!existing.isEmpty()) {
                response.put("message", "Prescription already exists for this appointment");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            prescriptionRepository.save(prescription);
            response.put("message", "Prescription saved");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "An error occurred while saving the prescription");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get prescription by appointment ID
     * @param appointmentId The appointment ID to find prescription for
     * @return ResponseEntity with prescription details
     */
    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);

            if (prescriptions.isEmpty()) {
                response.put("message", "No prescription found for this appointment");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response.put("prescriptions", prescriptions);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "An error occurred while retrieving the prescription");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
