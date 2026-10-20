package lk.sltc.nexusitam.service;

import lk.sltc.nexusitam.dto.HandoverRequestDTO;
import lk.sltc.nexusitam.dto.HandoverResponseDTO;
import lk.sltc.nexusitam.model.Asset;
import lk.sltc.nexusitam.model.HandoverLog;
import lk.sltc.nexusitam.model.enums.AssetStatus;
import lk.sltc.nexusitam.model.enums.HandoverStatus;
import lk.sltc.nexusitam.repository.AssetRepository;
import lk.sltc.nexusitam.repository.HandoverLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HandoverService {

    private final HandoverLogRepository handoverLogRepository;
    private final AssetRepository assetRepository;

    public HandoverResponseDTO initiateHandover(HandoverRequestDTO request) {
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new NoSuchElementException("Asset not found with ID: " + request.getAssetId()));

        // Save previous custodian
        asset.setPreviousCustodian(asset.getCurrentCustodian());
        asset.setCurrentCustodian(request.getCustodianName());
        asset.setStatus(AssetStatus.TRANSFER);
        if (request.getTargetCampus() != null) {
            asset.setCampusLocation(request.getTargetCampus());
        }
        if (request.getDepartment() != null) {
            asset.setDepartment(request.getDepartment());
        }

        // Generate digital cryptographic sign token
        String signToken = "NEX-SIGN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        HandoverLog logEntry = HandoverLog.builder()
                .asset(asset)
                .custodianName(request.getCustodianName())
                .custodianEmail(request.getCustodianEmail())
                .staffOrStudentId(request.getStaffOrStudentId())
                .targetCampus(request.getTargetCampus() != null ? request.getTargetCampus() : asset.getCampusLocation())
                .department(request.getDepartment() != null ? request.getDepartment() : asset.getDepartment())
                .handoverDate(request.getHandoverDate() != null ? request.getHandoverDate() : LocalDate.now())
                .expectedReturnDate(request.getExpectedReturnDate())
                .accessories(request.getAccessories() != null ? request.getAccessories() : "Acer 65W Type-C AC Adapter, Carry Bag")
                .memo(request.getMemo())
                .status(HandoverStatus.ACTIVE)
                .digitalSignToken(signToken)
                .authorizedBy(request.getAuthorizedBy() != null ? request.getAuthorizedBy() : "IT Admin Lead")
                .build();

        assetRepository.save(asset);
        HandoverLog savedLog = handoverLogRepository.save(logEntry);
        log.info("Handover authorized: asset={}, custodian={}, token={}", asset.getAssetTag(), request.getCustodianName(), signToken);

        return HandoverResponseDTO.fromEntity(savedLog);
    }

    public HandoverResponseDTO returnAsset(Long logId, String returnNotes) {
        HandoverLog logEntry = handoverLogRepository.findById(logId)
                .orElseThrow(() -> new NoSuchElementException("Handover log not found with ID: " + logId));

        logEntry.setStatus(HandoverStatus.RETURNED);
        logEntry.setActualReturnDate(LocalDate.now());
        if (returnNotes != null && !returnNotes.isBlank()) {
            logEntry.setMemo((logEntry.getMemo() != null ? logEntry.getMemo() + " | Return: " : "Return: ") + returnNotes);
        }

        Asset asset = logEntry.getAsset();
        if (asset != null) {
            asset.setPreviousCustodian(asset.getCurrentCustodian());
            asset.setCurrentCustodian("Unassigned (Depository)");
            asset.setStatus(AssetStatus.AVAILABLE);
            assetRepository.save(asset);
        }

        HandoverLog updated = handoverLogRepository.save(logEntry);
        log.info("Asset return confirmed: logId={}, assetTag={}", logId, asset != null ? asset.getAssetTag() : "N/A");
        return HandoverResponseDTO.fromEntity(updated);
    }

    @Transactional(readOnly = true)
    public Page<HandoverResponseDTO> getHandoverLogs(Pageable pageable) {
        return handoverLogRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(HandoverResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<HandoverResponseDTO> getRecentHandoverLogs() {
        return handoverLogRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .map(HandoverResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HandoverResponseDTO> getLogsByAssetId(Long assetId) {
        return handoverLogRepository.findByAssetIdOrderByCreatedAtDesc(assetId).stream()
                .map(HandoverResponseDTO::fromEntity)
                .toList();
    }
}
