package com.dazmy.pinjam.buku.config;

import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] WHITELIST_URL = {
      "/api/v1/auth/register",
      "/api/v1/auth/login"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(WHITELIST_URL).permitAll();
                    auth.requestMatchers("/test-api").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERUSER");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/books").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERUSER");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/books/**").authenticated();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/books/**").hasAnyAuthority("ROLE_USER", "ROLE_SUPERUSER");
                    auth.requestMatchers("/api/v1/users/**").authenticated();
                    auth.requestMatchers(HttpMethod.PATCH, "/api/v1/auth/password/reset").authenticated();
                    auth.requestMatchers(HttpMethod.DELETE, "api/v1/auth/logout").authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByCredential_Username(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

            return new org.springframework.security.core.userdetails.User(
                            user.getCredential().getUsername(),
                            user.getCredential().getPassword(),
                            true,
                            true,
                            true,
                            true,
                            List.of(new SimpleGrantedAuthority(user.getRole().name()))
                    );
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
