package com.example.app.security;
 
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
 
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
 
@Component
public class JwtUtil {
 
    // 秘密鍵は安全に管理してください
    private final String SECRET_KEY = "mySecretKey123456789012345678901234"; 
    private final long EXPIRATION_MS = 1000 * 60 * 60 * 24; // 
    
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
 
    // JWT生成
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
 
    // JWTからユーザーID取得
    public String extractUserId(String token) {
        return getClaims(token).getSubject();
    }
 
    // JWTの有効期限チェック
    public boolean validateToken(String token) {
        try {
            return !getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
 
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}