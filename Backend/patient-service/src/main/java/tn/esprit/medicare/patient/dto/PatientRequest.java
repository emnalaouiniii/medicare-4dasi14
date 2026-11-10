package tn.esprit.medicare.patient.dto;

import jakarta.validation.constraints.*;
import tn.esprit.medicare.patient.enums.BloodType;
import tn.esprit.medicare.patient.enums.Gender;

import java.time.LocalDate;

/** Données reçues pour créer ou modifier un patient. */
public record PatientRequest(

        String keycloakId,

        @NotBlank(message = "Le prénom est obligatoire")
        @Size(max = 50)
        String firstName,

        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 50)
        String lastName,

        @NotNull(message = "La date de naissance est obligatoire")
        @Past(message = "La date de naissance doit être dans le passé")
        LocalDate birthDate,

        @NotNull(message = "Le genre est obligatoire")
        Gender gender,

        @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Numéro de téléphone invalide")
        String phone,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Email invalide")
        String email,

        String address,

        BloodType bloodType,

        @Size(max = 500)
        String allergies,

        String emergencyContact
) {
}
