package com.alshadidi.meet.security.jwt;

import com.alshadidi.meet.models.AppUser;
import com.alshadidi.meet.models.dto.VerifyUserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class JwtUtil {
    private final int EXPIRATION_MILLIS = 1000 * 60 * 60;
    public static KeyPair keyPair = getRSAKeys();

    public String generateToken(AppUser appUser) {
        return Jwts.builder()
                .setSubject(appUser.getUsername())
                .setId(String.valueOf(appUser.getId()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(keyPair.getPrivate())
                .compact();
    }

    public VerifyUserDTO parseToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new VerifyUserDTO(claims.getSubject(), claims.getId(), token);
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static KeyPair getRSAKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
