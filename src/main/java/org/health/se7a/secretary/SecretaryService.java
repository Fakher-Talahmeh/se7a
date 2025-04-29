package org.health.se7a.secretary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SecretaryService {
    Boolean createSecretary(SecretaryDTO secretaryDTO);
    Boolean updateSecretary(SecretaryDTO secretaryDTO);
    Boolean deleteSecretary(Long secretaryId);
    SecretaryDTO getSecretary(Long secretaryId);
    Page<SecretaryDTO> getAllSecretaries(Pageable pageable);
    Secretary getSecretary();
}
