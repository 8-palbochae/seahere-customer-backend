package com.seahere.backend.company.entity;

import com.seahere.backend.common.entity.Address;
import com.seahere.backend.company.controller.request.CompanyCreateReq;
import com.seahere.backend.user.domain.UserEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "company")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    private String registrationNumber;

    private String companyName;

    @Embedded
    private Address address;

    private String profileImage;


    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<UserEntity> userEntityList =new ArrayList<>();

    @Builder
    public CompanyEntity(Long id, String registrationNumber, String companyName, Address address, String profileImage) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.companyName = companyName;
        this.address = address;
        this.profileImage = profileImage;
    }

    public static CompanyEntity from(CompanyCreateReq companyCreateReq){
        return CompanyEntity.builder()
                .registrationNumber(companyCreateReq.getRegistrationNumber())
                .companyName(companyCreateReq.getCompanyName())
                .address(companyCreateReq.getAddress())
                .build();
    }

    public void editProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
}
