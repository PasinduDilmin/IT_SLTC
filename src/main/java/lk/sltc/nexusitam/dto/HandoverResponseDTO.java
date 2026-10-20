package lk.sltc.nexusitam.dto;

import lk.sltc.nexusitam.model.HandoverLog;
import lk.sltc.nexusitam.model.enums.CampusLocation;
import lk.sltc.nexusitam.model.enums.HandoverStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HandoverResponseDTO {

    private Long id;
    private Long assetId;
    private String assetTag;
    private String serialNumber;
    private String assetModel;
    private String custodianName;
    private String custodianEmail;
    private String staffOrStudentId;
    private CampusLocation targetCampus;
    private String department;
    private LocalDate handoverDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private String accessories;
    private String memo;
    private HandoverStatus status;
    private String digitalSignToken;
    private String authorizedBy;
    private LocalDateTime createdAt;

    public static HandoverResponseDTO fromEntity(HandoverLog log) {
        if (log == null) return null;
        return HandoverResponseDTO.builder()
                .id(log.getId())
                .assetId(log.getAsset() != null ? log.getAsset().getId() : null)
                .assetTag(log.getAsset() != null ? log.getAsset().getAssetTag() : null)
                .serialNumber(log.getAsset() != null ? log.getAsset().getSerialNumber() : null)
                .assetModel(log.getAsset() != null ? log.getAsset().getModel() : null)
                .custodianName(log.getCustodianName())
                .custodianEmail(log.getCustodianEmail())
                .staffOrStudentId(log.getStaffOrStudentId())
                .targetCampus(log.getTargetCampus())
                .department(log.getDepartment())
                .handoverDate(log.getHandoverDate())
                .expectedReturnDate(log.getExpectedReturnDate())
                .actualReturnDate(log.getActualReturnDate())
                .accessories(log.getAccessories())
                .memo(log.getMemo())
                .status(log.getStatus())
                .digitalSignToken(log.getDigitalSignToken())
                .authorizedBy(log.getAuthorizedBy())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
