package tn.esprit.medicare.patient.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.medicare.patient.dto.PatientRequest;
import tn.esprit.medicare.patient.dto.PatientResponse;

public interface PatientService {

    PatientResponse create(PatientRequest request);

    PatientResponse getById(Long id);

    PatientResponse getByKeycloakId(String keycloakId);

    Page<PatientResponse> getAll(String search, Pageable pageable);

    PatientResponse update(Long id, PatientRequest request);

    void delete(Long id);

    boolean exists(Long id);
}
