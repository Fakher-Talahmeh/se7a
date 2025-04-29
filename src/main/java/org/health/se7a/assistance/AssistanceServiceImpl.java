package org.health.se7a.assistance;

import lombok.RequiredArgsConstructor;
import org.health.se7a.doctor.DoctorDTO;
import org.health.se7a.doctor.DoctorMapper;
import org.health.se7a.doctor.DoctorService;
import org.health.se7a.exception.XppException;
import org.health.se7a.nurse.Nurse;
import org.health.se7a.nurse.NurseService;
import org.health.se7a.patients.PatientService;
import org.health.se7a.patients.Patients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssistanceServiceImpl implements AssistanceService {

    private final MedicalAssistanceRepository repository;
    private final NurseService nurseService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Override
    public Boolean CreateAssistance(MedicalAssistanceDTO dto) {
        Nurse nurse = nurseService.getNurse();
        DoctorDTO doctor = doctorService.getDoctorById(dto.getDoctorId());
        Patients patient = patientService.getPatientByNatId(dto.getPatientNatId());
        MedicalAssistance assistance = MedicalAssistanceMapper.toEntity(dto, patient);
        assistance.setNurse(nurse);
        assistance.setDoctor(DoctorMapper.toEntity(doctor));
        repository.save(assistance);
        return true;
    }

    @Override
    public Boolean UpdateAssistance(MedicalAssistanceDTO dto) {
        MedicalAssistance assistance = repository.findById(dto.getId())
                .orElseThrow(() -> notFoundException(dto.getId()));
        assistance.setNotes(dto.getNotes());
        assistance.setToolsPrepared(dto.getToolsPrepared());
        if (dto.getDoctorId() != null) {
            DoctorDTO doctor = doctorService.getDoctorById(dto.getDoctorId());
            assistance.setDoctor(DoctorMapper.toEntity(doctor));
        }

        repository.save(assistance);
        return true;
    }

    @Override
    public Boolean DeleteAssistance(Long id) {
        MedicalAssistance assistance = repository.findById(id)
                .orElseThrow(() -> notFoundException(id));
        repository.delete(assistance);
        return true;
    }

    @Override
    public MedicalAssistanceResponseDTO GetMedicalAssistance(Long id) {
        MedicalAssistance assistance = repository.findById(id)
                .orElseThrow(() -> notFoundException(id));
        return MedicalAssistanceMapper.toResponse(assistance);
    }

    @Override
    public Page<MedicalAssistanceResponseDTO> GetAllMedicalAssistance(Pageable pageable) {
        return repository.findAll(pageable)
                .map(MedicalAssistanceMapper::toResponse);
    }

    @Override
    public Page<MedicalAssistanceResponseDTO> GetMedicalAssistanceByNurse(Pageable pageable, Long nurseId) {
        return repository.getAllByNurse_Id(nurseId, pageable)
                .map(MedicalAssistanceMapper::toResponse);
    }

    @Override
    public Page<MedicalAssistanceResponseDTO> GetMedicalAssistanceByDoctor(Pageable pageable, Long doctorId) {
        return repository.getAllByDoctor_Id(doctorId, pageable)
                .map(MedicalAssistanceMapper::toResponse);
    }

    @Override
    public Page<MedicalAssistanceResponseDTO> GetMedicalAssistanceByPatient(Pageable pageable, String patientId) {
        Patients patient = patientService.getPatientByNatId(patientId);
        return repository.findMedicalAssistanceByPatient_NationalityID(patient.getNationalityID(), pageable)
                .map(MedicalAssistanceMapper::toResponse);
    }

    private XppException notFoundException(Object identifier) {
        return new XppException(List.of(identifier), HttpStatus.NOT_FOUND, "assistance.not.found");
    }
}
