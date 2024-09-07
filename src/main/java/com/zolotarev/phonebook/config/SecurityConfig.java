package com.zolotarev.phonebook.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(configurer -> {
                    var corsConfigurationSource = new UrlBasedCorsConfigurationSource();
                    var corsConfiguration = new CorsConfiguration();

                    corsConfiguration.addAllowedOrigin("*");
                    corsConfiguration.addAllowedHeader(HttpHeaders.AUTHORIZATION);
                    corsConfiguration.addAllowedHeader("X-Custom-Header");
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setExposedHeaders(List.of("X-OTHER-CUSTOM-HEADER"));
                    corsConfiguration.setMaxAge(Duration.ofSeconds(30));
                    corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
                    configurer.configurationSource(corsConfigurationSource);
                    //CorsFilter corsFilter = new CorsFilter(corsConfigurationSource);
                })
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers("/users",
                                "/users/login",
                                "/users/register",
                                "/swagger-ui.html", "/swagger-ui/**",
                                "/swagger-resources", "/swagger-resources/**",
                                "/configuration/ui", "configuration/security",
                                "/v3/api-docs", "/v3/api-docs/**",
                                "/webjars/**").permitAll()
                                .requestMatchers("/users/delete/*").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .sessionManagement(sessionManagement ->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
