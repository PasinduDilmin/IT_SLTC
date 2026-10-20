package lk.sltc.nexusitam.service;

import lk.sltc.nexusitam.dto.AssetRequestDTO;
import lk.sltc.nexusitam.dto.AssetResponseDTO;
import lk.sltc.nexusitam.model.Asset;
import lk.sltc.nexusitam.model.enums.*;
import lk.sltc.nexusitam.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AssetService {

    private final AssetRepository assetRepository;

    @Transactional(readOnly = true)
    public Page<AssetResponseDTO> getAssets(
            String query,
            AssetStatus status,
            CampusLocation campus,
            DeviceType deviceType,
            String department,
            Boolean hasBag,
            Pageable pageable) {

        return assetRepository.searchAssets(query, status, campus, deviceType, department, hasBag, pageable)
                .map(AssetResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<AssetResponseDTO> getAllAssets() {
        return assetRepository.findAll().stream()
                .map(AssetResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AssetResponseDTO getAssetById(Long id) {
        return assetRepository.findById(id)
                .map(AssetResponseDTO::fromEntity)
                .orElseThrow(() -> new NoSuchElementException("Asset not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public AssetResponseDTO getAssetBySerial(String serialNumber) {
        return assetRepository.findBySerialNumber(serialNumber)
                .map(AssetResponseDTO::fromEntity)
                .orElseThrow(() -> new NoSuchElementException("Asset not found with Serial: " + serialNumber));
    }

    @Transactional(readOnly = true)
    public AssetResponseDTO getAssetByTag(String assetTag) {
        return assetRepository.findByAssetTag(assetTag)
                .map(AssetResponseDTO::fromEntity)
                .orElseThrow(() -> new NoSuchElementException("Asset not found with Tag: " + assetTag));
    }

    public AssetResponseDTO createAsset(AssetRequestDTO request) {
        if (assetRepository.findByAssetTag(request.getAssetTag()).isPresent()) {
            throw new IllegalArgumentException("Asset tag already exists: " + request.getAssetTag());
        }
        if (assetRepository.findBySerialNumber(request.getSerialNumber()).isPresent()) {
            throw new IllegalArgumentException("Serial number already registered: " + request.getSerialNumber());
        }

        Asset asset = Asset.builder()
                .assetTag(request.getAssetTag())
                .serialNumber(request.getSerialNumber())
                .model(request.getModel())
                .deviceType(request.getDeviceType())
                .status(request.getStatus())
                .campusLocation(request.getCampusLocation())
                .specificLocation(request.getSpecificLocation())
                .department(request.getDepartment())
                .currentCustodian(request.getCurrentCustodian())
                .previousCustodian(request.getPreviousCustodian())
                .antivirusStatus(request.getAntivirusStatus() != null ? request.getAntivirusStatus() : AntivirusStatus.NOT_ACTIVATED)
                .processor(request.getProcessor())
                .ram(request.getRam())
                .storage(request.getStorage())
                .operatingSystem(request.getOperatingSystem())
                .hasBag(request.getHasBag() != null ? request.getHasBag() : false)
                .notes(request.getNotes())
                .unitIdentifier(request.getUnitIdentifier())
                .batteryHealthCycle(request.getBatteryHealthCycle())
                .batteryHealthPercent(request.getBatteryHealthPercent())
                .isExamQuarantine(request.getIsExamQuarantine() != null ? request.getIsExamQuarantine() : false)
                .ipAddress(request.getIpAddress())
                .macAddress(request.getMacAddress())
                .pairedMonitorModel(request.getPairedMonitorModel())
                .pairedMonitorSerial(request.getPairedMonitorSerial())
                .build();

        Asset saved = assetRepository.save(asset);
        log.info("Asset created: tag={}, id={}", saved.getAssetTag(), saved.getId());
        return AssetResponseDTO.fromEntity(saved);
    }

    public AssetResponseDTO updateAsset(Long id, AssetRequestDTO request) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Asset not found with ID: " + id));

        asset.setModel(request.getModel());
        asset.setDeviceType(request.getDeviceType());
        asset.setStatus(request.getStatus());
        asset.setCampusLocation(request.getCampusLocation());
        asset.setSpecificLocation(request.getSpecificLocation());
        asset.setDepartment(request.getDepartment());
        asset.setCurrentCustodian(request.getCurrentCustodian());
        asset.setPreviousCustodian(request.getPreviousCustodian());
        if (request.getAntivirusStatus() != null) asset.setAntivirusStatus(request.getAntivirusStatus());
        asset.setProcessor(request.getProcessor());
        asset.setRam(request.getRam());
        asset.setStorage(request.getStorage());
        asset.setOperatingSystem(request.getOperatingSystem());
        if (request.getHasBag() != null) asset.setHasBag(request.getHasBag());
        asset.setNotes(request.getNotes());
        asset.setUnitIdentifier(request.getUnitIdentifier());
        asset.setBatteryHealthCycle(request.getBatteryHealthCycle());
        asset.setBatteryHealthPercent(request.getBatteryHealthPercent());
        if (request.getIsExamQuarantine() != null) asset.setIsExamQuarantine(request.getIsExamQuarantine());
        asset.setIpAddress(request.getIpAddress());
        asset.setMacAddress(request.getMacAddress());
        asset.setPairedMonitorModel(request.getPairedMonitorModel());
        asset.setPairedMonitorSerial(request.getPairedMonitorSerial());

        Asset updated = assetRepository.save(asset);
        log.info("Asset updated: tag={}, id={}", updated.getAssetTag(), updated.getId());
        return AssetResponseDTO.fromEntity(updated);
    }

    public void deleteAsset(Long id) {
        if (!assetRepository.existsById(id)) {
            throw new NoSuchElementException("Asset not found with ID: " + id);
        }
        assetRepository.deleteById(id);
        log.info("Asset deleted with ID: {}", id);
    }

    public AssetResponseDTO toggleExamQuarantine(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Asset not found with ID: " + id));

        boolean current = Boolean.TRUE.equals(asset.getIsExamQuarantine());
        asset.setIsExamQuarantine(!current);
        if (!current) {
            asset.setStatus(AssetStatus.QUARANTINE_EXAM);
            asset.setNotes((asset.getNotes() != null ? asset.getNotes() + " | " : "") + "Quarantined for Semester Exam Session");
        } else {
            asset.setStatus(AssetStatus.AVAILABLE);
        }

        Asset saved = assetRepository.save(asset);
        return AssetResponseDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<AssetResponseDTO> getLaptopFleet() {
        return assetRepository.findByDeviceType(DeviceType.LAPTOP).stream()
                .map(AssetResponseDTO::fromEntity)
                .toList();
    }
}
