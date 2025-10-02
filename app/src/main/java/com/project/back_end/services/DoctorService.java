package com.project.back_end.services;

import com.project.back_end.model.Doctor;
import com.project.back_end.model.Appointment;
import com.project.back_end.model.Login;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    /** 1. Get Doctor Availability */
    @Transactional(readOnly = true)
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Doctor doctor = doctorOpt.get();
        List<String> availableSlots = new ArrayList<>(doctor.getAvailableTimes());

        List<Appointment> bookedAppointments = appointmentRepository.findByDoctorIdAndDate(doctorId, date);
        Set<String> bookedSlots = bookedAppointments.stream()
                .map(Appointment::getTimeSlot)
                .collect(Collectors.toSet());

        availableSlots.removeAll(bookedSlots);
        return availableSlots;
    }

    /** 2. Save Doctor */
    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
                return -1; // already exists
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0; // error
        }
    }

    /** 3. Update Doctor */
    public int updateDoctor(Doctor doctor) {
        try {
            if (!doctorRepository.existsById(doctor.getId())) {
                return -1; // not found
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0; // error
        }
    }

    /** 4. Get All Doctors */
    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    /** 5. Delete Doctor */
    public int deleteDoctor(long id) {
        try {
            if (!doctorRepository.existsById(id)) {
                return -1; // not found
            }
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0; // error
        }
    }

    /** 6. Validate Doctor Login */
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Map<String, String> response = new HashMap<>();
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(login.getEmail());

        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            if (doctor.getPassword().equals(login.getPassword())) {
                String token = tokenService.generateToken(doctor.getEmail());
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Invalid password");
                return ResponseEntity.status(401).body(response);
            }
        } else {
            response.put("error", "Doctor not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    /** 7. Find Doctor by Name */
    @Transactional(readOnly = true)
    public Map<String, Object> findDoctorByName(String name) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        response.put("doctors", doctors);
        return response;
    }

    /** 8. Filter by Name + Specialty + Time */
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    /** 9. Filter by Name + Time */
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    /** 10. Filter by Name + Specialty */
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specialty) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return Map.of("doctors", doctors);
    }

    /** 11. Filter by Specialty + Time */
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    /** 12. Filter by Specialty */
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorBySpecility(String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return Map.of("doctors", doctors);
    }

    /** 13. Filter all Doctors by Time */
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        List<Doctor> doctors = doctorRepository.findAll();
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    /** 14. Private Helper Method: Filter Doctors by AM/PM */
    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        return doctors.stream()
                .filter(doc -> doc.getAvailableTimes().stream().anyMatch(time ->
                        (amOrPm.equalsIgnoreCase("AM") && time.endsWith("AM")) ||
                        (amOrPm.equalsIgnoreCase("PM") && time.endsWith("PM"))
                ))
                .collect(Collectors.toList());
    }
}
