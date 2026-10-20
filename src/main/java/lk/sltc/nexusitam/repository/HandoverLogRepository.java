package lk.sltc.nexusitam.repository;

import lk.sltc.nexusitam.model.HandoverLog;
import lk.sltc.nexusitam.model.enums.HandoverStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HandoverLogRepository extends JpaRepository<HandoverLog, Long> {

    List<HandoverLog> findByAssetIdOrderByCreatedAtDesc(Long assetId);

    List<HandoverLog> findByStatus(HandoverStatus status);

    List<HandoverLog> findByCustodianEmail(String custodianEmail);

    Optional<HandoverLog> findByDigitalSignToken(String digitalSignToken);

    Page<HandoverLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<HandoverLog> findTop10ByOrderByCreatedAtDesc();
}
