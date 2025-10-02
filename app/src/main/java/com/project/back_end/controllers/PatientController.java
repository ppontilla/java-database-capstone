package com.project.back_end.controllers;

import com.project.back_end.model.Patient;
import com.project.back_end.model.Login;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}patient")
public class PatientController {

    private final PatientService patientService;
    private final Service service;

    public PatientController(PatientService patientService, Service service) {
        this.patientService = patientService;
        this.service = service;
    }

    /**
     * Get patient details by token
     */
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatient(@PathVariable String token) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }
        return ResponseEntity.ok(patientService.getPatientDetails(token));
    }

    /**
     * Register a new patient
     */
    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            boolean exists = patientService.existsByEmailOrPhone(patient.getEmail(), patient.getPhoneNo());
            if (exists) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Patient with email id or phone no already exist"));
            }
            patientService.createPatient(patient);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Signup successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

    /**
     * Patient login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    /**
     * Get patient appointments
     */
    @GetMapping("/{id}/{token}")
    public ResponseEntity<?> getPatientAppointment(@PathVariable Long id, @PathVariable String token) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }
        return ResponseEntity.ok(patientService.getPatientAppointment(id));
    }

    /**
     * Filter patient appointments
     */
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }
        return ResponseEntity.ok(service.filterPatient(condition, name, token));
    }
}
