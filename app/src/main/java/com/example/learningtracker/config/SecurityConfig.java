package com.example.learningtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login //  フォーム認証を使う
        .permitAll()) //  フォーム認証画面は認証不要
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/css/**").permitAll()
            .requestMatchers("/todo", "/todo/query").permitAll()
            .requestMatchers("/signup", "/login", "/logout", "/error").permitAll()
            .requestMatchers("/").permitAll()
            .anyRequest().authenticated() //  他のURLはログイン後アクセス可能
        )
        .formLogin(form -> form
            .usernameParameter("accountId")
            .passwordParameter("password")
            .loginProcessingUrl("/login")
            .loginPage("/login")
            .permitAll()
        )
        .logout(logout -> logout
            .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}