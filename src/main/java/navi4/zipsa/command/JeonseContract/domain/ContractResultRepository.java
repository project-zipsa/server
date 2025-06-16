package navi4.zipsa.command.JeonseContract.domain;

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
        SET cr.jeonseContractJson = :text
        WHERE cr.user.id = :userId
    """)
    void updateJeonseContractJson(@Param("userId") Long userId, @Param("text") String text);

    @Modifying
    @Transactional
    @Query("""
        UPDATE ContractResult cr
        SET cr.propertyTitleJson = :text
        WHERE cr.user.id = :userId
    """
    )
    void updatePropertyTitleJson(@Param("userId") Long userId, @Param("text") String text);


    @Modifying
    @Transactional
    @Query("""
        UPDATE ContractResult cr
        SET cr.buildingRegisterJson = :text
        WHERE cr.user.id = :userId
    """)
    void updateBuildingRegisterJson(@Param("userId") Long userId, @Param("text") String text);

    @Modifying
    @Transactional
    @Query("""
        UPDATE ContractResult cr
        SET cr.rawLeaseContractText = :text
        WHERE cr.user.id = :userId
    """)
    void updateRawLeaseContractText(@Param("userId") Long userId, @Param("text") String text);


}
