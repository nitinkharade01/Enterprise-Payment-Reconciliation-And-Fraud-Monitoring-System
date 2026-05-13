/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.jsonwebtoken.Jwts
 *  io.jsonwebtoken.security.Keys
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.stereotype.Service
 */
package com.nitin.payment.auth.security;

import com.nitin.payment.auth.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey secretKey;
    private final long expirationMillis;

    public JwtService(@Value(value="${app.jwt.secret:${security.jwt.secret:change-this-secret-to-a-very-long-256-bit-key}}") String secret, @Value(value="${app.jwt.expiration:${security.jwt.expiration-ms:7200000}}") long expirationMillis) {
        this.secretKey = Keys.hmacShaKeyFor((byte[])secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMillis;
    }

    public String generate(User user) {
        Instant now = Instant.now();
        return Jwts.builder().subject(user.getEmail()).claims(Map.of("userId", user.getId(), "roles", user.getRoles().stream().map(role -> role.getName().name()).toList())).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(this.expirationMillis))).signWith((Key)this.secretKey).compact();
    }
}
