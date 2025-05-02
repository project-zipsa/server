package navi4.zipsa.auth.security;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider{

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    private Key key;

    private final long tokenValidationTime = 1000 * 60 * 60 * 24; // 몇 분?

    @PostConstruct
    public void init(){
        byte[] keyBytes = Base64.getDecoder().decode(JWT_SECRET);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String createToken(String userLoginId){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenValidationTime);

        return Jwts.builder()
                .setSubject(userLoginId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e){ // 예외 던지기
            return false;
        }
    }

    // 토큰에서 사용자 아이디 추출
    public String getUserLoginId(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }


}















