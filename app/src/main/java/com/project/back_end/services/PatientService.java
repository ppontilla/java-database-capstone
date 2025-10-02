package com.project.back_end.services;

import com.project.back_end.model.Patient;
import com.project.back_end.model.Appointment;
import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.repo.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    @Autowired
    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    /** 1. Create Patient */
    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 2. Get Patient Appointments */
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = tokenService.extractEmail(token);
            Optional<Patient> patientOpt = patientRepository.findByEmail(email);

            if (patientOpt.isEmpty() || !patientOpt.get().getId().equals(id)) {
                response.put("error", "Unauthorized access");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            List<Appointment> appointments = appointmentRepository.findByPatientId(id);
            List<AppointmentDTO> appointmentDTOs = appointments.stream()
                    .map(AppointmentDTO::fromEntity)
                    .collect(Collectors.toList());

            response.put("appointments", appointmentDTOs);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Failed to fetch appointments");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /** 3. Filter By Condition (Past/Future) */
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            int status;
            if ("past".equalsIgnoreCase(condition)) {
                status = 1;
            } else if ("future".equalsIgnoreCase(condition)) {
                status = 0;
            } else {
                response.put("error", "Invalid condition");
                return ResponseEntity.badRequest().body(response);
            }

            List<Appointment> appointments = appointmentRepository.findByPatientIdAndStatus(id, status);
            List<AppointmentDTO> appointmentDTOs = appointments.stream()
                    .map(AppointmentDTO::fromEntity)
                    .collect(Collectors.toList());

            response.put("appointments", appointmentDTOs);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Error filtering appointments");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /** 4. Filter By Doctor Name */
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Appointment> appointments =
                    appointmentRepository.findByDoctorNameContainingIgnoreCaseAndPatientId(name, patientId);

            List<AppointmentDTO> appointmentDTOs = appointments.stream()
                    .map(AppointmentDTO::fromEntity)
                    .collect(Collectors.toList());

            response.put("appointments", appointmentDTOs);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Error filtering by doctor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /** 5. Filter By Doctor + Condition */
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition, String name, long patientId) {
        Map<String, Object> response = new HashMap<>();
        try {
            int status;
            if ("past".equalsIgnoreCase(condition)) {
                status = 1;
            } else if ("future".equalsIgnoreCase(condition)) {
                status = 0;
            } else {
                response.put("error", "Invalid condition");
                return ResponseEntity.badRequest().body(response);
            }

            List<Appointment> appointments =
                    appointmentRepository.findByDoctorNameContainingIgnoreCaseAndPatientIdAndStatus(name, patientId, status);

            List<AppointmentDTO> appointmentDTOs = appointments.stream()
                    .map(AppointmentDTO::fromEntity)
                    .collect(Collectors.toList());

            response.put("appointments", appointmentDTOs);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Error filtering by doctor and condition");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /** 6. Get Patient Details from Token */
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = tokenService.extractEmail(token);
            Optional<Patient> patientOpt = patientRepository.findByEmail(email);

            if (patientOpt.isPresent()) {
                response.put("patient", patientOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Patient not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            response.put("error", "Failed to fetch patient details");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
