package br.com.blogback.blogback.config;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.blogback.blogback.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class TokenService {

    @Value("${backblog.security.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("feedback-campus")
                    .withSubject(user.getEmail())
                    .withClaim("userId", user.getId())
                    .withClaim("name", user.getName())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error ao gerar token");
        }
    }

    public JWTUserData validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("feedback-campus")
                    .build()
                    .verify(token);

            return JWTUserData.builder()
                    .id(jwt.getClaim("userId").asLong())
                    .name(jwt.getClaim("name").asString())
                    .email(jwt.getSubject())
                    .build();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
