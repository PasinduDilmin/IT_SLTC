package lk.sltc.nexusitam.dto;

import lk.sltc.nexusitam.model.Asset;
import lk.sltc.nexusitam.model.enums.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetResponseDTO {

    private Long id;
    private String assetTag;
    private String serialNumber;
    private String model;
    private DeviceType deviceType;
    private AssetStatus status;
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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AssetResponseDTO fromEntity(Asset asset) {
        if (asset == null) return null;
        return AssetResponseDTO.builder()
                .id(asset.getId())
                .assetTag(asset.getAssetTag())
                .serialNumber(asset.getSerialNumber())
                .model(asset.getModel())
                .deviceType(asset.getDeviceType())
                .status(asset.getStatus())
                .campusLocation(asset.getCampusLocation())
                .specificLocation(asset.getSpecificLocation())
                .department(asset.getDepartment())
                .currentCustodian(asset.getCurrentCustodian())
                .previousCustodian(asset.getPreviousCustodian())
                .antivirusStatus(asset.getAntivirusStatus())
                .processor(asset.getProcessor())
                .ram(asset.getRam())
                .storage(asset.getStorage())
                .operatingSystem(asset.getOperatingSystem())
                .hasBag(asset.getHasBag())
                .notes(asset.getNotes())
                .unitIdentifier(asset.getUnitIdentifier())
                .batteryHealthCycle(asset.getBatteryHealthCycle())
                .batteryHealthPercent(asset.getBatteryHealthPercent())
                .isExamQuarantine(asset.getIsExamQuarantine())
                .ipAddress(asset.getIpAddress())
                .macAddress(asset.getMacAddress())
                .pairedMonitorModel(asset.getPairedMonitorModel())
                .pairedMonitorSerial(asset.getPairedMonitorSerial())
                .createdAt(asset.getCreatedAt())
                .updatedAt(asset.getUpdatedAt())
                .build();
    }
}
