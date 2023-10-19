package com.apiGateway.apiGateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.function.Function;

@Component
public class JWTUtil {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
private static final Logger logger=LoggerFactory.getLogger(JWTUtil.class);

    public void validateToken(final String token) {
        logger.info("inside valid token of APIGATEWAY>>>>>>>");
        //try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            logger.info("after claims>>>>>>>");
            // Token is valid; you can access the claims using claims.getBody()
       // } catch (Exception e) {
            // Handle token validation errors
            // Log the error, and return a "401 Unauthorized" response
            //logger.error("Token validation failed: " + e.getMessage());
            // Return a 401 Unauthorized response
       // }
        //Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        logger.info("After valid token of APIGATEWAY>>>>>>>");
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
