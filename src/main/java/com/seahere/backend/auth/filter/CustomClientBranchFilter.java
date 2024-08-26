package com.seahere.backend.auth.filter;

import com.seahere.backend.auth.oauth.handler.OAuth2LoginFailureHandler;
import com.seahere.backend.auth.oauth.handler.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomClientBranchFilter extends OncePerRequestFilter {
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String refererHeader = request.getHeader("Referer");

        if (refererHeader != null) {
            oAuth2LoginSuccessHandler.editResponseServer(refererHeader);
            oAuth2LoginFailureHandler.editResponseServer(refererHeader);
        }

        filterChain.doFilter(request, response);
    }
}