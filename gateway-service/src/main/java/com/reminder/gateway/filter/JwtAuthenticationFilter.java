package com.reminder.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.security.Key;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
    
    private final String SECRET_KEY = "mySecretKeyForJWTTokenGenerationAndValidation123456789";
    
    public JwtAuthenticationFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // 跳过认证的路径
            String path = request.getPath().toString();
            if (isPublicPath(path)) {
                return chain.filter(exchange);
            }
            
            // 检查Authorization头
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Missing or invalid authorization header");
            }
            
            String token = authHeader.substring(7);
            try {
                Claims claims = validateToken(token);
                String userId = claims.get("userId").toString();
                
                // 将用户ID添加到请求头
                ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .build();
                
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
                
            } catch (Exception e) {
                return onError(exchange, "Invalid token: " + e.getMessage());
            }
        };
    }
    
    private boolean isPublicPath(String path) {
        return path.contains("/login") || 
               path.contains("/register") || 
               path.contains("/health") ||
               path.contains("/eureka") ||
               path.startsWith("/api/users/login") ||
               path.startsWith("/api/users/register");
    }
    
    private Claims validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    private reactor.core.publisher.Mono<Void> onError(ServerWebExchange exchange, String error) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
    
    public static class Config {
        // 配置类，可以添加需要的配置属性
    }
}