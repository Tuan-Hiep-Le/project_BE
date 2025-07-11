package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/images/**","/css/**", "/js/**","/favicon.ico").permitAll()
                .requestMatchers(HttpMethod.GET, "/login","/register","/homepage","/homepage/search","/homepage/category","/homepage/author","/homepage/topic","/admin/sync-shipcost").permitAll()
                .requestMatchers(HttpMethod.POST,"/login","/register").permitAll()
                .requestMatchers(HttpMethod.GET,"/home_user_after_login","/homepage/information_book","/home_after_user_login/switch_buy_now").hasRole("USER")
                .anyRequest().authenticated()
        );
        httpSecurity.securityContext(securityContext -> securityContext.securityContextRepository(securityContextRepository())).exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/login") ));
        httpSecurity
                .securityContext(securityContext -> securityContext
                        .securityContextRepository(new HttpSessionSecurityContextRepository())
                );

        httpSecurity.formLogin(form -> form.disable()).csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
}
