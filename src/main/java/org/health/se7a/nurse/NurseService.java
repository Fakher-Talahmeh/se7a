package org.health.se7a.nurse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NurseService {
    Boolean createNurse(NurseDTO nurseDTO);
    Boolean  updateNurse(NurseDTO nurseDTO);
    Boolean deleteNurse(Long id);
    NurseDTO getNurseById(Long id);
    Page<NurseDTO> getAllNurses(Pageable page);
    Nurse getNurse();
}
