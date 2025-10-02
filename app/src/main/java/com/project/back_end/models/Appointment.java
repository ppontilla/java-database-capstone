package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Doctor relationship
    @ManyToOne
    @NotNull(message = "Doctor cannot be null")
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // Patient relationship
    @ManyToOne
    @NotNull(message = "Patient cannot be null")
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Appointment time
    @NotNull(message = "Appointment time cannot be null")
    @Future(message = "Appointment time must be in the future")
    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    // Status (0 = Scheduled, 1 = Completed)
    @NotNull(message = "Status cannot be null")
    @Column(nullable = false)
    private int status;

    // Constructors
    public Appointment() {}

    public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentTime, int status) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    // Helper Methods

    /**
     * Returns the end time of the appointment (1 hour after start).
     */
    @Transient
    public LocalDateTime getEndTime() {
        return this.appointmentTime != null ? this.appointmentTime.plusHours(1) : null;
    }

    /**
     * Returns only the date portion of the appointment.
     */
    @Transient
    public LocalDate getAppointmentDate() {
        return this.appointmentTime != null ? this.appointmentTime.toLocalDate() : null;
    }

    /**
     * Returns only the time portion of the appointment.
     */
    @Transient
    public LocalTime getAppointmentTimeOnly() {
        return this.appointmentTime != null ? this.appointmentTime.toLocalTime() : null;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
