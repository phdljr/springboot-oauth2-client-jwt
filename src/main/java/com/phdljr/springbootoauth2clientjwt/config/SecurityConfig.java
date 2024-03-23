package com.phdljr.springbootoauth2clientjwt.config;

import com.phdljr.springbootoauth2clientjwt.jwt.JwtFilter;
import com.phdljr.springbootoauth2clientjwt.jwt.JwtUtil;
import com.phdljr.springbootoauth2clientjwt.oauth2.CustomSuccessHandler;
import com.phdljr.springbootoauth2clientjwt.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(
                config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuth2UserService))
                .successHandler(customSuccessHandler));

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll()
                .anyRequest().authenticated());

        http
            .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
