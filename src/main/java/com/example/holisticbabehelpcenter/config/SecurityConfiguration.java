package com.example.holisticbabehelpcenter.config;
import com.example.holisticbabehelpcenter.enumeration.Role;
import com.example.holisticbabehelpcenter.exceptions.CustomAccessDeniedHandler;
import com.example.holisticbabehelpcenter.exceptions.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SecurityConfiguration  {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomAuthenticationEntryPoint customEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers( "/user/Register","/user/auth","/user/ConfirmAccount/**","/user/forgot-password/**").permitAll()
                                .requestMatchers("/config/disableAccount/**", "/config/enableAccount/**","/config/registerAdmin","/config/getAllUsers").hasAnyRole("ADMIN")
                                .requestMatchers("/reclamations/","/reclamations/user/**","/reclamations/reclamation/resolve/").hasRole("ADMIN")
                                .requestMatchers("/reclamations/create/**").hasAnyRole("ADMIN","USER")
                                .requestMatchers("/ws/**").permitAll()
                                .requestMatchers("/profile/**").authenticated()
                                .requestMatchers("/user/logout").authenticated()
                                .requestMatchers("/ExchangedMessages/**").authenticated()
                                .requestMatchers("/user/onlineUsers").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> {
                    e.authenticationEntryPoint(customEntryPoint);
                    e.accessDeniedHandler(customAccessDeniedHandler);
                });
        return httpSecurity.build();
    }
}
