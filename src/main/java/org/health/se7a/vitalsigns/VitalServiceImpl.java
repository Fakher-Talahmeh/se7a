package org.health.se7a.vitalsigns;

import lombok.RequiredArgsConstructor;
import org.health.se7a.exception.XppException;
import org.health.se7a.nurse.NurseService;
import org.health.se7a.patients.PatientRepository;
import org.health.se7a.patients.PatientService;
import org.health.se7a.patients.Patients;
import org.health.se7a.nurse.Nurse;
import org.health.se7a.nurse.NurseRepository;
import org.health.se7a.security.util.SecurityContextUtil;
import org.health.se7a.visits.MedicalVisit;
import org.health.se7a.visits.MedicalVisitMapper;
import org.health.se7a.visits.MedicalVisitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VitalServiceImpl implements VitalService {

    private final VitalSignsRepository vitalSignsRepository;
    private final NurseService nurseService;
    private final PatientService patientService;
    private final MedicalVisitService medicalVisitService;

    @Override
    @Transactional
    public Boolean updateVitalSigns(VitalSignsDTO vitalSignsDTO) {
        VitalSigns existingVitalSigns = vitalSignsRepository.findById(vitalSignsDTO.getId())
                .orElseThrow(() -> notFoundException(vitalSignsDTO.getId(), "vitalSigns.not.found"));
        updateVitalDetails(existingVitalSigns, vitalSignsDTO);
        vitalSignsRepository.save(existingVitalSigns);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteVitalSigns(Long id) {
        VitalSigns existingVitalSigns = vitalSignsRepository.findById(id)
                .orElseThrow(() -> notFoundException(id, "vitalSigns.not.found"));

        vitalSignsRepository.delete(existingVitalSigns);
        return true;
    }

    @Override
    @Transactional
    public Boolean createVitalSigns(Long visitId,VitalSignsDTO vitalSignsDTO) {
        Patients patient = patientService.getPatientByNatId(vitalSignsDTO.getPatientNatId());
        Nurse nurse = nurseService.getNurse();
        if (!patient.getNurses().contains(nurse)) {
            patient.getNurses().add(nurse);
        }
        VitalSigns vitalSigns = VitalSignsMapper.toEntity(vitalSignsDTO, patient, nurse);
        vitalSigns.setVisit(medicalVisitService.getMedicalVisitById(visitId));
        vitalSignsRepository.save(vitalSigns);
        return true;
    }

    @Override
    public Page<VitalSignsResponseDTO> getVitalSignsByPatient(String patientId, Pageable pageable) {
        return vitalSignsRepository.findByPatient_NationalityID(patientId, pageable)
                .map(VitalSignsMapper::toDto);

    }

    @Override
    public Page<VitalSignsResponseDTO> getVitalSignsDTOByNurse(Long nurseId, Pageable pageable) {
        return vitalSignsRepository.findByNurseId(nurseId, pageable)
                .map(VitalSignsMapper::toDto);
    }

    @Override
    public VitalSignsResponseDTO getVitalSignsDTOById(Long id) {
        return vitalSignsRepository.findById(id)
                .map(VitalSignsMapper::toDto)
                .orElseThrow(() -> notFoundException(id, "vitalSigns.not.found"));

    }

    private void updateVitalDetails(VitalSigns vitalSigns, VitalSignsDTO vitalSignsDTO) {
        vitalSigns.setBloodPressure(vitalSignsDTO.getBloodPressure());
        vitalSigns.setHeartRate(vitalSignsDTO.getHeartRate());
        vitalSigns.setTemperature(vitalSignsDTO.getTemperature());
        vitalSigns.setRespiratoryRate(vitalSignsDTO.getRespiratoryRate());
        vitalSigns.setRecordedAt(vitalSignsDTO.getRecordedAt());
    }

    private XppException notFoundException(Object id, String messageKey) {
        return new XppException(List.of(id), HttpStatus.NOT_FOUND, messageKey);
    }

    private Long loggedInUserId() {
        return SecurityContextUtil.loggedUser().getId();
    }
}
