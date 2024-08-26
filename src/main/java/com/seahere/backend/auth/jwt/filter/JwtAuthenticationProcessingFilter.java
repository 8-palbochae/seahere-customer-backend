package com.seahere.backend.auth.jwt.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.seahere.backend.auth.jwt.service.JwtService;
import com.seahere.backend.auth.jwt.util.PasswordUtil;
import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.common.dto.UserLogin;
import com.seahere.backend.common.exception.SeaHereException;
import com.seahere.backend.redis.entity.Token;
import com.seahere.backend.redis.respository.TokenRepository;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/api/login";
    private static final String LOGOUT_URL = "/api/logout";
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException , SeaHereException {
        try {
            if (request.getRequestURI().equals(NO_CHECK_URL)) {
                filterChain.doFilter(request, response);
                return;
            }

            Optional<String> extractRefreshToken = jwtService.extractRefreshToken(request);

            if(request.getRequestURI().equals(LOGOUT_URL)){
                Optional<String> optionalAccessToken = jwtService.extractAccessToken(request);
                if (optionalAccessToken.filter(jwtService::isAccessTokenValid).isPresent()) {
                    String accessToken = optionalAccessToken.get();
                    String email = jwtService.extractEmail(accessToken).orElse(null);
                    jwtService.deleteRedisRefreshToken(email);
                }
            }

            if(extractRefreshToken.isPresent()) {
                boolean valid = jwtService.isTokenValid(extractRefreshToken.get());
                if(!valid) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"errorCode\": \"REFRESH_TOKEN_EXPIRED\", \"message\": \"Refresh 토큰 만기 로그인 필요\"}");
                    return;
                }
            }

            String refreshToken = jwtService.extractRefreshToken(request)
                    .filter(jwtService::isTokenValid)
                    .orElse(null);

            if (refreshToken != null) {
                checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
                return;
            }

            if (refreshToken == null) {
                checkAccessTokenAndAuthentication(request, response, filterChain);
            }
        } catch (SeaHereException | TokenExpiredException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"errorCode\": \"ACCESS_TOKEN_EXPIRED\", \"message\": \"Access 토큰 만료 리프레시 토큰 전달 필요\"}");

            return;
        }
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        if(token.isPresent()){
            Token redisToken = token.get();
            jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(redisToken.getEmail()),
                    refreshToken);
        }
        else{
            userRepository.findByRefreshToken(refreshToken)
                    .ifPresent(user -> {
                        jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(user.getEmail()),
                                refreshToken);
                    });
        }

        log.info("ACCESS 토큰 재발급 성공");
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException, TokenExpiredException {
        Optional<String> optionalAccessToken = jwtService.extractAccessToken(request);
        if (optionalAccessToken.filter(jwtService::isAccessTokenValid).isPresent()) {
            String accessToken = optionalAccessToken.get();
            String email = jwtService.extractEmail(accessToken).orElse(null);

            if (email != null) {
                userRepository.findWithCompanyByEmail(email).ifPresent(user -> {
                    saveAuthentication(user);
                });
            }
        }
        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(UserEntity myUser) {
        String password = myUser.getPassword();
        if (password == null) {
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser = new CustomUserDetails(UserLogin.from(myUser,myUser.getCompany()));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
