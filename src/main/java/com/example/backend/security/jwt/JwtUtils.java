package com.example.backend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    private SecretKey key;

    // GENERAMOS EL TOKEN DE ACCESO
    public String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities){
        Instant now = Instant.now();
        Instant expirationTime = now.plus(Long.parseLong(timeExpiration), ChronoUnit.MILLIS); // ChronoUnit.MILLIS indica que el valor que se está sumando está en milisegundos.

        // Convertir los roles a una lista de strings
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder() // Constructor
                .subject(username) // Sujeto basa en el username del usuario
                .issuedAt(Date.from(now)) //Fecha de emisión. Convertir Instant a Date ya que issuedAt y expiration aceptan objetos de tipo Date
                .expiration(Date.from(expirationTime)) // Fecha de expiración
                .claim("roles", roles)
                .signWith(key) // Firma del token, usando una clave secreta
                .compact();
    }

    // FIRMA DEL METODO
    @PostConstruct
    public void init() {
        // Asegurarse de que la clave sea al menos de 256 bits (32 bytes)
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("La clave secreta debe tener al menos 256 bits (32 bytes)");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // VALIDAR EL TOKEN DE ACCESO
    public boolean isTokenValid(String token){
        try {
            Jwts.parser()
                    .verifyWith(key) // Se decodifica usando la clave secreta
                    .build()
                    .parseSignedClaims(token) // // Parsear(descomponer) el token
                    .getPayload(); // Extraer el cuerpo del token (claims)

            return true; // Si no se encuentran problemas
        } catch(Exception e) {
            log.error("Token inválido, error: " + e.getMessage());
            return false;
        }
    }

    // OBTENEMOS TODOS LOS CLAIMS DEL TOKEN
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // OBTENER UN SOLO CLAIM
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction){
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // OBTENER EL USERNAME DEL TOKEN
    public String getUsernameFromToken(String username){
        return getClaim(username, Claims :: getSubject);
    }
}
