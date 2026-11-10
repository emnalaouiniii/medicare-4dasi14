package tn.esprit.medicare.patient.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.medicare.patient.dto.PatientRequest;
import tn.esprit.medicare.patient.dto.PatientResponse;
import tn.esprit.medicare.patient.entity.Patient;
import tn.esprit.medicare.patient.exception.DuplicateResourceException;
import tn.esprit.medicare.patient.exception.ResourceNotFoundException;
import tn.esprit.medicare.patient.mapper.PatientMapper;
import tn.esprit.medicare.patient.repository.PatientRepository;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;

    public PatientServiceImpl(PatientRepository repository, PatientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PatientResponse create(PatientRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Un patient avec l'email " + request.email() + " existe déjà");
        }
        Patient saved = repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getById(Long id) {
        return mapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getByKeycloakId(String keycloakId) {
        return repository.findByKeycloakId(keycloakId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun patient lié au compte " + keycloakId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientResponse> getAll(String search, Pageable pageable) {
        Page<Patient> page = (search == null || search.isBlank())
                ? repository.findAll(pageable)
                : repository.findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(search, search, pageable);
        return page.map(mapper::toResponse);
    }

    @Override
    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = findOrThrow(id);
        if (repository.existsByEmailAndIdNot(request.email(), id)) {
            throw new DuplicateResourceException("L'email " + request.email() + " est déjà utilisé");
        }
        mapper.updateEntity(patient, request);
        return mapper.toResponse(repository.save(patient));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    private Patient findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient introuvable avec l'id " + id));
    }
}
