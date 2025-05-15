package navi4.zipsa.command.contract.domain;

import jakarta.persistence.*;
import lombok.Getter;
import navi4.zipsa.command.user.domain.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Table(name = "contract_results")
public class ContractResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO: 업데이트
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // TODO: 테이블 필드타입 변경
    //ALTER TABLE your_table_name
    //MODIFY COLUMN jeonse_contract_text MEDIUMTEXT;
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
    private int totalRiskScore = 0;

    // 계약 시작일
    @Column(name = "contract_start_date")
    private LocalDateTime contractStartDate;

    // 계약 종료일
    @Column(name = "contract_end_date")
    private LocalDateTime contractEndDate;

    // 거래가
    @Column(name = "contract_price")
    private BigInteger contractPrice;

    // 보증금
    @Column(name = "deposit")
    private BigInteger deposit;

    // 주소 전체
    @Column(name = "total_address")
    private String totalAddress;

    // 시
    @Column(name = "si")
    private String si;

    // 구
    @Column(name = "gu")
    private String gu;

    // 동
    @Column(name = "dong")
    private String dong;

    // 상세
    @Column(name = "detail_address")
    private String detailAddress;

    // 시군구 코드
    @Column(name = "sigungu_code")
    private String sigunguCode;

    // 번지동 코드
    @Column(name = "bunjidong_code")
    private String bunjidongCode;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
