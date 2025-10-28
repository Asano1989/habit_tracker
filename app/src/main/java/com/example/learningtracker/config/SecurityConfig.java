package com.example.learningtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login //  フォーム認証を使う
        .permitAll()) //  フォーム認証画面は認証不要
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/css/**").permitAll() // CSSファイルは認証不要
            .requestMatchers("/todo").permitAll()
            .requestMatchers("/").permitAll() //  トップページは認証不要
            .anyRequest().authenticated() //  他のURLはログイン後アクセス可能
        )
        .formLogin(form -> form
            .usernameParameter("userId")
            .passwordParameter("password")
            .loginProcessingUrl("/login")
            .permitAll()
        )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .permitAll()
        );

        return http.build();
    }

    // LoginUserDetailServiceとBCryptPasswordEncoderを使うよう設定
    @Bean
    public DaoAuthenticationProvider authenticationProvider(LoginUserDetailService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // 💡 これが必要
        authProvider.setPasswordEncoder(passwordEncoder);       // 💡 これが必要
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}