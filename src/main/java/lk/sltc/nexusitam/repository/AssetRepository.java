package lk.sltc.nexusitam.repository;

import lk.sltc.nexusitam.model.Asset;
import lk.sltc.nexusitam.model.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findByAssetTag(String assetTag);

    Optional<Asset> findBySerialNumber(String serialNumber);

    List<Asset> findByDeviceType(DeviceType deviceType);

    List<Asset> findByStatus(AssetStatus status);

    List<Asset> findByCampusLocation(CampusLocation campusLocation);

    List<Asset> findByDepartment(String department);

    List<Asset> findByIsExamQuarantineTrue();

    // Laptop fleet filtering (e.g. Acer Aspire Go 14 BAIT units)
    List<Asset> findByDeviceTypeAndModelContainingIgnoreCase(DeviceType deviceType, String modelKeyword);

    // Multi-attribute search and filter query
    @Query("SELECT a FROM Asset a WHERE " +
           "(:query IS NULL OR :query = '' OR " +
           " LOWER(a.assetTag) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(a.serialNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(a.model) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(a.currentCustodian) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(a.department) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(a.specificLocation) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:campus IS NULL OR a.campusLocation = :campus) AND " +
           "(:deviceType IS NULL OR a.deviceType = :deviceType) AND " +
           "(:department IS NULL OR a.department = :department) AND " +
           "(:hasBag IS NULL OR a.hasBag = :hasBag)")
    Page<Asset> searchAssets(
            @Param("query") String query,
            @Param("status") AssetStatus status,
            @Param("campus") CampusLocation campus,
            @Param("deviceType") DeviceType deviceType,
            @Param("department") String department,
            @Param("hasBag") Boolean hasBag,
            Pageable pageable
    );

    // KPI Counters
    long countByStatus(AssetStatus status);

    long countByDeviceType(DeviceType deviceType);

    long countByIsExamQuarantineTrue();

    long countByAntivirusStatus(AntivirusStatus antivirusStatus);

    @Query("SELECT COUNT(a) FROM Asset a WHERE a.deviceType IN (lk.sltc.nexusitam.model.enums.DeviceType.LAPTOP, lk.sltc.nexusitam.model.enums.DeviceType.DESKTOP)")
    long countComputeUnits();

    @Query("SELECT a.department, COUNT(a) FROM Asset a WHERE a.department IS NOT NULL GROUP BY a.department")
    List<Object[]> countByDepartmentBreakdown();

    @Query("SELECT a.specificLocation, COUNT(a) FROM Asset a WHERE a.specificLocation IS NOT NULL GROUP BY a.specificLocation ORDER BY COUNT(a) DESC")
    List<Object[]> countByLocationBreakdown();
}
