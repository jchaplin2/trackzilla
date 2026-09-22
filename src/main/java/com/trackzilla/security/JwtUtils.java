package com.trackzilla.security;

import com.trackzilla.exception.JwtExpiredTokenException;
import com.trackzilla.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${trackzilla.app.jwtSecret}")
  private String jwtSecret;

  @Value("${trackzilla.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  private SecretKey signingKey(){
    try {
      byte[] digest = MessageDigest.getInstance("SHA-512").digest(jwtSecret.getBytes(StandardCharsets.UTF_8));
      return new SecretKeySpec(digest, "HmacSha512");
    }catch (NoSuchAlgorithmException e){
      throw new IllegalArgumentException("SHA-512 not available",e);
    }
  }

  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(token).getPayload().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
      throw new BadCredentialsException("Invalid JWT signature: {}" + e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
      throw new BadCredentialsException("Invalid JWT token: " + e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
      throw new JwtExpiredTokenException("JWT Token expired"+ e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
      throw new BadCredentialsException("JWT token is unsupported: {}" + e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
      throw new BadCredentialsException("JWT claims string is empty: {}" + e.getMessage());
    }

//    return false;
  }
}
