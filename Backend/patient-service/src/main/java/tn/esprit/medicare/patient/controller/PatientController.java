package tn.esprit.medicare.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.medicare.patient.dto.PatientRequest;
import tn.esprit.medicare.patient.dto.PatientResponse;
import tn.esprit.medicare.patient.service.PatientService;

import java.net.URI;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Gestion des patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Créer un patient", description = "201 Created, 400 données invalides, 409 email déjà utilisé")
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody PatientRequest request) {
        PatientResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détails d'un patient", description = "Utilisé par appointment, prescription et medical-record (OpenFeign). 200, 404")
    public PatientResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Vérifier l'existence d'un patient", description = "Réponse légère pour les autres microservices")
    public boolean exists(@PathVariable Long id) {
        return service.exists(id);
    }

    @GetMapping("/keycloak/{keycloakId}")
    @Operation(summary = "Patient lié à un compte Keycloak", description = "Servira pour /me quand la sécurité sera branchée. 200, 404")
    public PatientResponse getByKeycloakId(@PathVariable String keycloakId) {
        return service.getByKeycloakId(keycloakId);
    }

    @GetMapping
    @Operation(summary = "Lister les patients", description = "Pagination : ?page=0&size=10&sort=lastName ; recherche : ?search=ben")
    public Page<PatientResponse> getAll(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getAll(search, pageable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un patient", description = "200, 400, 404, 409")
    public PatientResponse update(@PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un patient", description = "204, 404")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
