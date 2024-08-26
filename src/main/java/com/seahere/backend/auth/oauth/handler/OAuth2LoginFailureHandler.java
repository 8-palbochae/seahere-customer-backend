package com.seahere.backend.auth.oauth.handler;

import com.seahere.backend.user.exception.BrokerPermissionException;
import com.seahere.backend.user.exception.UserNotFound;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@Component
@Getter
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    private String responseServer;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        int statusCode;

        if (exception.getCause() instanceof BrokerPermissionException) {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            errorMessage = "사용자 계정이 활성화되지 않았습니다.";
        } else if (exception.getCause() instanceof UserNotFound) {
            statusCode = HttpServletResponse.SC_NOT_FOUND;
            errorMessage = "사용자를 찾을 수 없습니다.";
        } else {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            errorMessage = "로그인 실패! 이메일이나 비밀번호를 확인해주세요.";
        }

        response.sendRedirect( "https://broker.seahere.org/login?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
    }

    public void editResponseServer(String responseServer){
        this.responseServer = responseServer;
    }
}
