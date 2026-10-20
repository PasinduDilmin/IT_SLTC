package lk.sltc.nexusitam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.sltc.nexusitam.model.enums.CampusLocation;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HandoverRequestDTO {

    @NotNull(message = "Asset ID is required")
    private Long assetId;

    @NotBlank(message = "Custodian name is required")
    private String custodianName;

    private String custodianEmail;

    private String staffOrStudentId;

    private CampusLocation targetCampus;

    private String department;

    @NotNull(message = "Handover date is required")
    private LocalDate handoverDate;

    private LocalDate expectedReturnDate;

    private String accessories; // e.g. "Acer 65W Type-C AC Adapter, Carry Bag"

    private String memo;

    private String authorizedBy;
}
