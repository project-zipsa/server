package navi4.zipsa.auth.utils;


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

    private static final long TOKEN_VALIDATION_TIME = 1000 * 60 * 60 * 24;

    @PostConstruct
    public void init(){
        byte[] keyBytes = Base64.getDecoder().decode(JWT_SECRET);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String userLoginId){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TOKEN_VALIDATION_TIME);

        return Jwts.builder()
                .setSubject(userLoginId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public String getUserLoginId(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}
