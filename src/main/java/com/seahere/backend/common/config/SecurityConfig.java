package com.seahere.backend.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seahere.backend.auth.filter.CustomClientBranchFilter;
import com.seahere.backend.auth.jwt.filter.JwtAuthenticationProcessingFilter;
import com.seahere.backend.auth.jwt.service.JwtService;
import com.seahere.backend.auth.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.seahere.backend.auth.login.handler.CustomLogoutSuccessHandler;
import com.seahere.backend.auth.login.handler.LoginFailureHandler;
import com.seahere.backend.auth.login.handler.LoginSuccessHandler;
import com.seahere.backend.auth.login.service.LoginService;
import com.seahere.backend.auth.oauth.handler.OAuth2LoginFailureHandler;
import com.seahere.backend.auth.oauth.handler.OAuth2LoginSuccessHandler;
import com.seahere.backend.auth.oauth.service.CustomOAuth2UserService;
import com.seahere.backend.redis.respository.TokenRepository;
import com.seahere.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
    private final LoginService loginService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final TokenRepository tokenRepository;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ServletRegistrationBean h2Console) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .cors().and()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/health").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/ocr").permitAll()
                .antMatchers(HttpMethod.POST,"/companies").permitAll()
                .antMatchers(HttpMethod.POST,"/companies/duplicate").permitAll()
                .antMatchers(HttpMethod.POST,"/users/**").permitAll()
                .antMatchers("/v3/api-docs").permitAll()
                .antMatchers("/swagger/**").permitAll()
                .antMatchers("/authentication/protected").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
                .userInfoEndpoint().userService(customOAuth2UserService);
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://localhost:3000",
                        "http://localhost:3000",
                        "http://localhost:5173",
                        "http://localhost:81",
                        "https://10.10.10.37:3000",
                        "http://10.10.10.170:5173",
                        "http://15.168.190.86:81",
                        "http://15.168.190.86:80",
                        "http://172.31.0.1:80",
                        "http://172.31.0.1:81",
                        "http://172.31.0.5:80",
                        "http://172.31.0.5:81",
                        "http://172.31.0.5:81",
                        "http://172.31.0.6",
                        "http://172.31.0.7",
                        "http://172.31.0.4:3000",
                        "http://172.31.0.6:5173",
                        "http://192.168.200.130:81",
                        "http://192.168.200.130:80",
                        "https://broker.seahere.org",
                        "https://customer.seahere.org"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Authorization", "Authorization-refresh");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository,tokenRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public CustomLogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler(tokenRepository);
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public CustomClientBranchFilter customClientFilter(){
        CustomClientBranchFilter customClientFilter = new CustomClientBranchFilter(oAuth2LoginSuccessHandler,oAuth2LoginFailureHandler);
        return customClientFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository,tokenRepository    );
        return jwtAuthenticationFilter;
    }
}
