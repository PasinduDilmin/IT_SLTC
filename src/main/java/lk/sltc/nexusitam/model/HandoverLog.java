package lk.sltc.nexusitam.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.sltc.nexusitam.model.enums.CampusLocation;
import lk.sltc.nexusitam.model.enums.HandoverStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Audit ledger entry recording custody handovers, peripheral transfers, and return receipts.
 */
@Entity
@Table(name = "handover_logs", indexes = {
    @Index(name = "idx_handover_asset", columnList = "asset_id"),
    @Index(name = "idx_handover_status", columnList = "status"),
    @Index(name = "idx_custodian_email", columnList = "custodianEmail")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HandoverLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Asset reference is mandatory")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @NotBlank(message = "Custodian name is mandatory")
    @Column(nullable = false, length = 100)
    private String custodianName;

    @Column(length = 100)
    private String custodianEmail;

    @Column(length = 50)
    private String staffOrStudentId; // e.g. "SLTC-NA-1048"

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private CampusLocation targetCampus;

    @Column(length = 100)
    private String department;

    @NotNull(message = "Handover date is mandatory")
    @Column(nullable = false)
    private LocalDate handoverDate;

    private LocalDate expectedReturnDate;

    private LocalDate actualReturnDate;

    @Column(length = 255)
    private String accessories; // e.g. "Acer 65W Type-C AC Adapter, Original Carry Bag"

    @Column(length = 500)
    private String memo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private HandoverStatus status = HandoverStatus.ACTIVE;

    @Column(length = 100)
    private String digitalSignToken; // Cryptographic voucher token

    @Column(length = 100)
    private String authorizedBy; // e.g. "SysAdmin Lead"

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = HandoverStatus.ACTIVE;
    }
}
