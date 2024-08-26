package com.seahere.backend.auth.login.handler;

import com.seahere.backend.user.exception.BrokerPermissionException;
import com.seahere.backend.user.exception.UserNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if (exception.getCause() instanceof BrokerPermissionException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"사용자 계정이 활성화되지 않았습니다.\"}");
        } else if (exception.getCause() instanceof UserNotFound) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"message\": \"사용자를 찾을 수 없습니다.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"로그인 실패! 이메일이나 비밀번호를 확인해주세요.\"}");
        }

        log.info("로그인에 실패했습니다. 메시지 : {}", exception.getMessage());
    }
}
