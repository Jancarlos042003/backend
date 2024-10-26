package com.example.backend.security.filters;

import com.example.backend.security.jwt.JwtUtils;
import com.example.backend.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//  Intercepta solicitudes HTTP a rutas protegidas y verifica si contienen un token JWT válido
//  y se ejecuta solo una vez por solicitud
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        // OBTENEMOS EL TOKEN DEL ENCABEZADO(HEADER) HTTP
        String tokenHeader = request.getHeader("Authorization");

        // Se verifica si el token existe y tiene el formato correcto "Bearer "
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")){

            // Se extrae el token real
            String token = tokenHeader.substring(7);

            // Se valida el token
            if (jwtUtils.isTokenValid(token)){
                // Se extrae el nombre del usuario del token
                String username = jwtUtils.getUsernameFromToken(token);

                // Se cargan los detalles completos del usuario
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                //Se crea un objeto de autenticación
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities()); // Contiene el nombre de usuario y sus roles/autoridades

                // Se establece la autenticación en el contexto de seguridad
                // Esto marca al usuario como autenticado para el resto de la aplicación.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Se pasa la solicitud al siguiente filtro
        filterChain.doFilter(request, response);
    }
}
