package com.fyp.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import com.fyp.cli.AuthRole;
import com.fyp.cli.User;
import com.fyp.db.AuthDao;
import com.fyp.db.DatabaseConnector;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

public class TokenService {

    private static final long EXPIRATION_TIME = 86400000;

    public TokenService(AuthDao authDao) {
        this.authDao = authDao;
    }

    private AuthDao authDao;
    private static final String SECRET = "afdkghjwkrejsdgewrkjvh,shvsgbkuweyiu5.kghjwesbjfg,kw3h4e";

    public String generateToken(User user) {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        String subject = user.getEmail();
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        String token = Jwts.builder()
                .setSubject(subject)
                .claim("email", user.getEmail())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public Optional<User> decodeToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.get("email", String.class);
            User user = authDao.getUserByEmail(email);

            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isValidToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
