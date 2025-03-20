package com.kgy.usedCar.config;

import com.kgy.usedCar.dto.response.user.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret_key:not found!}")
    private String secretKey;

    @Value("${jwt.token_expired_time:not found!}")
    private long expirationTime;

    public String generateToken(UserDto userDto){
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("userId", userDto.getUserId());
        claims.put("role",  "ROLE_" + userDto.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(){
        byte[] keyByte = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public boolean isExpired(String token){
        Date expiredDate = getClaims(token).getExpiration();
        return expiredDate.before(new Date());
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token){
        return getClaims(token).get("userId").toString();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String role = claims.get("role").toString();

        UserDto userDto = new UserDto(claims.get("userId").toString(), null, null, null, null, null);
        return new UsernamePasswordAuthenticationToken(
                userDto.getUserId(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }

}
