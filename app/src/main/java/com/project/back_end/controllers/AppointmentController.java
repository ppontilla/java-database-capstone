package com.project.back_end.controllers;

import com.project.back_end.model.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TokenService tokenService;

    // Constructor injection
    public AppointmentController(AppointmentService appointmentService, TokenService tokenService) {
        this.appointmentService = appointmentService;
        this.tokenService = tokenService;
    }

    /**
     * Get Appointments
     * Doctors can retrieve appointments by date and patient name
     */
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable String date,
                                             @PathVariable String patientName,
                                             @PathVariable String token) {
        // Validate token for doctor role
        if (!tokenService.validateToken(token, "doctor")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or unauthorized token"));
        }

        try {
            List<Appointment> appointments = appointmentService.getAppointments(date, patientName);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unable to fetch appointments"));
        }
    }

    /**
     * Book Appointment
     * Patients can book an appointment
     */
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(@PathVariable String token,
                                                               @RequestBody Appointment appointment) {
        if (!tokenService.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or unauthorized token"));
        }

        // Validate appointment before booking
        if (!appointmentService.validateAppointment(appointment)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid appointment details"));
        }

        try {
            appointmentService.bookAppointment(appointment);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Appointment booked successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unable to book appointment"));
        }
    }

    /**
     * Update Appointment
     * Patients can update their appointment
     */
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(@PathVariable String token,
                                                                 @RequestBody Appointment appointment) {
        if (!tokenService.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or unauthorized token"));
        }

        try {
            appointmentService.updateAppointment(appointment);
            return ResponseEntity.ok(Map.of("message", "Appointment updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unable to update appointment"));
        }
    }

    /**
     * Cancel Appointment
     * Patients can cancel their appointment
     */
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable Long id,
                                                                 @PathVariable String token) {
        if (!tokenService.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or unauthorized token"));
        }

        try {
            appointmentService.cancelAppointment(id);
            return ResponseEntity.ok(Map.of("message", "Appointment canceled successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unable to cancel appointment"));
        }
    }
}
