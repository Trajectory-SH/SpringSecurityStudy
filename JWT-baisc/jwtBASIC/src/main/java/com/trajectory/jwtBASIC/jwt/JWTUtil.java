package com.trajectory.jwtBASIC.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    //0.12.3 jwt
    private SecretKey secretKey;

    /**
     * 설정 파일의 비밀 문자열을 받아 UTF-8 바이트
     * → HmacSHA256 대칭키(SecretKeySpec) 로 만들고
     * •	첫 번째 인자: 키 원재료 바이트
     * •	두 번째 인자: JCA 알고리즘 이름
     * •	여기서는 Jwts.SIG.HS256.key().build().getAlgorithm()로 얻습니다.
     * •	JJWT 0.12 API가 HS256에 해당하는 JCA 이름(실제로는 "HmacSHA256")을 돌려줍니다.
     * •	결과적으로 “이 바이트로 HmacSHA256 키를 만들어라” 라는 뜻이 됩니다.
     *
     * @param secret
     */
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String username, String role, Long expiredMs){
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
