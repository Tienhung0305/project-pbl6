package com.example.sportstore06.config;

import com.example.sportstore06.security.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserSecurityService userService;
    private final LogoutHandler logoutHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] WHITE_LIST_URL = {
                "/api/v1/auth/**",
                "/api/v1/test",
                "/api/v1/momo_ipn",
                "/swagger-ui/**",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
        };
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(WHITE_LIST_URL)
                        .permitAll()
                        .requestMatchers(GET,
                                "/api/v1/bill/**",
                                "/api/v1/cart/**",
                                "/api/v1/business/**",
                                "/api/v1/category/**",
                                "/api/v1/comment/**",
                                "/api/v1/image/**",
                                "/api/v1/role/**",
                                "/api/v1/sale/**",
                                "/api/v1/user/**",
                                "/api/v1/product-information/**",
                                "/api/v1/product/**"
                        )
                        .permitAll()

                        .requestMatchers(GET, "/api/v1/test/admin/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(GET, "/api/v1/test/business/**").hasAnyAuthority("ROLE_BUSINESS")
                        .requestMatchers(GET, "/api/v1/test/customer/**").hasAnyAuthority("ROLE_CUSTOMER")

                        .requestMatchers(PUT, "/api/v1/bill/confirm-buy/**").hasAnyAuthority("ROLE_CUSTOMER")
                        .requestMatchers(GET, "/api/v1/bill/get-by-id-user/**").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_BUSINESS", "ROLE_ADMIN")
                        .requestMatchers(PUT, "/api/v1/bill/confirm-receive/**").hasAnyAuthority("ROLE_CUSTOMER")
                        .requestMatchers(PUT, "/api/v1/bill/confirm-sell/**").hasAnyAuthority("ROLE_BUSINESS")
                        .requestMatchers(PUT, "/api/v1/bill/confirm-cancel/**").hasAnyAuthority("ROLE_CUSTOMER")
                        .requestMatchers(GET, "/api/v1/bill/**").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")
                        .requestMatchers(POST, "/api/v1/bill/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(PUT, "/api/v1/bill/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(DELETE, "/api/v1/bill/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(PUT, "/api/v1/cart/change-state/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(GET, "/api/v1/cart/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")
                        .requestMatchers(POST, "/api/v1/cart/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")
                        .requestMatchers(PUT, "/api/v1/cart/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")
                        .requestMatchers(DELETE, "/api/v1/cart/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")

                        .requestMatchers(POST, "/api/v1/business/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")
                        .requestMatchers(PUT, "/api/v1/business/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")
                        .requestMatchers(DELETE, "/api/v1/business/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS")

                        .requestMatchers(POST, "/api/v1/category/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(PUT, "/api/v1/category/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(DELETE, "/api/v1/category/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(POST, "/api/v1/comment/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")
                        .requestMatchers(PUT, "/api/v1/comment/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")
                        .requestMatchers(DELETE, "/api/v1/comment/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")

                        .requestMatchers(POST, "/api/v1/image/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")
                        .requestMatchers(PUT, "/api/v1/image/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")
                        .requestMatchers(DELETE, "/api/v1/image/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")

                        .requestMatchers(POST, "/api/v1/role/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(PUT, "/api/v1/role/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(DELETE, "/api/v1/role/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(POST, "/api/v1/sale/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")
                        .requestMatchers(PUT, "/api/v1/sale/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")
                        .requestMatchers(DELETE, "/api/v1/sale/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")

                        .requestMatchers(PUT, "/api/v1/user/change-state/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(POST, "/api/v1/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")
                        .requestMatchers(PUT, "/api/v1/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")
                        .requestMatchers(DELETE, "/api/v1/user/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(POST, "/api/v1/product/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")
                        .requestMatchers(PUT, "/api/v1/product/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")
                        .requestMatchers(DELETE, "/api/v1/product/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")

                        .requestMatchers(PUT, "/api/v1/product-information/change-state/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(POST, "/api/v1/product-information/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")
                        .requestMatchers(PUT, "/api/v1/product-information/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")
                        .requestMatchers(DELETE, "/api/v1/product-information/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")

                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(withDefaults())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("POST", "PUT", "GET", "OPTIONS", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
