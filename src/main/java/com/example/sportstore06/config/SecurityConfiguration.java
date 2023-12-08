package com.example.sportstore06.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.example.sportstore06.model.Permission;
import com.example.sportstore06.repository.IPermissionRepository;
import com.example.sportstore06.security.UserService;
import com.example.sportstore06.service.RoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;


import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
    private final IPermissionRepository permissionRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] WHITE_LIST_URL = {
                "/api/v1/auth/**",
                "/api/v1/test",
                "/swagger-ui/**",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/swagger-ui.html"
        };

        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(WHITE_LIST_URL)
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

                                .requestMatchers(POST, "/api/v1/bill/**").hasAnyAuthority("ROLE_ADMIN","ROLE_CUSTOMER")
                                .requestMatchers(PUT, "/api/v1/bill/**").hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers(DELETE, "/api/v1/bill/**").hasAnyAuthority("ROLE_ADMIN")

                                .requestMatchers(POST, "/api/v1/cart/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")
                                .requestMatchers(PUT, "/api/v1/cart/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")
                                .requestMatchers(DELETE, "/api/v1/cart/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")

                                .requestMatchers(POST, "/api/v1/business/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")
                                .requestMatchers(PUT, "/api/v1/business/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS")
                                .requestMatchers(DELETE, "/api/v1/business/**").hasAnyAuthority("ROLE_ADMIN")

                                .requestMatchers(POST, "/api/v1/category/**").hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers(PUT, "/api/v1/category/**").hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers(DELETE, "/api/v1/category/**").hasAnyAuthority("ROLE_ADMIN")

                                .requestMatchers(POST, "/api/v1/comment/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")
                                .requestMatchers(PUT, "/api/v1/comment/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")
                                .requestMatchers(DELETE, "/api/v1/comment/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")

                                .requestMatchers(POST, "/api/v1/image/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")
                                .requestMatchers(PUT, "/api/v1/image/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")
                                .requestMatchers(DELETE, "/api/v1/image/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BUSINESS", "ROLE_CUSTOMER")

                                .requestMatchers(POST, "/api/v1/role/**").hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers(PUT, "/api/v1/role/**").hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers(DELETE, "/api/v1/role/**").hasAnyAuthority("ROLE_ADMIN")

                                .requestMatchers(POST, "/api/v1/sale/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS")
                                .requestMatchers(PUT, "/api/v1/sale/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS")
                                .requestMatchers(DELETE, "/api/v1/sale/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS")

                                .requestMatchers(POST, "/api/v1/user/**").hasAnyAuthority("ROLE_ADMIN","ROLE_CUSTOMER")
                                .requestMatchers(PUT, "/api/v1/user/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS","ROLE_CUSTOMER")
                                .requestMatchers(DELETE, "/api/v1/user/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS")

                                .requestMatchers(POST, "/api/v1/product/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS")
                                .requestMatchers(PUT, "/api/v1/product/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS")
                                .requestMatchers(DELETE, "/api/v1/product/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS")

                                .requestMatchers(POST, "/api/v1/product-information/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS")
                                .requestMatchers(PUT, "/api/v1/product-information/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS")
                                .requestMatchers(DELETE, "/api/v1/product-information/**").hasAnyAuthority("ROLE_ADMIN","ROLE_BUSINESS")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
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
