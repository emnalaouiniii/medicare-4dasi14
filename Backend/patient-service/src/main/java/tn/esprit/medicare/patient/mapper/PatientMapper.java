package tn.esprit.medicare.patient.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.medicare.patient.dto.PatientRequest;
import tn.esprit.medicare.patient.dto.PatientResponse;
import tn.esprit.medicare.patient.entity.Patient;

@Component
public class PatientMapper {

    public Patient toEntity(PatientRequest r) {
        Patient p = new Patient();
        updateEntity(p, r);
        return p;
    }

    public void updateEntity(Patient p, PatientRequest r) {
        p.setKeycloakId(r.keycloakId());
        p.setFirstName(r.firstName());
        p.setLastName(r.lastName());
        p.setBirthDate(r.birthDate());
        p.setGender(r.gender());
        p.setPhone(r.phone());
        p.setEmail(r.email());
        p.setAddress(r.address());
        p.setBloodType(r.bloodType());
        p.setAllergies(r.allergies());
        p.setEmergencyContact(r.emergencyContact());
    }

    public PatientResponse toResponse(Patient p) {
        return new PatientResponse(
                p.getId(), p.getKeycloakId(), p.getFirstName(), p.getLastName(),
                p.getBirthDate(), p.getGender(), p.getPhone(), p.getEmail(),
                p.getAddress(), p.getBloodType(), p.getAllergies(),
                p.getEmergencyContact(), p.getCreatedAt(), p.getUpdatedAt());
    }
}
