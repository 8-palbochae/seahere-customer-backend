package com.seahere.backend.alarm.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seahere.backend.alarm.entity.AlarmTokenEntity;
import com.seahere.backend.alarm.entity.QAlarmTokenEntity;
import com.seahere.backend.company.entity.QCompanyEntity;
import com.seahere.backend.follow.entity.QFollowEntity;
import com.seahere.backend.user.domain.QUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.seahere.backend.alarm.entity.QAlarmTokenEntity.alarmTokenEntity;
import static com.seahere.backend.company.entity.QCompanyEntity.companyEntity;
import static com.seahere.backend.follow.entity.QFollowEntity.followEntity;
import static com.seahere.backend.user.domain.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class AlarmRepository {

    private final JPAQueryFactory queryFactory;

    public List<AlarmTokenEntity> findByCompanyUser(Long companyId){
        return queryFactory.selectFrom(alarmTokenEntity)
                .leftJoin(alarmTokenEntity.user, userEntity).fetchJoin()
                .leftJoin(userEntity.company, companyEntity).fetchJoin()
                .where(companyEntity.id.eq(companyId))
                .fetch();
    }

    public List<AlarmTokenEntity> findByCompanyFlowerUser(Long companyId){
        return queryFactory.selectFrom(alarmTokenEntity).distinct()
                .leftJoin(alarmTokenEntity.user, userEntity)
                .leftJoin(userEntity.followList, followEntity)
                .leftJoin(followEntity.company, companyEntity)
                .where(companyEntity.id.eq(companyId))
                .fetch();
    }
}
