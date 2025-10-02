package com.project.back_end.controllers;

import com.project.back_end.model.Doctor;
import com.project.back_end.model.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final Service service;

    // Constructor injection
    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    /**
     * 1. Get Doctor Availability
     */
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(@PathVariable String user,
                                                                     @PathVariable Long doctorId,
                                                                     @PathVariable String date,
                                                                     @PathVariable String token) {
        // Validate token
        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, user);
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }

        try {
            Map<String, Object> availability = doctorService.getDoctorAvailability(doctorId, date);
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unable to fetch availability"));
        }
    }

    /**
     * 2. Get All Doctors
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {
        try {
            List<Doctor> doctors = doctorService.getDoctors();
            return ResponseEntity.ok(Map.of("doctors", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unable to fetch doctors"));
        }
    }

    /**
     * 3. Add New Doctor (Admin only)
     */
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(@RequestBody Doctor doctor,
                                                          @PathVariable String token) {
        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "admin");
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }

        try {
            boolean saved = doctorService.saveDoctor(doctor);
            if (saved) {
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Doctor added to db"));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Doctor already exists"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Some internal error occurred"));
        }
    }

    /**
     * 4. Doctor Login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    /**
     * 5. Update Doctor (Admin only)
     */
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@RequestBody Doctor doctor,
                                                            @PathVariable String token) {
        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "admin");
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }

        try {
            boolean updated = doctorService.updateDoctor(doctor);
            if (updated) {
                return ResponseEntity.ok(Map.of("message", "Doctor updated"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Doctor not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Some internal error occurred"));
        }
    }

    /**
     * 6. Delete Doctor (Admin only)
     */
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable Long id,
                                                            @PathVariable String token) {
        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "admin");
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }

        try {
            boolean deleted = doctorService.deleteDoctor(id);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Doctor deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Doctor not found with id " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Some internal error occurred"));
        }
    }

    /**
     * 7. Filter Doctors
     */
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filterDoctors(@PathVariable String name,
                                                             @PathVariable String time,
                                                             @PathVariable String speciality) {
        try {
            Map<String, Object> doctors = service.filterDoctor(name, speciality, time);
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unable to filter doctors"));
        }
    }
}
