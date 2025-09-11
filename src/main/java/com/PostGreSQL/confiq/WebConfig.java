package com.PostGreSQL.confiq;
import com.PostGreSQL.springSecurity.JwtFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;

@Configuration
public class WebConfig {


    private final JwtFilter jwtFilter;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(getAllowedOrigins())
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public WebMvcConfigurer staticResourceConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");
            }
        };
    }

    // Dynamically get allowed origins based on environment
    private String[] getAllowedOrigins() {
        String environment = activeProfile;
        System.out.println(environment);

        if ("prod".equalsIgnoreCase(environment)) {
            System.out.println("Online checking:");
            return new String[]{"https://159.89.172.251:3085","https://datacollection.deepseahost.com/","https://studentpanel.sohojit.com"};
        } else {
            System.out.println("local host checking:");

            return new String[]{"http://localhost:4200"};
        }
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public WebConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/students/submit",
                                "/api/users/login",
                                "/api/image/images",
                                "/api/locations",
                                "/uploads/**",
                                "/api/**").permitAll()
                        // .requestMatchers("/api/user/**").hasAuthority("ROLE_Admin") // Restrict access
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            handleForbiddenRedirect(request, response);
                        })
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Ensure JWT filter runs before Spring Security

        return http.build();
    }

    private void handleForbiddenRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loginPage = "/"; // Change this to your actual login page URL
        response.sendRedirect(loginPage);
    }

}
