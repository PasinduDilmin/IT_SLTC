package lk.sltc.nexusitam.model.enums;

/**
 * Operational lifecycle statuses of assets in NexusITAM.
 */
public enum AssetStatus {
    AVAILABLE,          // In depository vault, ready for handover
    ACTIVE,             // Assigned and actively in use by staff or student pool
    TRANSFER,           // Transferred to a department or staff member
    IN_TRANSIT,         // Under physical relocation between campuses (e.g. Trace -> Padukka)
    QUARANTINE_EXAM,    // Locked in exam pool buffer with exam OS image
    NOT_WORKING,        // Defective, bench diagnosis needed (e.g. battery failure)
    DECOMMISSIONED      // Scrap or retired asset
}
