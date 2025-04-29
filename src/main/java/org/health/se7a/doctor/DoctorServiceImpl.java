package org.health.se7a.doctor;

import lombok.RequiredArgsConstructor;
import org.health.se7a.entity.EntityService;
import org.health.se7a.exception.XppException;
import org.health.se7a.nurse.Nurse;
import org.health.se7a.security.model.AccountStatus;
import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.util.SecurityContextUtil;
import org.health.se7a.users.UserRepo;
import org.health.se7a.visits.MedicalVisit;
import org.health.se7a.visits.MedicalVisitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final EntityService entityService;
    private final UserRepo userRepo;
    private final MedicalVisitRepository medicalVisitRepository;

    @Override
    public Boolean createDoctor(DoctorDTO doctorDTO) {
        validateDoctorPhoneNumberUniqueness(doctorDTO.getTelNumber());
        Doctor doctor = DoctorMapper.toEntity(doctorDTO);
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setAccountStatus(AccountStatus.ACTIVE);
        doctorRepository.save(doctor);
        entityService.addUserLoginInfo(doctor.getTelNumber(), LoginType.DOCTOR);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = doctorRepository.findById(doctorDTO.getId())
                .orElseThrow(() -> notFoundException(doctorDTO.getId(), "doctor.not.found"));

        validatePhoneNumberUpdate(doctor, doctorDTO.getTelNumber());
        updateDoctorDetails(doctor, doctorDTO);

        return true;
    }



    @Override
    public DoctorDTO getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(DoctorMapper::toDto)
                .orElseThrow(() -> notFoundException(id, "doctor.not.found"));
    }

    @Override
    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable)
                .map(DoctorMapper::toDto);
    }

    @Override
    public List<DoctorLookupDTO> getAllDoctorsForLookup() {
        return doctorRepository.findAll().stream()
                .map(doc -> DoctorLookupDTO.builder()
                        .id(doc.getId())
                        .fullName(doc.getName())
                        .build())
                .collect(Collectors.toList());
    }

    private XppException notFoundException(Object identifier, String messageKey) {
        return new XppException(
                List.of(identifier),
                HttpStatus.NOT_FOUND,
                messageKey
        );
    }

    private void validateDoctorPhoneNumberUniqueness(String phoneNumber) {
        Optional.of(phoneNumber)
                .filter(number -> userRepo.existsByTelNumber(phoneNumber))
                .ifPresent(number -> {
                    throw new XppException(
                            List.of(number),
                            HttpStatus.BAD_REQUEST,
                            "doctor.telNumber.exists"
                    );
                });
    }

    private void validatePhoneNumberUpdate(Doctor existingDoctor, String newNumber) {
        Optional.ofNullable(newNumber)
                .filter(number -> !number.equals(existingDoctor.getTelNumber()))
                .ifPresent(this::validateDoctorPhoneNumberUniqueness);
    }

    @Override
    public Doctor getDoctor() {
        return doctorRepository.findById(loggedInUserId())
                .orElseThrow(() -> notFoundException(loggedInUserId(), "doctor.not.found"));
    }

    private void updateDoctorDetails(Doctor doctor, DoctorDTO doctorDTO) {
        String oldPhoneNumber = doctor.getTelNumber();
        String newPhoneNumber = doctorDTO.getTelNumber();

        Optional.ofNullable(doctorDTO.getName()).ifPresent(doctor::setName);
        Optional.ofNullable(newPhoneNumber).ifPresent(doctor::setTelNumber);

        if (newPhoneNumber != null && !newPhoneNumber.equals(oldPhoneNumber)) {
            entityService.updateUserLoginInfoPhone(oldPhoneNumber, newPhoneNumber);
        }
        Optional.ofNullable(doctorDTO.getName()).ifPresent(doctor::setName);
        Optional.ofNullable(doctorDTO.getTelNumber()).ifPresent(doctor::setTelNumber);
        Optional.ofNullable(doctorDTO.getSpecialty()).ifPresent(doctor::setSpecialty);
        doctor.setUpdatedAt(LocalDateTime.now());
        doctorRepository.save(doctor);
    }

    public Map<String, String> getAllVisitsToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atTime(6, 0);   // 6:00 AM
        LocalDateTime endOfDay = today.atTime(23, 59);   // 11:59 PM

        List<MedicalVisit> visits = medicalVisitRepository.findVisitsByDoctorAndDayRange(
                getDoctor().getId(), startOfDay, endOfDay, Pageable.unpaged()
        ).getContent();
        return visits.stream()
                .filter(visit -> visit.getPatients() != null)
                .collect(Collectors.toMap(
                        visit -> visit.getPatients().getNationalityID(),
                        visit -> visit.getPatients().getName(),
                        (existing, replacement) -> existing
                ));
    }

    private Long loggedInUserId() {
        return SecurityContextUtil.loggedUser().getId();
    }

}
