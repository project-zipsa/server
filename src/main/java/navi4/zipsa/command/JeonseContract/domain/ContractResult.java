package navi4.zipsa.command.JeonseContract.domain;

import jakarta.persistence.*;
import lombok.Getter;
import navi4.zipsa.command.user.domain.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Table(name = "contract_results")
public class ContractResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Lob
    @Column(name = "jeonse_contract_text", columnDefinition = "MEDIUMTEXT")
    private String jeonseContractText;

    @Lob
    @Column(name = "property_title_text", columnDefinition = "MEDIUMTEXT")
    private String propertyTitleText;

    // 전세계약서 위험 분석 총점
    @Column(name = "jeonse_contract_risk_score")
    private Integer jeonseContractRiskScore = 0;

    // 건축물대장 + 등기부등본 위험 요소 점수
    @Column(name = "title_register_risk_score")
    private Integer titleRegisterRiskScore = 0;

    // 위험도 총점
    @Column(name = "total_risk_score")
    private Integer totalRiskScore = 0;

    // 전세계약서 JSON
    @Column(name = "jeonse_contract_data", columnDefinition = "TEXT")
    private String jeonseContractData;

    // 등기부등본 JSON
    @Column(name = "property_title_data", columnDefinition = "TEXT")
    private String propertyTitleData;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
