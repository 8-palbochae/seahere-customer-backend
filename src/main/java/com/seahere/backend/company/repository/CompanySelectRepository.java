package com.seahere.backend.company.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seahere.backend.company.controller.request.CompanySearch;
import com.seahere.backend.company.controller.response.CompanyFollowResponse;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.company.entity.QCompanyEntity;
import com.seahere.backend.follow.entity.QFollowEntity;
import com.seahere.backend.company.controller.request.CompanySearch;
import com.seahere.backend.inventory.entity.QInventoryEntity;
import com.seahere.backend.outgoing.entity.QOutgoingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CompanySelectRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<CompanyEntity> getList(CompanySearch companySearch) {
        return jpaQueryFactory.selectFrom(QCompanyEntity.companyEntity)
                .where(QCompanyEntity.companyEntity.companyName.containsIgnoreCase(companySearch.getSearchWord()))
                .limit(companySearch.getSize())
                .offset(companySearch.getOffset())
                .orderBy(QCompanyEntity.companyEntity.id.desc())
                .fetch();
    }

    public List<CompanyEntity> findTradeCompanyList(CompanySearch companySearch, Long companyId) {
        QCompanyEntity company = QCompanyEntity.companyEntity;
        QInventoryEntity inventory = QInventoryEntity.inventoryEntity;

        return jpaQueryFactory.selectDistinct(company)
                .from(company)
                .join(inventory).on(company.id.eq(inventory.company.id))
                .where(
                        company.id.ne(companyId),
                        inventory.quantity.gt(0),
                        company.companyName.containsIgnoreCase(companySearch.getSearchWord())
                )
                .limit(companySearch.getSize())
                .offset(companySearch.getOffset())
                .orderBy(company.id.desc())
                .fetch();
    }



    public CompanyEntity  findCompanyWithBestOutgoing() {
        QOutgoingEntity outgoing = QOutgoingEntity.outgoingEntity;
        return jpaQueryFactory
                .select(outgoing.company)
                .from(outgoing)
                .groupBy(outgoing.company)
                .orderBy(outgoing.count().desc())
                .limit(1)
                .fetchOne();
    }

    public CompanyFollowResponse findCompanyWithBestOutgoingForCustomer(Long customerId) {
        CompanyEntity bestCompany = findCompanyWithBestOutgoing();

        if (bestCompany == null) {
            return null;
        }

        QFollowEntity follow = QFollowEntity.followEntity;

        Boolean isFollowed = jpaQueryFactory
                .select(follow.followId.isNotNull())
                .from(follow)
                .where(follow.company.id.eq(bestCompany.getId())
                        .and(follow.user.id.eq(customerId)))
                .fetchOne();

        return CompanyFollowResponse.builder()
                .id(bestCompany.getId())
                .registrationNumber(bestCompany.getRegistrationNumber())
                .companyName(bestCompany.getCompanyName())
                .address(bestCompany.getAddress())
                .profileImage(bestCompany.getProfileImage())
                .isFollowed(isFollowed != null && isFollowed)
                .build();
    }

    public List<CompanyFollowResponse> getListForCustomer(Long customerId, CompanySearch companySearch) {
        QFollowEntity follow = QFollowEntity.followEntity;

        return jpaQueryFactory
                .select(Projections.constructor(CompanyFollowResponse.class,
                        QCompanyEntity.companyEntity.id,
                        QCompanyEntity.companyEntity.registrationNumber,
                        QCompanyEntity.companyEntity.companyName,
                        QCompanyEntity.companyEntity.address,
                        QCompanyEntity.companyEntity.profileImage,
                        follow.followId.isNotNull()
                ))
                .from(QCompanyEntity.companyEntity)
                .leftJoin(follow).on(follow.company.id.eq(QCompanyEntity.companyEntity.id)
                        .and(follow.user.id.eq(customerId)))
                .where(QCompanyEntity.companyEntity.companyName.containsIgnoreCase(companySearch.getSearchWord()))
                .limit(companySearch.getSize())
                .offset(companySearch.getOffset())
                .orderBy(QCompanyEntity.companyEntity.id.desc())
                .fetch();
    }

    public List<CompanyFollowResponse> getFollowListForCustomer(Long customerId, CompanySearch companySearch) {
        QFollowEntity follow = QFollowEntity.followEntity;
        QCompanyEntity company = QCompanyEntity.companyEntity;

        try {
            return jpaQueryFactory
                    .select(Projections.constructor(CompanyFollowResponse.class,
                            company.id,
                            company.registrationNumber,
                            company.companyName,
                            company.address,
                            company.profileImage,
                            follow.followId.isNotNull()
                    ))
                    .from(company)
                    .innerJoin(follow).on(follow.company.id.eq(company.id)
                            .and(follow.user.id.eq(customerId)))
                    .where(company.companyName.containsIgnoreCase(companySearch.getSearchWord()))
                    .limit(companySearch.getSize())
                    .offset(companySearch.getOffset())
                    .orderBy(company.id.desc())
                    .fetch();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching company follow list");
        }
    }

    public CompanyFollowResponse findCompanyByIdForCustomer(Long customerId, Long companyId) {
        QFollowEntity follow = QFollowEntity.followEntity;

        return jpaQueryFactory
                .select(Projections.constructor(CompanyFollowResponse.class,
                        QCompanyEntity.companyEntity.id,
                        QCompanyEntity.companyEntity.registrationNumber,
                        QCompanyEntity.companyEntity.companyName,
                        QCompanyEntity.companyEntity.address,
                        QCompanyEntity.companyEntity.profileImage,
                        follow.followId.isNotNull()
                ))
                .from(QCompanyEntity.companyEntity)
                .leftJoin(follow).on(follow.company.id.eq(QCompanyEntity.companyEntity.id)
                        .and(follow.user.id.eq(customerId)))
                .where(QCompanyEntity.companyEntity.id.eq(companyId))
                .fetchOne();
    }

}
