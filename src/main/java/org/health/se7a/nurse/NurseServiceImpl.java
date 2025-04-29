package org.health.se7a.nurse;

import org.health.se7a.entity.EntityService;
import org.health.se7a.exception.XppException;
import org.health.se7a.security.model.AccountStatus;
import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.util.SecurityContextUtil;
import org.health.se7a.users.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;
    private final EntityService entityService;
    private final UserRepo userRepo;

    @Override
    public NurseDTO getNurseById(Long id) {
        return nurseRepository.findById(id)
                .map(NurseMapper::toDto)
                .orElseThrow(() -> notFoundException(id, "nurse.not.found"));
    }

    @Override
    public Page<NurseDTO> getAllNurses(Pageable pageable) {
        return nurseRepository.findAll(pageable)
                .map(NurseMapper::toDto);
    }

    @Override
    @Transactional
    public Boolean createNurse(NurseDTO nurseDTO) {
        validatePhoneNumberUniqueness(nurseDTO.getTelNumber());
        Nurse nurse = NurseMapper.toEntity(nurseDTO);
        nurse.setAccountStatus(AccountStatus.ACTIVE);
        entityService.addUserLoginInfo(nurse.getTelNumber(), LoginType.NURSE);
        nurseRepository.save(nurse);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateNurse(NurseDTO nurseDTO) {
        Nurse existingNurse = nurseRepository.findById(nurseDTO.getId())
                .orElseThrow(() -> notFoundException(nurseDTO.getId(), "nurse.not.found"));
        validatePhoneNumberUpdate(existingNurse, nurseDTO.getTelNumber());
        updateNurseDetails(existingNurse, nurseDTO);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteNurse(Long id) {
        Nurse existingNurse = nurseRepository.findById(id)
                .orElseThrow(() -> notFoundException(id, "nurse.not.found"));

        nurseRepository.delete(existingNurse);

        return true;
    }

    private void updateNurseDetails(Nurse nurse, NurseDTO nurseDTO) {
        String oldPhoneNumber = nurse.getTelNumber();
        String newPhoneNumber = nurseDTO.getTelNumber();

        Optional.ofNullable(nurseDTO.getName()).ifPresent(nurse::setName);
        Optional.ofNullable(newPhoneNumber).ifPresent(nurse::setTelNumber);

        if (newPhoneNumber != null && !newPhoneNumber.equals(oldPhoneNumber)) {
            entityService.updateUserLoginInfoPhone(oldPhoneNumber, newPhoneNumber);
        }
        Optional.ofNullable(nurseDTO.getName()).ifPresent(nurse::setName);
        Optional.ofNullable(nurseDTO.getTelNumber()).ifPresent(nurse::setTelNumber);
        nurseRepository.save(nurse);
    }

    private void validatePhoneNumberUniqueness(String phoneNumber) {
        Optional.ofNullable(phoneNumber)
                .filter(telNumber -> !userRepo.existsByTelNumber(telNumber))
                .orElseThrow(() -> new XppException(List.of(phoneNumber),HttpStatus.BAD_REQUEST,"phoneNumber.already.exists"));
    }

    private void validatePhoneNumberUpdate(Nurse existingNurse, String newPhoneNumber) {
        if (newPhoneNumber != null && !newPhoneNumber.equals(existingNurse.getTelNumber())) {
            validatePhoneNumberUniqueness(newPhoneNumber);
        }
    }
    @Override
    public Nurse getNurse() {
        return nurseRepository.findById(loggedInUserId())
                .orElseThrow(() -> notFoundException(loggedInUserId(), "nurse.not.found"));
    }

    private XppException notFoundException(Long id, String messageKey) {
        return new XppException(List.of(id), HttpStatus.NOT_FOUND, messageKey);
    }
    private Long loggedInUserId() {
        return SecurityContextUtil.loggedUser().getId();
    }

}
