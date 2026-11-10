package tn.esprit.medicare.patient.dto;

import tn.esprit.medicare.patient.enums.BloodType;
import tn.esprit.medicare.patient.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Données renvoyées au client (et aux autres microservices via OpenFeign). */
public record PatientResponse(
        Long id,
        String keycloakId,
        String firstName,
        String lastName,
        LocalDate birthDate,
        Gender gender,
        String phone,
        String email,
        String address,
        BloodType bloodType,
        String allergies,
        String emergencyContact,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
