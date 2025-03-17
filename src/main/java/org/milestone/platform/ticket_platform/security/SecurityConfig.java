package org.milestone.platform.ticket_platform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    @SuppressWarnings({ "removal", "deprecation" })
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .requestMatchers("/users").hasAnyAuthority("Admin")
            .requestMatchers("/ticket/create").hasAnyAuthority("Admin")
            .requestMatchers("/**").permitAll()
            .and()
            .formLogin()
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
            .and()
            .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID") 
            .and().exceptionHandling();
        return http.build();
    }

    @Bean
    DatabaseUserDetailsService userDetailsService(){
        return new DatabaseUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
