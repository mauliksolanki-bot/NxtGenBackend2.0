package com.nxtgen.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class NxtGenJwtAuthenticationFilter extends OncePerRequestFilter {
  private final NxtGenJwtService jwt;

  public NxtGenJwtAuthenticationFilter(NxtGenJwtService jwt) { this.jwt = jwt; }

  @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) { chain.doFilter(request, response); return; }
    try {
      var claims = jwt.parse(header.substring(7));
      var authorities = java.util.List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role", String.class)));
      var authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (JwtException | IllegalArgumentException ignored) {
      SecurityContextHolder.clearContext();
    }
    chain.doFilter(request, response);
  }
}
