package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenGenerator {
    private String secret="secretKey";
    private long expires=5*60*1000;

    public String generateJwtToken(Authentication auth){
        JwtUserDetails userDetails=(JwtUserDetails) auth.getPrincipal();
        Date expireDate=new Date(System.currentTimeMillis()+expires);
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expires))
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }
    public String getUsernameToken(String token){
        Claims claims =Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    public boolean tokenValidate(String token){
        if(getUsernameToken(token)!=null && !isExpired(token))
            return true;
        return false;
    }
    public boolean isExpired(String token){
        Claims claims=Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }

}