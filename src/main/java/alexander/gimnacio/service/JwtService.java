package alexander.gimnacio.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secreto}")
    private String secreto;

    @Value("${jwt.expiracion}")
    private Long expiracion;

    private SecretKey obtenerClaveSecreta() {
        return Keys.hmacShaKeyFor(secreto.getBytes());
    }

    public String generarToken(String correoElectronico) {
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + expiracion);

        return Jwts.builder()
                .subject(correoElectronico)
                .issuedAt(ahora)
                .expiration(fechaExpiracion)
                .signWith(obtenerClaveSecreta())
                .compact();
    }

    public String obtenerCorreoDesdeToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(obtenerClaveSecreta())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(obtenerClaveSecreta())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
