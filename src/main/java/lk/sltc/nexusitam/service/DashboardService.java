package lk.sltc.nexusitam.service;

import lk.sltc.nexusitam.dto.DashboardStatsDTO;
import lk.sltc.nexusitam.model.enums.AntivirusStatus;
import lk.sltc.nexusitam.model.enums.AssetStatus;
import lk.sltc.nexusitam.model.enums.DeviceType;
import lk.sltc.nexusitam.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final AssetRepository assetRepository;

    public DashboardStatsDTO getDashboardStats() {
        long total = assetRepository.count();
        long available = assetRepository.countByStatus(AssetStatus.AVAILABLE);
        long active = assetRepository.countByStatus(AssetStatus.ACTIVE);
        long transferred = assetRepository.countByStatus(AssetStatus.TRANSFER);
        long faulty = assetRepository.countByStatus(AssetStatus.NOT_WORKING);
        long inTransit = assetRepository.countByStatus(AssetStatus.IN_TRANSIT);
        long examQuarantine = assetRepository.countByIsExamQuarantineTrue();
        long computeUnits = assetRepository.countComputeUnits();

        long avActivated = assetRepository.countByAntivirusStatus(AntivirusStatus.ACTIVATED);
        long avNotActivated = assetRepository.countByAntivirusStatus(AntivirusStatus.NOT_ACTIVATED);
        double avComplianceRate = total > 0 ? ((double) avActivated / total) * 100.0 : 0.0;

        // Breakdown by department
        Map<String, Long> deptMap = new HashMap<>();
        List<Object[]> deptResults = assetRepository.countByDepartmentBreakdown();
        for (Object[] row : deptResults) {
            String dept = (String) row[0];
            Long count = (Long) row[1];
            deptMap.put(dept, count);
        }

        // Breakdown by location
        Map<String, Long> locMap = new HashMap<>();
        List<Object[]> locResults = assetRepository.countByLocationBreakdown();
        for (Object[] row : locResults) {
            String loc = (String) row[0];
            Long count = (Long) row[1];
            locMap.put(loc, count);
        }

        // Breakdown by device type
        Map<String, Long> typeMap = new HashMap<>();
        for (DeviceType type : DeviceType.values()) {
            long c = assetRepository.countByDeviceType(type);
            if (c > 0) {
                typeMap.put(type.name(), c);
            }
        }

        return DashboardStatsDTO.builder()
                .totalAssets(total)
                .availableAssets(available)
                .activeAssets(active)
                .transferredAssets(transferred)
                .faultyAssets(faulty)
                .inTransitAssets(inTransit)
                .examQuarantineUnits(examQuarantine)
                .computeUnits(computeUnits)
                .antivirusActivated(avActivated)
                .antivirusNotActivated(avNotActivated)
                .antivirusComplianceRate(Math.round(avComplianceRate * 10.0) / 10.0)
                .departmentDistribution(deptMap)
                .locationDistribution(locMap)
                .deviceTypeDistribution(typeMap)
                .build();
    }
}
