package com.seahere.backend.auth.oauth;

import com.seahere.backend.common.dto.UserLogin;
import com.seahere.backend.user.domain.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private UserLogin user;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            UserLogin user) {
        super(authorities, attributes, nameAttributeKey);
        this.user = user;
    }
}
