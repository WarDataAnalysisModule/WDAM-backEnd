package com.back.wdam.user.config;


import com.back.wdam.user.jwt.JwtAuthenticationEntryPoint;
import com.back.wdam.user.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    //private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http
                .csrf((csrfConfig)->
                        csrfConfig.disable()
                )
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .headers((headers) -> headers
                                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 사용 X


                                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        //.anyRequest().permitAll()
                        //.requestMatchers("/files","files/**", "analyze/**", "users/update").authenticated() //인증 필요
                        .anyRequest().permitAll()   // 인증 불필요
                )
                ;

        return http.build();
    }
}