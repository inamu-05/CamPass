package com.example.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .anyRequest().authenticated() // その他のリクエストは認証が必要
            )

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
