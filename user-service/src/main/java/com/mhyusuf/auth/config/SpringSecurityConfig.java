package com.mhyusuf.auth.config;

import com.mhyusuf.auth.filter.JsonUsernamePasswordAuthFilter;
import com.mhyusuf.auth.service.JwtAuthenticationEntryPoint;
import com.mhyusuf.auth.service.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.mhyusuf.auth.service.JwtTokenProvider;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {

        // JSON LOGIN FILTER
        JsonUsernamePasswordAuthFilter jsonFilter = new JsonUsernamePasswordAuthFilter();
        jsonFilter.setAuthenticationManager(authManager);

        // SUCCESS HANDLER → return Bearer token
        jsonFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            String token = jwtTokenProvider.generateToken(authentication);

            response.setContentType("application/json");
            response.getWriter().write(
                    "{ \"result\": true, \"token\": \"Bearer " + token + "\" }"
            );
        });

        // FAILURE HANDLER
        jsonFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{ \"result\": false, \"error\": \"Invalid username or password\" }"
            );
        });

        // SECURITY RULES
        http.csrf(csrf -> csrf.disable());

//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/", "/greeting", "/error").permitAll()
//                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
//                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                .requestMatchers("/actuator/**").permitAll()
//                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .anyRequest().authenticated()
//        );

        http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
        );

        http.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint));

        http.sessionManagement(session ->
                session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
        );

        // LOGIN FILTER
        http.addFilterAt(jsonFilter, UsernamePasswordAuthenticationFilter.class);

        // JWT FILTER (FIXED)
        http.addFilterBefore(jwtAuthenticationFilter, JsonUsernamePasswordAuthFilter.class);

        return http.build();
    }
}
