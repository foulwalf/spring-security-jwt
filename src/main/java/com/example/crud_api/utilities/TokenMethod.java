package com.example.crud_api.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.crud_api.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Slf4j
public class TokenMethod {
    @Autowired
    private static UserRepository userRepository;
    public static String createAccesToken(User user, String issuer, String secret) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30)))
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public static String refreshToken(User user, String issuer, String secret, String tokenToRefresh) {
        Map<String, Claim> claims = JWT.decode(tokenToRefresh).getClaims();
        Map<String, Object> refreshTokenProperties = new HashMap<>();
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withExpiresAt(new Date(claims.get("exp").asLong() + TimeUnit.DAYS.toMillis(1)))
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public static UsernamePasswordAuthenticationToken getInformationFromToken(String token, String secret) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
