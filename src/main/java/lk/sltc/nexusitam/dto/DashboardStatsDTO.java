package lk.sltc.nexusitam.dto;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {

    private long totalAssets;
    private long availableAssets;
    private long activeAssets;
    private long transferredAssets;
    private long faultyAssets;
    private long inTransitAssets;
    private long examQuarantineUnits;
    private long computeUnits;

    // Security & Compliance
    private long antivirusActivated;
    private long antivirusNotActivated;
    private double antivirusComplianceRate; // percentage

    // Distribution
    private Map<String, Long> locationDistribution;
    private Map<String, Long> departmentDistribution;
    private Map<String, Long> deviceTypeDistribution;
}
