package com.apiGateway.apiGateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;
import java.util.function.Function;

@Component
public class JWTUtil {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
private static final Logger logger=LoggerFactory.getLogger(JWTUtil.class);

//    public void validateToken(final String token) {
//        logger.info("inside valid token of APIGATEWAY>>>>>>>");
//            Jws<Claims> claims = Jwts.parserBuilder()
//                    .setSigningKey(getSignKey())
//                    .build()
//                    .parseClaimsJws(token);
//            logger.info("after claims>>>>>>>");
//
//        //Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
//        logger.info("After valid token of APIGATEWAY>>>>>>>");
//    }
public void validateToken(final String token) {
    logger.info("Inside validateToken of API Gateway");

    try {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);

        logger.info("Token validation successful");

        // Extract user roles from JWT claims
        List<String> userRoles = claims.getBody().get("roles", List.class);

        // Check if the user has the required role (e.g., "ROLE_ADMIN")
        if (userRoles != null && userRoles.contains("ROLE_ADMIN")) {
            logger.info("User has the required role for authorization");
            // Continue processing or grant access to the resource
        } else {
            logger.error("User does not have the required role for authorization");
            // Return a 403 Forbidden response or handle the lack of authorization appropriately
            throw new JwtException("User does not have the required role for authorization");
        }

    } catch (JwtException e) {
        // Handle token validation errors
        logger.error("Token validation failed: " + e.getMessage());
        // Return a 401 Unauthorized response or handle the error appropriately
    }

    logger.info("After validateToken of API Gateway");
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
