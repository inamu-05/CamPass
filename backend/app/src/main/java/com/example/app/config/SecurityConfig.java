package com.example.app.config;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
 
import com.example.app.security.JwtFilter;
 
@Configuration

public class SecurityConfig {
 
    @Autowired

    private JwtFilter jwtFilter;
 
    @Bean

    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }
 
    // ① API用（Flutter）----------------------------------------

    @Bean

    @Order(1) // 優先度1でAPIチェーンを先に評価

    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
 
        http

            .securityMatcher("/api/**")  // /api/** にのみ適用

            .csrf(csrf -> csrf.disable()) // APIはstatelessなのでCSRF無効

            .sessionManagement(sm -> sm

                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/student/login").permitAll()

                .requestMatchers("/api/validate-otp").permitAll()

                .anyRequest().authenticated()

            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
 
        return http.build();

    }
 
    // ② PC（HTML画面）用--------------------------------------

    @Bean

    @Order(2) // 優先度2でAPIチェーンの後に評価

    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
 
        http

            // CSRF有効化、Cookieにトークンを保存しJSからも読めるように設定

            .csrf(csrf -> csrf

                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

            )

            // セッション管理はstateful（通常のフォームログイン）

            .sessionManagement(sm -> sm

                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)

            )

            .authorizeHttpRequests(auth -> auth

                // 静的リソースとログインページは全員アクセス可能

                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()

                .requestMatchers("/save-onetime-pass").authenticated()

                .anyRequest().authenticated()

            )

            .formLogin(form -> form

                .loginPage("/login") // カスタムログインページ

                .defaultSuccessUrl("/main", true)

                .failureUrl("/login?error")

                .usernameParameter("userId")

                .passwordParameter("userPass")

                .permitAll()

            )

            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl("/login?logout")

                .invalidateHttpSession(true)

                .deleteCookies("JSESSIONID")

                .permitAll()

            );
 
        return http.build();

    }

}

 