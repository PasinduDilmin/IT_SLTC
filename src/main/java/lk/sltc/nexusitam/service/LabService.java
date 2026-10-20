package lk.sltc.nexusitam.service;

import lk.sltc.nexusitam.dto.LabStationDTO;
import lk.sltc.nexusitam.model.Asset;
import lk.sltc.nexusitam.model.enums.DeviceType;
import lk.sltc.nexusitam.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LabService {

    private final AssetRepository assetRepository;

    public List<LabStationDTO> getLabWorkstations(String rowFilter) {
        List<Asset> labAssets = assetRepository.findByDeviceType(DeviceType.DESKTOP).stream()
                .filter(a -> a.getUnitIdentifier() != null && a.getUnitIdentifier().contains("Station"))
                .toList();

        return labAssets.stream()
                .filter(a -> {
                    if (rowFilter == null || rowFilter.equalsIgnoreCase("all")) return true;
                    // match "row-1", "Row 1", etc.
                    String clean = rowFilter.replace("-", " ").toLowerCase();
                    return a.getUnitIdentifier().toLowerCase().contains(clean);
                })
                .map(this::toLabStationDTO)
                .toList();
    }

    public Map<String, List<LabStationDTO>> getLabMatrix() {
        List<Asset> labAssets = assetRepository.findByDeviceType(DeviceType.DESKTOP).stream()
                .filter(a -> a.getUnitIdentifier() != null && a.getUnitIdentifier().contains("Station"))
                .toList();

        return labAssets.stream()
                .map(this::toLabStationDTO)
                .collect(Collectors.groupingBy(LabStationDTO::getBenchRow));
    }

    private LabStationDTO toLabStationDTO(Asset a) {
        // Parse row name from unitIdentifier like "Row 1 Station 01"
        String rowName = "General";
        String stationId = "S-01";
        if (a.getUnitIdentifier() != null && a.getUnitIdentifier().contains("Station")) {
            String[] parts = a.getUnitIdentifier().split("Station");
            rowName = parts[0].trim();
            if (parts.length > 1) {
                stationId = "S-" + parts[1].trim();
            }
        }

        return LabStationDTO.builder()
                .stationId(stationId)
                .benchRow(rowName)
                .stationName(a.getUnitIdentifier())
                .pcSerial(a.getSerialNumber())
                .model(a.getModel())
                .monitorModel(a.getPairedMonitorModel() != null ? a.getPairedMonitorModel() : "Acer 27\" Full HD LED")
                .monitorSerial(a.getPairedMonitorSerial() != null ? a.getPairedMonitorSerial() : "MMV1BSG0024301424D3W01")
                .ipAddress(a.getIpAddress() != null ? a.getIpAddress() : "10.140.2.11")
                .processor(a.getProcessor() != null ? a.getProcessor() : "i7-14700")
                .ram(a.getRam() != null ? a.getRam() : "16GB DDR5")
                .storage(a.getStorage() != null ? a.getStorage() : "NVMe 512 GB")
                .osVersion(a.getOperatingSystem() != null ? a.getOperatingSystem() : "Windows 11 Pro")
                .status(a.getStatus() == lk.sltc.nexusitam.model.enums.AssetStatus.NOT_WORKING ? "Maintenance" : "Healthy")
                .build();
    }
}
