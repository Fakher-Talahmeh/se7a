package org.health.se7a.labtests;

import lombok.RequiredArgsConstructor;
import org.health.se7a.exception.XppException;
import org.health.se7a.nurse.Nurse;
import org.health.se7a.patients.Patients;
import org.health.se7a.patients.PatientService;
import org.health.se7a.nurse.NurseService;
import org.health.se7a.security.util.SecurityContextUtil;
import org.health.se7a.visits.MedicalVisitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository labTestRepository;
    private final NurseService nurseService;
    private final PatientService patientService;
    private final MedicalVisitService medicalVisitService;

    @Override
    public Boolean createLabTest(Long visitId,LabTestDTO labTestDTO) {
        Patients patient = patientService.getPatientByNatId(labTestDTO.getPatientNatId());
        Nurse nurse = nurseService.getNurse();
        if (!patient.getNurses().contains(nurse)) {
            patient.getNurses().add(nurse);
        }
        LabTest labTest = LabTestMapper.toEntity(labTestDTO, patient);
        labTest.setTestDate(LocalDateTime.now());
        labTest.setNurse(nurse);
        labTest.setVisit(medicalVisitService.getMedicalVisitById(visitId));
        labTestRepository.save(labTest);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateLabTest(LabTestDTO labTestDTO) {
        LabTest existingTest = labTestRepository.findById(labTestDTO.getId())
                .orElseThrow(() -> notFoundException(labTestDTO.getId(), "labtest.not.found"));
        updateLabTestDetails(existingTest, labTestDTO);
        return true;
    }

    @Override
    public Boolean deleteLabTest(Long id) {
        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> notFoundException(id, "labtest.not.found"));
        labTestRepository.delete(labTest);
        return true;
    }

    @Override
    public Page<LabTestResponseDTO> getLabTestsByPatientNatId(String natId, Pageable pageable) {
        return labTestRepository.findLabTestByPatient_NationalityID(natId, pageable)
                .map(LabTestMapper::toResponse);
    }

    @Override
    public LabTestResponseDTO getLabTestById(Long id) {
        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> notFoundException(id, "labtest.not.found"));
        return LabTestMapper.toResponse(labTest);
    }

    @Override
    public Page<LabTestResponseDTO> getByNurse(Pageable pageable) {
        return labTestRepository.findLabTestByNurse_Id(loggedInUserId(), pageable)
                .map(LabTestMapper::toResponse);
    }

    private void updateLabTestDetails(LabTest existingTest, LabTestDTO dto) {
        Optional.ofNullable(dto.getTestName()).ifPresent(existingTest::setTestName);
        Optional.ofNullable(dto.getResult()).ifPresent(existingTest::setResult);
        existingTest.setTestDate(LocalDateTime.now());
        labTestRepository.save(existingTest);
    }

    private XppException notFoundException(Object identifier, String messageKey) {
        return new XppException(List.of(identifier), HttpStatus.NOT_FOUND, messageKey);
    }

    private Long loggedInUserId() {
        return SecurityContextUtil.loggedUser().getId();
    }
}
