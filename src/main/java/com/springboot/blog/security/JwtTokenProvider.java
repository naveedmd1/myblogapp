// 
// Decompiled by Procyon v0.5.36
// 

package com.springboot.blog.security;

import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import com.springboot.blog.exception.BlogApiException;
import org.springframework.http.HttpStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider
{
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;
    
    public String generateToken(final Authentication authentication) {
        final String userName = authentication.getName();
        final String token = Jwts.builder().setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 86400000L)).signWith(SignatureAlgorithm.HS256, this.jwtSecret).compact();
        return token;
    }
    
    public String getUserNameFromJwt(final String token) {
        final Claims claims = (Claims)Jwts.parser().setSigningKey(this.jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    
    public boolean validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(this.jwtSecret).parse(token);
            return true;
        }
        catch (SignatureException e) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
        }
        catch (MalformedJwtException ex) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        }
        catch (ExpiredJwtException ex2) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        }
        catch (UnsupportedJwtException ex3) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        }
        catch (IllegalArgumentException ex4) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }
}
