package lk.sltc.nexusitam.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabStationDTO {
    private String stationId; // e.g. "R1-S01"
    private String benchRow;  // e.g. "Row 1"
    private String stationName; // e.g. "Row 1 Station 01"
    private String pcSerial;
    private String model;
    private String monitorModel;
    private String monitorSerial;
    private String ipAddress;
    private String processor;
    private String ram;
    private String storage;
    private String osVersion;
    private String status; // "Healthy", "Offline", "Maintenance"
}
