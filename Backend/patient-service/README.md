# patient-service

Microservice MediCare de gestion des patients. Responsable : **Emna Laouini**.

| | |
|---|---|
| Port | `8081` |
| Base | MySQL `medicare_patient_db` (créée automatiquement) |
| Swagger | http://localhost:8081/swagger-ui.html |
| Santé | http://localhost:8081/actuator/health |

## Lancer en local

1. Démarrer MySQL (XAMPP : bouton **Start** sur MySQL). Par défaut : `root` sans mot de passe.
2. Ouvrir le dossier `Backend/patient-service` dans IntelliJ (il détecte le `pom.xml`).
3. Lancer `PatientServiceApplication`.

Autre utilisateur ou mot de passe MySQL : variables d'environnement `DB_USER` et `DB_PASSWORD` (ou modifier `application.yml`).

## Endpoints

| Méthode | Endpoint | Description | Codes |
|---|---|---|---|
| POST | `/api/patients` | Créer un patient | 201, 400, 409 |
| GET | `/api/patients?search=&page=&size=&sort=` | Lister / rechercher (paginé) | 200 |
| GET | `/api/patients/{id}` | Détails (appelé par les autres services) | 200, 404 |
| GET | `/api/patients/{id}/exists` | Vérifier l'existence | 200 |
| GET | `/api/patients/keycloak/{keycloakId}` | Patient lié à un compte Keycloak | 200, 404 |
| PUT | `/api/patients/{id}` | Modifier | 200, 400, 404, 409 |
| DELETE | `/api/patients/{id}` | Supprimer | 204, 404 |

Exemples prêts à lancer : [`patient-api.http`](patient-api.http).

## Structure

```
controller/  PatientController      → endpoints REST
service/     PatientService(Impl)   → logique métier
repository/  PatientRepository      → Spring Data JPA
entity/      Patient                → table patients
dto/         PatientRequest/Response → contrat d'API (records + validation)
mapper/      PatientMapper
exception/   GlobalExceptionHandler → erreurs JSON communes (404, 409, 400)
```

## Prochaines étapes

- [ ] Activer Eureka (`EUREKA_ENABLED=true`) quand `eureka-server` sera prêt
- [ ] Brancher Config Server
- [ ] Sécurité Keycloak (Resource Server JWT, `@PreAuthorize`, endpoint `/me`)
- [ ] Dockerfile
