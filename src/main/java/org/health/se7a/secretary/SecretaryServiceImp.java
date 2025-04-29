package org.health.se7a.secretary;

import lombok.RequiredArgsConstructor;
import org.health.se7a.doctor.Doctor;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecretaryServiceImp implements SecretaryService {

    private final SecretaryRepository secretaryRepository;
    private final EntityService entityService;
    private final UserRepo userRepo;

    @Override
    public Boolean createSecretary(SecretaryDTO secretaryDTO) {
        validateSecretaryPhoneNumberUniqueness(secretaryDTO.getTelNumber());
        Secretary secretary = SecretaryMapper.toEntity(secretaryDTO);
        secretary.setCreatedAt(LocalDateTime.now());
        secretary.setAccountStatus(AccountStatus.ACTIVE);
        secretaryRepository.save(secretary);
        entityService.addUserLoginInfo(secretary.getTelNumber(), LoginType.SECRETARY);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateSecretary(SecretaryDTO secretaryDTO) {
        Secretary secretary = secretaryRepository.findById(secretaryDTO.getId())
                .orElseThrow(() -> notFoundException(secretaryDTO.getId(), "secretary.not.found"));

        validatePhoneNumberUpdate(secretary, secretaryDTO.getTelNumber());
        updateSecretaryDetails(secretary, secretaryDTO);

        return true;
    }

    @Override
    public Boolean deleteSecretary(Long id) {
        Secretary secretary = secretaryRepository.findById(id)
                .orElseThrow(() -> notFoundException(id, "secretary.not.found"));
        secretaryRepository.delete(secretary);
        return true;
    }

    @Override
    public SecretaryDTO getSecretary(Long id) {
        return secretaryRepository.findById(id)
                .map(SecretaryMapper::toDto)
                .orElseThrow(() -> notFoundException(id, "secretary.not.found"));
    }

    @Override
    public Page<SecretaryDTO> getAllSecretaries(Pageable pageable) {
        return secretaryRepository.findAll(pageable)
                .map(SecretaryMapper::toDto);
    }

    private XppException notFoundException(Object identifier, String messageKey) {
        return new XppException(
                List.of(identifier),
                HttpStatus.NOT_FOUND,
                messageKey
        );
    }

    private void validateSecretaryPhoneNumberUniqueness(String phoneNumber) {
        Optional.of(phoneNumber)
                .filter(number -> userRepo.existsByTelNumber(phoneNumber))
                .ifPresent(number -> {
                    throw new XppException(
                            List.of(number),
                            HttpStatus.BAD_REQUEST,
                            "secretary.telNumber.exists"
                    );
                });
    }


    private void validatePhoneNumberUpdate(Secretary existingSecretary, String newNumber) {
        Optional.ofNullable(newNumber)
                .filter(number -> !number.equals(existingSecretary.getTelNumber()))
                .ifPresent(this::validateSecretaryPhoneNumberUniqueness);
    }

    private void updateSecretaryDetails(Secretary secretary, SecretaryDTO secretaryDTO) {

        String oldPhoneNumber = secretary.getTelNumber();
        String newPhoneNumber = secretaryDTO.getTelNumber();

        Optional.ofNullable(secretaryDTO.getName()).ifPresent(secretary::setName);
        Optional.ofNullable(newPhoneNumber).ifPresent(secretary::setTelNumber);

        if (newPhoneNumber != null && !newPhoneNumber.equals(oldPhoneNumber)) {
            entityService.updateUserLoginInfoPhone(oldPhoneNumber, newPhoneNumber);
        }

        Optional.ofNullable(secretaryDTO.getName()).ifPresent(secretary::setName);
        Optional.ofNullable(secretaryDTO.getTelNumber()).ifPresent(secretary::setTelNumber);
        secretary.setUpdatedAt(LocalDateTime.now());
        secretaryRepository.save(secretary);
    }

    @Override
    public Secretary getSecretary() {
        return secretaryRepository.findById(loggedInUserId())
                .orElseThrow(() -> notFoundException(loggedInUserId(), "secretary.not.found"));
    }

    private Long loggedInUserId() {
        return SecurityContextUtil.loggedUser().getId();
    }


}
