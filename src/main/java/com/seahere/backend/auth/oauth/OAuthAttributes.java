package com.seahere.backend.auth.oauth;

import com.seahere.backend.auth.oauth.userinfo.GoogleOAuth2UserInfo;
import com.seahere.backend.auth.oauth.userinfo.NaverOAuth2UserInfo;
import com.seahere.backend.auth.oauth.userinfo.OAuth2UserInfo;
import com.seahere.backend.common.entity.Role;
import com.seahere.backend.common.entity.SocialType;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.domain.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private String nameAttributeKey;
    private OAuth2UserInfo oauth2UserInfo;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType,
                                     String userNameAttributeName, Map<String, Object> attributes) {

        if (socialType == SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    public UserEntity toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        return UserEntity.builder()
                .socialType(socialType)
                .email(oauth2UserInfo.getEmail())
                .socialId(oauth2UserInfo.getId())
                .leaves(false)
                .role(Role.GUEST)
                .status(UserStatus.PENDING)
                .build();
    }
}
