package com.seahere.backend.auth.oauth.service;

import com.seahere.backend.auth.oauth.CustomOAuth2User;
import com.seahere.backend.auth.oauth.OAuthAttributes;
import com.seahere.backend.common.dto.UserLogin;
import com.seahere.backend.common.entity.Role;
import com.seahere.backend.common.entity.SocialType;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.domain.UserStatus;
import com.seahere.backend.user.exception.BrokerPermissionException;
import com.seahere.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        UserEntity createdUser = getUser(extractAttributes, socialType);
        CompanyEntity company = createdUser.getCompany();
        UserLogin userLogin = UserLogin.from(createdUser,company);
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                userLogin
        );
    }

    private SocialType getSocialType(String registrationId) {
        if(NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        }
        if(KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }

    private UserEntity getUser(OAuthAttributes attributes, SocialType socialType) {
        UserEntity findUser = userRepository.findWithCompanyBySocialTypeAndSocialId(socialType,
                attributes.getOauth2UserInfo().getId())
                .orElse(null);

        if(findUser == null) {
            return saveUser(attributes, socialType);
        }

        if(findUser.getRole() == Role.EMPLOYEE &&
                (findUser.getStatus() == UserStatus.PENDING) || (findUser.getStatus() == UserStatus.REJECTED)){
            OAuth2Error error = new OAuth2Error("허가된 사용자가 아닙니다.", "User account is not active.", null);
            throw new OAuth2AuthenticationException(error,new BrokerPermissionException());
        }
        return findUser;
    }

    private UserEntity saveUser(OAuthAttributes attributes, SocialType socialType) {
        UserEntity createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        return userRepository.save(createdUser);
    }
}
