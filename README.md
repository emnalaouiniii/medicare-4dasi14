# MediCare – Plateforme distribuée de gestion de clinique

**Projet AWD (Architecture Web Distribuée)** · Classe **4DAS-I14** · Groupe **gX**

MediCare est une plateforme web basée sur une architecture microservices qui digitalise le parcours patient dans une clinique : prise de rendez-vous, gestion du planning des médecins, dossier médical, ordonnances, facturation et notifications.

---

## Problématique

Dans beaucoup de cliniques, la prise de rendez-vous se fait par téléphone, les dossiers médicaux sont dispersés (papier, fichiers locaux) et la facturation est manuelle. Cela entraîne des pertes de temps, des erreurs et un manque de suivi pour le patient.

MediCare centralise ces processus dans une plateforme unique, scalable et sécurisée, où chaque domaine métier est géré par un microservice indépendant.

## Acteurs

| Acteur | Rôle |
|---|---|
| Patient | Prend rendez-vous, consulte son dossier, ses ordonnances et ses factures |
| Médecin | Gère son planning, consulte/complète les dossiers, rédige les ordonnances |
| Pharmacien | Consulte et délivre les ordonnances |
| Administrateur | Gère les utilisateurs, les médecins, les spécialités et la facturation |

## Architecture

```
Frontend (Angular)
       │
   API Gateway (Spring Cloud Gateway) ── Keycloak (JWT, rôles)
       │                              └─ Eureka + Config Server
       │
 ┌─────┴──────────────────────────────────────────────┐
 │ Patient │ Doctor │ Appointment │ MedicalRecord │ Prescription │ Billing │
 └─────┬──────────────────────────────────────────────┘
       │ événements
     Kafka ──► Notification (microservice commun)
```

Le diagramme complet se trouve dans [`Documentation/Diagrams`](Documentation/Diagrams).

## Microservices

| Microservice | Responsabilité | Base de données | Responsable |
|---|---|---|---|
| `patient-service` | Profils et informations des patients | MySQL | Emna Laouini |
| `doctor-service` | Médecins, spécialités, disponibilités / planning | MySQL | Dhouha Belhaj |
| `appointment-service` | Prise, modification et annulation des rendez-vous | MySQL | Eslem Zen |
| `medical-record-service` | Dossier médical : consultations, antécédents, documents | MongoDB | Sarra Akremi |
| `prescription-service` | Ordonnances et médicaments | PostgreSQL | Hiba Dogui |
| `billing-service` | Factures et paiements | PostgreSQL | Maram Ghazel |
| `notification-service` (commun) | Emails / notifications déclenchés par événements | MongoDB | Toute l'équipe |

### Services d'infrastructure (toute l'équipe)

| Service | Rôle |
|---|---|
| `api-gateway` | Point d'entrée unique, routage, validation du JWT |
| `eureka-server` | Découverte des services |
| `config-server` | Configuration centralisée |
| Keycloak | Authentification, autorisation (rôles PATIENT, DOCTOR, PHARMACIST, ADMIN) |

## Communications

**Synchrones (REST / OpenFeign)**
- `appointment-service` → `doctor-service` : vérifier la disponibilité d'un médecin
- `appointment-service` → `patient-service` : vérifier l'existence du patient
- `prescription-service` → `patient-service` : vérifier le patient

**Asynchrones (Kafka)**
| Événement | Producteur | Consommateurs |
|---|---|---|
| `AppointmentBooked` | appointment-service | notification-service, billing-service |
| `AppointmentCancelled` | appointment-service | notification-service, doctor-service |
| `PrescriptionCreated` | prescription-service | medical-record-service, notification-service |
| `PaymentCompleted` | billing-service | notification-service |

## Technologies

- **Backend** : Java 17, Spring Boot 3, Spring Cloud (Gateway, Eureka, Config, OpenFeign)
- **Sécurité** : Keycloak, OAuth2 / JWT
- **Messaging** : Apache Kafka
- **Bases de données** : MySQL, PostgreSQL, MongoDB
- **Frontend** : Angular
- **DevOps** : Docker, Docker Compose, Git / GitHub
- **Gestion de projet** : Jira (ou Trello)

## Structure du dépôt

```
medicare-4dasi14-gX/
├── Backend/
│   ├── api-gateway/
│   ├── eureka-server/
│   ├── config-server/
│   ├── patient-service/
│   ├── doctor-service/
│   ├── appointment-service/
│   ├── medical-record-service/
│   ├── prescription-service/
│   ├── billing-service/
│   └── notification-service/
├── Frontend/
├── Documentation/
│   ├── Presentation/
│   ├── Diagrams/
│   └── Dossier-technique/
└── README.md
```

## Organisation Git

- `main` : version stable (présentations, validations)
- `develop` : intégration
- `feature/<service>-<fonctionnalité>` : une branche par fonctionnalité (ex. `feature/appointment-booking`)
- Merge vers `develop` via Pull Request, relue par au moins un autre membre

## Équipe

| Membre | Microservice |
|---|---|
| Emna Laouini | patient-service |
| Dhouha Belhaj | doctor-service |
| Eslem Zen | appointment-service |
| Sarra Akremi | medical-record-service |
| Hiba Dogui | prescription-service |
| Maram Ghazel | billing-service |
| Toute l'équipe | notification-service, gateway, eureka, config, Keycloak |
