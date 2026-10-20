package lk.sltc.nexusitam.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.sltc.nexusitam.model.enums.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Core entity representing a physical IT asset across SLTC campus infrastructure.
 */
@Entity
@Table(name = "assets", indexes = {
    @Index(name = "idx_asset_tag", columnList = "assetTag"),
    @Index(name = "idx_serial_number", columnList = "serialNumber"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_campus_location", columnList = "campusLocation"),
    @Index(name = "idx_department", columnList = "department")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Asset tag is mandatory")
    @Column(nullable = false, unique = true, length = 50)
    private String assetTag;

    @NotBlank(message = "Serial number is mandatory")
    @Column(nullable = false, unique = true, length = 100)
    private String serialNumber;

    @NotBlank(message = "Model name is mandatory")
    @Column(nullable = false, length = 100)
    private String model;

    @NotNull(message = "Device type is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DeviceType deviceType;

    @NotNull(message = "Asset status is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AssetStatus status;

    @NotNull(message = "Campus location is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CampusLocation campusLocation;

    @Column(length = 100)
    private String specificLocation; // e.g. "Padukka - Bldg B - Room L1-04", "Trace GF1"

    @Column(length = 100)
    private String department; // e.g. "IT Dept", "Sales", "HR", "Academic Computing"

    @Column(length = 100)
    private String currentCustodian;

    @Column(length = 100)
    private String previousCustodian;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    @Builder.Default
    private AntivirusStatus antivirusStatus = AntivirusStatus.NOT_ACTIVATED;

    // Hardware specifications
    @Column(length = 100)
    private String processor; // e.g. "Intel Core i3-N305", "Intel Core i7-14700"

    @Column(length = 50)
    private String ram; // e.g. "8GB LPDDR5", "16GB DDR5"

    @Column(length = 50)
    private String storage; // e.g. "512GB PCIe NVMe"

    @Column(length = 100)
    private String operatingSystem; // e.g. "Windows 11 Pro Edu"

    @Builder.Default
    private Boolean hasBag = false;

    @Column(length = 500)
    private String notes;

    // Fleet / Lab metadata
    @Column(length = 20)
    private String unitIdentifier; // e.g. "#01", "#49", "Row 1 Station 01"

    private Integer batteryHealthCycle; // e.g. 14 cycles

    private Double batteryHealthPercent; // e.g. 98.4%

    @Builder.Default
    private Boolean isExamQuarantine = false;

    // Network & Lab station specifics
    @Column(length = 50)
    private String ipAddress;

    @Column(length = 50)
    private String macAddress;

    @Column(length = 100)
    private String pairedMonitorModel;

    @Column(length = 100)
    private String pairedMonitorSerial;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.hasBag == null) this.hasBag = false;
        if (this.isExamQuarantine == null) this.isExamQuarantine = false;
        if (this.antivirusStatus == null) this.antivirusStatus = AntivirusStatus.NOT_ACTIVATED;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
