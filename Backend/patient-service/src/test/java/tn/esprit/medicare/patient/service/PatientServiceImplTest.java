package tn.esprit.medicare.patient.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.medicare.patient.dto.PatientRequest;
import tn.esprit.medicare.patient.dto.PatientResponse;
import tn.esprit.medicare.patient.entity.Patient;
import tn.esprit.medicare.patient.enums.BloodType;
import tn.esprit.medicare.patient.enums.Gender;
import tn.esprit.medicare.patient.exception.DuplicateResourceException;
import tn.esprit.medicare.patient.exception.ResourceNotFoundException;
import tn.esprit.medicare.patient.mapper.PatientMapper;
import tn.esprit.medicare.patient.repository.PatientRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository repository;

    private PatientServiceImpl service;

    private PatientRequest request;

    @BeforeEach
    void setUp() {
        service = new PatientServiceImpl(repository, new PatientMapper());
        request = new PatientRequest(null, "Amine", "Ben Ali", LocalDate.of(1995, 4, 12),
                Gender.MALE, "+21620123456", "amine@mail.tn", "Tunis",
                BloodType.O_POS, "Pénicilline", "Sami Ben Ali");
    }

    @Test
    void create_savesPatient_whenEmailIsFree() {
        when(repository.existsByEmail("amine@mail.tn")).thenReturn(false);
        when(repository.save(any(Patient.class))).thenAnswer(inv -> {
            Patient p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        PatientResponse response = service.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("amine@mail.tn");
        verify(repository).save(any(Patient.class));
    }

    @Test
    void create_throwsConflict_whenEmailExists() {
        when(repository.existsByEmail("amine@mail.tn")).thenReturn(true);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(DuplicateResourceException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void getById_throwsNotFound_whenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}
