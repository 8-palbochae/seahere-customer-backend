package com.seahere.backend.auth.login.service;

import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.common.dto.UserLogin;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.domain.UserStatus;
import com.seahere.backend.user.exception.BrokerPermissionException;
import com.seahere.backend.user.exception.UserNotFound;
import com.seahere.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findWithCompanyByEmail(email)
                .orElseThrow(UserNotFound::new);
        CompanyEntity company = user.getCompany();
        if (user.getStatus() == UserStatus.PENDING || user.getStatus() == UserStatus.REJECTED) {
            throw new BrokerPermissionException();
        }

        return new CustomUserDetails(UserLogin.from(user,company));
    }
}
