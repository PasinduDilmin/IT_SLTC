# 🏛️ SLTC-IT - Campus Enterprise IT Asset Management System

A production-grade **Spring Boot 3.x (Java 21)** backend application engineered for IT asset lifecycle governance, multi-campus fleet tracking, computer lab management, and cryptographic custody voucher auditing across **SLTC (Sri Lanka Technological Campus)**.

---

## 🛠️ Technology Stack

- **Framework**: Spring Boot `3.4.2`
- **Language**: Java `21` (LTS)
- **Data Persistence**: Spring Data JPA & Hibernate
- **Database Support**:
  - **MySQL 8.x** (Configured via `application-mysql.properties`)
  - **H2 In-Memory Database** (Configured as default `dev` profile with Web Console for instant zero-configuration testing)
- **Validation**: Jakarta Bean Validation (`spring-boot-starter-validation`)
- **Code Generation**: Project Lombok
- **Testing**: JUnit 5, MockMvc, AssertJ, Spring Boot Test
- **Build System**: Apache Maven with Maven Wrapper (`mvnw` / `mvnw.cmd`)

---

## 📂 Project Architecture

```
IT_SLTC/
├── .mvn/wrapper/                  # Maven wrapper binaries & properties
├── src/
│   ├── main/
│   │   ├── java/lk/sltc/nexusitam/
│   │   │   ├── config/            # CORS configuration & initial data seeder
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/        # REST API endpoints
│   │   │   │   ├── AssetController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── FleetController.java
│   │   │   │   ├── HandoverController.java
│   │   │   │   └── LabController.java
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   │   ├── AssetRequestDTO.java
│   │   │   │   ├── AssetResponseDTO.java
│   │   │   │   ├── DashboardStatsDTO.java
│   │   │   │   ├── HandoverRequestDTO.java
│   │   │   │   ├── HandoverResponseDTO.java
│   │   │   │   └── LabStationDTO.java
│   │   │   ├── model/             # JPA Entities & Enums
│   │   │   │   ├── Asset.java
│   │   │   │   ├── HandoverLog.java
│   │   │   │   └── enums/
│   │   │   │       ├── AntivirusStatus.java
│   │   │   │       ├── AssetStatus.java
│   │   │   │       ├── CampusLocation.java
│   │   │   │       ├── DeviceType.java
│   │   │   │       └── HandoverStatus.java
│   │   │   ├── repository/        # Spring Data JPA Repositories
│   │   │   │   ├── AssetRepository.java
│   │   │   │   └── HandoverLogRepository.java
│   │   │   ├── service/           # Business logic & services
│   │   │   │   ├── AssetService.java
│   │   │   │   ├── DashboardService.java
│   │   │   │   ├── HandoverService.java
│   │   │   │   └── LabService.java
│   │   │   └── NexusitamApplication.java # Spring Boot entry point
│   │   └── resources/
│   │       ├── application.properties        # Main config & dev H2 profile
│   │       └── application-mysql.properties  # MySQL database profile
│   └── test/
│       └── java/lk/sltc/nexusitam/
│           ├── AssetControllerTest.java      # MockMvc integration tests
│           └── NexusitamApplicationTests.java
├── mvnw / mvnw.cmd               # Maven wrappers for Linux/Mac and Windows
├── pom.xml                       # Project Object Model dependencies
└── README.md
```

---

## 🚀 How to Run the Application

### Prerequisites
- **Java 21** (JDK installed at `C:\Users\SLTC\.jdk\jdk-21.0.12.1+1` or configured in system `JAVA_HOME`)
- **MySQL Server** (e.g. XAMPP MySQL running on port `3306`)

### 1. Run with MySQL Database (Default Profile)
The application defaults to `mysql` profile and connects to your local MySQL (e.g., XAMPP on port 3306):
```powershell
.\mvnw.cmd spring-boot:run
```
- **Database**: `nexusitam_db` (created automatically if not present)
- **Host**: `localhost:3306`
- **Username / Password**: Defaults to `root` with no password (standard XAMPP setup).
- **Environment Overrides (Optional)**:
  - `DB_URL` (e.g. `jdbc:mysql://localhost:3306/nexusitam_db`)
  - `DB_USERNAME`
  - `DB_PASSWORD`

### 2. Run in Development Mode (In-Memory H2 Database)
If you wish to run without a local MySQL server:
```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```
- **H2 Interactive Web Console**: `http://localhost:8080/h2-console`
  - **JDBC URL**: `jdbc:h2:mem:nexusitam_db`
  - **User**: `sa`
  - **Password**: *(leave blank)*

---

## 📡 REST API Reference

### 1. Executive Telemetry & Dashboard (`/api/dashboard`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/dashboard/stats` | Returns aggregate counts, compute units, antivirus compliance %, and campus distribution breakdown. |

### 2. Master Asset Registry (`/api/assets`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/assets` | Filterable, paginated query: `?search=...&status=AVAILABLE&campus=PADUKKA_MAIN&page=0&size=10` |
| `GET` | `/api/assets/all` | Returns all registered campus assets. |
| `GET` | `/api/assets/{id}` | Retrieve asset by numerical ID. |
| `GET` | `/api/assets/serial/{serialNumber}` | Lookup asset by hardware serial number (e.g. `NXJ0LSG00243403CC62N00`). |
| `GET` | `/api/assets/tag/{assetTag}` | Lookup asset by institutional tag (e.g. `NEX-LP-BAIT01`). |
| `POST` | `/api/assets` | Register a new asset (JSON body validated). |
| `PUT` | `/api/assets/{id}` | Update existing asset specifications and status. |
| `DELETE` | `/api/assets/{id}` | Remove asset record from inventory. |

### 3. Acer Aspire Go 14 Fleet Tracker (`/api/fleet`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/fleet/laptops` | Retrieve all laptops provisioned in the student/staff fleet pool. |
| `GET` | `/api/fleet/summary` | Telemetry breakdown: available, active handover, quarantined for exams, faulty units. |
| `POST` | `/api/fleet/laptops/{id}/quarantine` | Toggle exam pool readiness quarantine lockdown on a unit. |

### 4. Computer Laboratory 01 & DAIT Fleet (`/api/labs`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/labs/computer-lab-01/stations` | List workstations (filterable by row, e.g. `?row=row-1`). |
| `GET` | `/api/labs/computer-lab-01/matrix` | Full physical layout matrix grouped by rows (Rows 1–6). |
| `GET` | `/api/labs/computer-lab-01/stats` | Lab hardware telemetry, uptime, network gateway and display pairings. |

### 5. Custody Handover & Return Ledger (`/api/handovers`)
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/handovers` | Initiate handover voucher, allocate custodian, and generate cryptographic digital token. |
| `POST` | `/api/handovers/{id}/return` | Check-in returned equipment back to depository vault. |
| `GET` | `/api/handovers` | Paginated audit trail of all handovers. |
| `GET` | `/api/handovers/recent` | Top 10 most recent handover transactions. |
| `GET` | `/api/handovers/asset/{assetId}` | Full custody history for a specific device. |

---

## 🧪 Testing

Execute the automated test suite (MockMvc web integration tests):
```powershell
.\mvnw.cmd test
```

---

## 🛡️ License & Copyright
Developed for **SLTC Campus Enterprise IT Operations**. All rights reserved.
