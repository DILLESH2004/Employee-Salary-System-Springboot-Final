package com.example.springjdbc.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

//    public JWTService(){
//        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGenerator.generateKey();
//            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }



    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return buildToken(claims, userDetails.getUsername());
    }

    public String buildToken(Map<String, Object> claims, String username) {
//        Map<String,Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() +1000*60*60*30))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    }


    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private<T> T extractClaim(String Token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(Token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return  Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }


    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object roles = claims.get("roles");

        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .filter(role -> role instanceof String)
                    .map(role -> (String) role)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public boolean validate(String token, String username ) {
        final String userName = extractUsername(token);
        return (userName.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

}
