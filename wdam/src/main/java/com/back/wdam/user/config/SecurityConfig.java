package com.back.wdam.user.config;


import com.back.wdam.user.jwt.JwtAccessDeniedHandler;
import com.back.wdam.user.jwt.JwtAuthenticationEntryPoint;
import com.back.wdam.user.jwt.JwtSecurityConfig;
import com.back.wdam.user.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource configurationSource () {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setMaxAge(3600 * 6L);
        config.addExposedHeader("Authorization");
        config.addExposedHeader("refreshToken");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 토큰을 사용하므로 CSRF 설정 Disable
                .csrf((csrfConfig)->
                        csrfConfig.disable()
                )
                // 커스텀 exception handler 사용
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .cors(cors -> cors.configurationSource(configurationSource()))
                .headers((headers) -> headers
                                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 사용 X
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        //예외처리된 요청 결과에 대해 401 대신 제대로 된 결과를 보내도록
                        .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                        //프론트엔드 빌드 파일들 인증 불필요
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/static/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/index.html")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/favicon.ico")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/manifest.json")).permitAll()
                        //회원가입과 로그인은 인증 불필요
                        .requestMatchers(new AntPathRequestMatcher("/users/signup")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users/login")).permitAll()
                        //인증 필요
                        .requestMatchers(new AntPathRequestMatcher("/users/update")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/users/logout")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/users")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/files")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/log/**")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/analyze")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/analyze/**")).authenticated()
                        .anyRequest().authenticated()

                )
                .with(new JwtSecurityConfig(tokenProvider), customizer -> {})
        ;

        return http.build();
    }

    // 시큐리티 로직 수행 제외
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)
                -> web
                .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // 정적 리소스들
    }
}