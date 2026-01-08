package com.example.app.config;
 
import java.util.Arrays;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.app.security.JwtFilter;

import jakarta.servlet.http.HttpServletResponse;
 
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
            .securityMatcher("/api/**")  // /api/** に適用
            .csrf(csrf -> csrf.disable()) // APIはstatelessなのでCSRF無効
            .sessionManagement(sm -> sm
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            .exceptionHandling(eh -> eh
                .authenticationEntryPoint((request, response, authException) ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                )
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/student/login").permitAll()
                .requestMatchers("/api/validate-otp").permitAll()
                .requestMatchers("/api/student/image").authenticated() 
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

            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/login?error") // Redirect users who fail authorization (like students)
            )

            .authorizeHttpRequests(auth -> auth
                // 静的リソースとログインページは全員アクセス可能
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/save-onetime-pass").authenticated()
                .anyRequest().hasRole("STAFF")
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // 全オリジン許可（開発用）
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    } 
}

 