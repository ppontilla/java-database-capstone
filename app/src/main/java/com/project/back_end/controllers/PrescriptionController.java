package com.project.back_end.controllers;

import com.project.back_end.model.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import com.project.back_end.services.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final Service service;
    private final AppointmentService appointmentService;

    public PrescriptionController(PrescriptionService prescriptionService,
                                  Service service,
                                  AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.service = service;
        this.appointmentService = appointmentService;
    }

    /**
     * Save Prescription
     * Accessible only to doctors
     */
    @PostMapping("/{token}")
    public ResponseEntity<?> savePrescription(@PathVariable String token,
                                              @RequestBody Prescription prescription) {
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }

        try {
            prescriptionService.savePrescription(prescription);

            // Optionally update appointment status to reflect prescription added
            appointmentService.updateAppointmentStatus(prescription.getAppointmentId(), "PRESCRIBED");

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Prescription saved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save prescription"));
        }
    }

    /**
     * Get Prescription by Appointment ID
     * Accessible only to doctors
     */
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(@PathVariable Long appointmentId,
                                             @PathVariable String token) {
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }

        Prescription prescription = prescriptionService.getPrescription(appointmentId);
        if (prescription == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No prescription found for this appointment"));
        }

        return ResponseEntity.ok(prescription);
    }
}
