
package com.txrd.base.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtUtil {
    // 密钥至少32字节 for HS256
    private static final String SECRET_KEY_STRING = "ThisIsATXKJSecureSecretKeyForJWTGenerationAndValidation123456";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    public static String generateToken(String userId, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean validateToken(String token) {
        try{
            Claims claims = parseToken(token);
            if(claims == null){
                return false;
            }
           return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.error("Token has expired", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Invalid JWT", e.getMessage());
            return false;
        }
    }

    public static String getUserIdFromToken(String token) {
        return parseToken(token).getSubject();
    }
}
