package com.example.backend.security;

import com.example.backend.security.filters.JwtAuthenticationFilter;
import com.example.backend.security.filters.JwtAuthorizationFilter;
import com.example.backend.security.jwt.JwtUtils;
import com.example.backend.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtAuthorizationFilter authorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/usuario/registrar").permitAll();
                    auth.requestMatchers("/error").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/usuario/**").hasRole("USER");

                    // Endpoint público para ver libros
                    auth.requestMatchers(HttpMethod.GET, "/api/libro").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/libro/**").permitAll();

                    // Endpoints protegidos para crear, actualizar o eliminar libros (solo ADMIN)
                    auth.requestMatchers(HttpMethod.POST, "/api/libro").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/libro/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/libro/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/autor").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/autor").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/autor/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/editorial").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/editorial").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/editorial/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/subcategoria").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/subcategoria").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/subcategoria/**").hasRole("ADMIN");

                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite peticiones desde cualquier origen
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedOrigins(Arrays.asList("https://tu-libro-favorito.vercel.app"));

        // Permite estos métodos HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));

        // Permite todos los headers
        // Headers básicos necesarios
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",  // Para tu JWT
                "Content-Type"    // Para enviar JSON
        ));

        // Necesario para JWT
        configuration.setAllowCredentials(true);

        // Aplica esta configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
                                                       PasswordEncoder passwordEncoder) throws Exception {

        AuthenticationManagerBuilder authManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authManagerBuilder
                .userDetailsService(userDetailsService) // // Definir cómo cargar los detalles de los usuarios (desde la base de datos)
                .passwordEncoder(passwordEncoder); // // Define cómo validar las contraseñas (encriptación con BCrypt)

        return authManagerBuilder.build();
    }

}
