package br.com.blogback.blogback.config;

import br.com.blogback.blogback.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Desabilita CSRF para APIs
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // Configura CORS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless para APIs REST
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/backblog/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/backblog/auth/register").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/backblog/post").permitAll()
                        .requestMatchers(HttpMethod.GET, "/backblog/post/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/backblog/validate-token").permitAll()
                        .requestMatchers(HttpMethod.GET, "/backblog/category").permitAll()
                        .requestMatchers(HttpMethod.GET, "/backblog/post/search").permitAll()
                        .requestMatchers(HttpMethod.POST, "/backblog/category").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/backblog/category").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/backblog/post").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/backblog/post/{id}").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated() // Requer autenticação para qualquer outra rota
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
