package com.example.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // セキュリティ設定の詳細
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll() // ログインページは全員アクセス可能
                .requestMatchers("/api/validate-otp").permitAll()
                .requestMatchers("/save-onetime-pass", "/api/**").authenticated()
                .anyRequest().authenticated() // その他のリクエストは認証が必要
            )
            // Postman (テスト用、ここから)
            // 2. Enable HTTP Basic Authentication (REQUIRED for Postman Basic Auth)
            .httpBasic(Customizer.withDefaults()) // <--- ADD THIS LINE

            // 3. Disable CSRF for API Endpoints (REQUIRED for smooth Postman POST testing)
            // This tells Spring to ignore CSRF checks for any path starting with /api/
            // (like your /api/validate-otp endpoint)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**") // <--- ADD THIS LINE
                // NOTE: You must also ignore /save-onetime-pass if you want to test it
                // without providing the CSRF token in Postman.
                .ignoringRequestMatchers("/save-onetime-pass") // <--- ADD THIS LINE
            )

            // ここまで

            // フォームログインの設定
            .formLogin(form -> form
                .loginPage("/login") // ログインページの指定
                .defaultSuccessUrl("/main", true) // ログイン成功後のリダイレクト先
                .failureUrl("/login?error")
                .usernameParameter("userId") // ユーザーIDのパラメータ名
                .passwordParameter("userPass") // パスワードのパラメータ名
                .permitAll()
            )

            // ログアウトの設定
            .logout(logout -> logout
                .logoutUrl("/logout") // ログアウトURLの指定
                .logoutSuccessUrl("/login?logout") // ログアウト成功後のリダイレクト先
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        return http.build();   
    }
}
