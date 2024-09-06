package com.ktproject.backend.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

  @SuppressWarnings("deprecation")
  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

  @Value("${JWT_SECRET}")
  private String secret; // 실제 프로젝트에서는 환경 변수로 설정하는 것이 좋습니다.
  private long expirationMs = 1000 * 60 * 60; // 1시간

  // JWT 토큰 생성 메서드
  @SuppressWarnings("deprecation")
  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  @SuppressWarnings("deprecation")
  public String extractUsername(String token) {
    return Jwts.parser()
        .setSigningKey(secret)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

      // 토큰 유효성 검증
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // 토큰 만료 여부 확인
    @SuppressWarnings("deprecation")
    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}