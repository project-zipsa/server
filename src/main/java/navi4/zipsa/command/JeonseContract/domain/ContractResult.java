package navi4.zipsa.command.JeonseContract.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import navi4.zipsa.command.user.domain.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Table(name = "contract_results")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContractResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "jeonse_contract_risk_score")
    private Integer jeonseContractRiskScore = 0;

    @Column(name = "jeonse_contract_json", columnDefinition = "TEXT")
    private String jeonseContractJson;

    @Column(name = "property_title_json", columnDefinition = "TEXT")
    private String propertyTitleJson;

    @Column(name = "building_register_json", columnDefinition = "TEXT")
    private String buildingRegisterJson;

    @Column(name = "raw_lease_contract_text", columnDefinition = "TEXT")
    private String rawLeaseContractText;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public static ContractResult create(User user) {
        return new ContractResult(user);
    }

    private ContractResult(User user) {
        this.user = user;
    }
}
