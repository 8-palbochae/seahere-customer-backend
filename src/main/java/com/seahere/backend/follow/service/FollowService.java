package com.seahere.backend.follow.service;

import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.company.exception.CompanyNotFound;
import com.seahere.backend.company.repository.CompanyRepository;
import com.seahere.backend.follow.entity.FollowEntity;
import com.seahere.backend.follow.repository.FollowJpaRepository;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.exception.UserNotFound;
import com.seahere.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowJpaRepository followJpaRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;


    public void save(Long userId, Long companyId) {
        CompanyEntity companyEntity = companyRepository.findById(companyId).orElseThrow(CompanyNotFound::new);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        FollowEntity followEntity = FollowEntity.builder()
                .user(userEntity)
                .company(companyEntity)
                .build();
        followJpaRepository.save(followEntity);
    }

    public void delete(Long userId, Long companyId) {
        FollowEntity followEntity = followJpaRepository.findByUserIdAndCompanyId(userId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 존재하지 않습니다."));
        followJpaRepository.delete(followEntity);
    }
}
