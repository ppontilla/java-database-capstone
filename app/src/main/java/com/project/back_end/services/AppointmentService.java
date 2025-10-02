package com.project.back_end.services;

import com.project.back_end.model.Appointment;
import com.project.back_end.model.Patient;
import com.project.back_end.model.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    /**
     * Book a new appointment
     */
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1; // success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // failure
        }
    }

    /**
     * Update an existing appointment
     */
    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();

        return appointmentRepository.findById(appointment.getId())
                .map(existingAppointment -> {
                    // Validate doctor exists
                    Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctor().getId());
                    if (doctor.isEmpty()) {
                        response.put("message", "Doctor not found");
                        return ResponseEntity.badRequest().body(response);
                    }

                    // Update fields
                    existingAppointment.setAppointmentTime(appointment.getAppointmentTime());
                    existingAppointment.setStatus(appointment.getStatus());

                    appointmentRepository.save(existingAppointment);
                    response.put("message", "Appointment updated successfully");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("message", "Appointment not found");
                    return ResponseEntity.badRequest().body(response);
                });
    }

    /**
     * Cancel appointment (only patient who booked it can cancel)
     */
    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();
        Long patientId = tokenService.extractPatientId(token);

        return appointmentRepository.findById(id)
                .map(appointment -> {
                    if (!appointment.getPatient().getId().equals(patientId)) {
                        response.put("message", "You are not authorized to cancel this appointment");
                        return ResponseEntity.status(403).body(response);
                    }

                    appointmentRepository.delete(appointment);
                    response.put("message", "Appointment canceled successfully");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("message", "Appointment not found");
                    return ResponseEntity.badRequest().body(response);
                });
    }

    /**
     * Get all appointments for a doctor on a given date, optionally filter by patient name
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        Map<String, Object> response = new HashMap<>();
        Long doctorId = tokenService.extractDoctorId(token);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Appointment> appointments;

        if (pname != null && !pname.isEmpty()) {
            appointments = appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, pname, start, end
            );
        } else {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                    doctorId, start, end
            );
        }

        response.put("appointments", appointments);
        return response;
    }

    /**
     * Change status of an appointment
     */
    @Transactional
    public void changeStatus(long id, int status) {
        appointmentRepository.findById(id).ifPresent(appointment -> {
            appointment.setStatus(status);
            appointmentRepository.save(appointment);
        });
    }
}
