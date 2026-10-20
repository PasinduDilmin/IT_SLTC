package lk.sltc.nexusitam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.sltc.nexusitam.model.enums.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequestDTO {

    @NotBlank(message = "Asset tag is required")
    private String assetTag;

    @NotBlank(message = "Serial number is required")
    private String serialNumber;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Device type is required")
    private DeviceType deviceType;

    @NotNull(message = "Status is required")
    private AssetStatus status;

    @NotNull(message = "Campus location is required")
    private CampusLocation campusLocation;

    private String specificLocation;
    private String department;
    private String currentCustodian;
    private String previousCustodian;
    private AntivirusStatus antivirusStatus;

    // Hardware specifications
    private String processor;
    private String ram;
    private String storage;
    private String operatingSystem;
    private Boolean hasBag;
    private String notes;

    // Fleet / Lab specifics
    private String unitIdentifier;
    private Integer batteryHealthCycle;
    private Double batteryHealthPercent;
    private Boolean isExamQuarantine;
    private String ipAddress;
    private String macAddress;
    private String pairedMonitorModel;
    private String pairedMonitorSerial;
}
