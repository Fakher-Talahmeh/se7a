package org.health.se7a.doctor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DoctorService {
    Boolean createDoctor(DoctorDTO doctorDTO);
    Boolean  updateDoctor(DoctorDTO doctorDTO);
    DoctorDTO getDoctorById(Long id);
    Page<DoctorDTO> getAllDoctors(Pageable page);
    List<DoctorLookupDTO> getAllDoctorsForLookup();
    Doctor getDoctor();
    Map<String, String> getAllVisitsToday();

}
