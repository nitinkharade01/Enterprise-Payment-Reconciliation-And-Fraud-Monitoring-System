/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.jsonwebtoken.Claims
 *  io.jsonwebtoken.Jwts
 *  io.jsonwebtoken.security.Keys
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.cloud.gateway.filter.GatewayFilterChain
 *  org.springframework.cloud.gateway.filter.GlobalFilter
 *  org.springframework.core.Ordered
 *  org.springframework.http.HttpMethod
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.stereotype.Component
 *  org.springframework.web.server.ResponseStatusException
 *  org.springframework.web.server.ServerWebExchange
 *  reactor.core.publisher.Mono
 */
package com.nitin.payment.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter
implements GlobalFilter,
Ordered {
    private static final List<String> PUBLIC_PATHS = List.of("/api/auth/login", "/api/auth/register", "/swagger-ui", "/v3/api-docs", "/actuator");
    private static final Map<String, List<String>> ROLE_RULES = Map.of("/api/auth/users", List.of("ADMIN"), "/api/transactions", List.of("ADMIN", "FINANCE_USER"), "/api/fraud-alerts", List.of("ADMIN", "AUDITOR"), "/api/reconciliation", List.of("ADMIN", "FINANCE_USER"), "/api/reports", List.of("ADMIN", "AUDITOR"), "/api/notifications", List.of("ADMIN", "FINANCE_USER", "AUDITOR"));
    private final SecretKey secretKey;

    public JwtAuthenticationFilter(@Value(value="${app.jwt.secret:${security.jwt.secret:change-this-secret-to-a-very-long-256-bit-key}}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor((byte[])secret.getBytes(StandardCharsets.UTF_8));
    }

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path;
        block10: {
            block9: {
                path = exchange.getRequest().getURI().getPath();
                if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) break block9;
                if (!PUBLIC_PATHS.stream().anyMatch(path::startsWith)) break block10;
            }
            return chain.filter(exchange);
        }
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode((HttpStatusCode)HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        try {
            Claims claims = (Claims)Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims((CharSequence)header.substring(7)).getPayload();
            List<String> roles = this.extractRoles(claims.get((Object)"roles"));
            List<String> requiredRoles = this.requiredRoles(path);
            if (!requiredRoles.isEmpty()) {
                if (roles.stream().noneMatch(requiredRoles::contains)) {
                    exchange.getResponse().setStatusCode((HttpStatusCode)HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }
            ServerWebExchange authenticatedExchange = exchange.mutate().request(builder -> builder.header("X-Authenticated-User", new String[]{claims.getSubject()}).header("X-User-Id", new String[]{String.valueOf(claims.get((Object)"userId"))}).header("X-User-Roles", new String[]{String.join((CharSequence)",", roles)})).build();
            return chain.filter(authenticatedExchange);
        }
        catch (ResponseStatusException ex) {
            exchange.getResponse().setStatusCode((HttpStatusCode)HttpStatus.valueOf((int)ex.getStatusCode().value()));
            return exchange.getResponse().setComplete();
        }
        catch (RuntimeException ex) {
            exchange.getResponse().setStatusCode((HttpStatusCode)HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private List<String> requiredRoles(String path) {
        return ROLE_RULES.entrySet().stream().filter(entry -> path.startsWith((String)entry.getKey())).map(Map.Entry::getValue).findFirst().orElse(List.of());
    }

    private List<String> extractRoles(Object rolesClaim) {
        if (rolesClaim instanceof Collection) {
            Collection roles = (Collection)rolesClaim;
            return roles.stream().map(String::valueOf).toList();
        }
        if (rolesClaim instanceof String) {
            String roles = (String)rolesClaim;
            return List.of(roles.split(","));
        }
        return List.of();
    }

    public int getOrder() {
        return -1;
    }
}
