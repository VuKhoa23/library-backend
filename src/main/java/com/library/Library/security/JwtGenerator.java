package com.library.Library.security;

import com.library.Library.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.library.Library.security.JwtConstants.JWT_EXPIRATION;
import static com.library.Library.security.JwtConstants.JWT_SECRET;

@Component
public class JwtGenerator {
    @Data
    public class UserDetailsDTO{
        private String username;
        List<String> roles;
    }
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    UserService userService;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expiredDate = new Date(currentDate.getTime() + JWT_EXPIRATION);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUsername(username);
        List<String> tempRoles = new ArrayList<>();
        for(GrantedAuthority grantedAuthority : userDetails.getAuthorities()){
            tempRoles.add(grantedAuthority.getAuthority());
        }
        userDetailsDTO.setRoles(tempRoles);
        Long userId = userService.getUserIdByUsername(username);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiredDate)
                .claim("user-details", userDetailsDTO)
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
        return token;
    }

    public String getUsername(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(JWT_SECRET)
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
