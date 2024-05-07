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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .headers((headers) -> headers
                                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 사용 X
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        // 인증(토큰) 없이 접속 가능한 api
                        .requestMatchers(new AntPathRequestMatcher("/users/signup")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users/login")).permitAll()

                        .requestMatchers(new AntPathRequestMatcher("/users/**")).authenticated()
                        //.anyRequest().authenticated()  // 나머지는 인증 필요
                        .anyRequest().permitAll()
                )

                .with(new JwtSecurityConfig(tokenProvider), customizer -> {})
                // 프론트 연결 시 로그인, 로그아웃 페이지 연결
                 //.formLogin((formLogin) -> formLogin
                        //.loginProcessingUrl("users/login"));
//                        .loginPage("/users/login")  //로그인 페이지
//                        .defaultSuccessUrl("/")    //로그인 성공 시 이동하는 페이지
//                        .failureUrl("/loginfail")    // 로그인 실패 시
//                )
//                .logout((logout) -> logout
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
//                        .logoutSuccessUrl("/")
//                        .invalidateHttpSession(true))

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