package navi4.zipsa.command.contract.domain;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContractResultRepository extends JpaRepository<ContractResult, Long> {
    Optional<ContractResult> findContractResultById(Long contractId);
    Optional<ContractResult> findContractResultsByUserId(Long userId);
    boolean existsContractResultByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("""
        UPDATE ContractResult cr
        SET cr.jeonseContractText = :text
        WHERE cr.user.id = :userId
    """)
    void updateJeonseContractText(@Param("userId") Long userId, @Param("text") String text);

    @Modifying
    @Transactional
    @Query("""
        UPDATE ContractResult cr
        SET cr.jeonseContractRiskScore = :riskScore
        WHERE cr.user.id = :userId
    """)
    void updateJeonseContractRiskScore(@Param("userId") Long userId, @Param("riskScore") double riskScore);

}
