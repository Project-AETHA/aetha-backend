package com.nighthawk.aetha_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public SecurityFilterChain defaultFilterChain (HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                //* Authorizing routes with the matching urls
                .authorizeHttpRequests
                        (auth -> auth
                            .requestMatchers(
                                    "/images/**",
                                    "/api/auth/**",
                                    "/login",
                                    "/error",
                                    "/api",
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/api/files/**",
                                    "/api/books",
                                    "/api/ebooks/**", // TODO : Check this line
                                    "/api/novels/**",
                                    "/api/chapters/**",
                                    "/api/config/**",
                                    "/api/payment/success",
                                    "/api/payment/cancel",
                                    "/api/subscription/get-tiers/**",
                                    "/api/poems/**",
                                    "/api/comments/getComments",
                                    "/api/ratings/getRatings",
                                    "/api/forum/getForum"
                            )
                            .permitAll()
                            .anyRequest().authenticated()
                        )
                .oauth2Login(Customizer.withDefaults())
                //* Making the session management stateless, no cookies will be saved
                .sessionManagement
                        (session-> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                .exceptionHandling
                        (exception -> exception
                                .authenticationEntryPoint(unauthorizedHandler)
                        )
                //* Allowing basic authentication with an alert
                //? Removing the basic authentication and form login capabilities
//!                .httpBasic(Customizer.withDefaults())
//!                .formLogin(Customizer.withDefaults())
                //* Allowing form authentication with the help of a form
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration builder
    ) throws Exception {
        return builder.getAuthenticationManager();
    }

}
