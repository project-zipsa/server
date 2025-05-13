package navi4.zipsa.command.contract.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContractResultRepository extends JpaRepository<ContractResult, Long> {
    Optional<ContractResult> findContractResultById(Long contractId);
    Optional<List<ContractResult>> findContractResultsByUsersId(Long userId);
}
