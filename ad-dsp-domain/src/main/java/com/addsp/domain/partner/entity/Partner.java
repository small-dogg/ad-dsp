package com.addsp.domain.partner.entity;

import com.addsp.common.constant.PartnerStatus;
import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "partners")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Partner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String businessNumber;

    @Column(nullable = false)
    private String contactName;

    @Column(nullable = false)
    private String contactPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartnerStatus status;

    @Builder
    public Partner(String email, String password, String businessName,
                   String businessNumber, String contactName, String contactPhone) {
        this.email = email;
        this.password = password;
        this.businessName = businessName;
        this.businessNumber = businessNumber;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.status = PartnerStatus.PENDING;
    }

    public void activate() {
        this.status = PartnerStatus.ACTIVE;
    }

    public void suspend() {
        this.status = PartnerStatus.SUSPENDED;
    }

    public void withdraw() {
        this.status = PartnerStatus.WITHDRAWN;
    }

    public boolean isActive() {
        return this.status == PartnerStatus.ACTIVE;
    }
}
