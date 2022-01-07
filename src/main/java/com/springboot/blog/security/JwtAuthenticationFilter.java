package com.springboot.blog.security;

import java.io.IOException;
import javax.servlet.ServletException;
import org.springframework.security.core.userdetails.UserDetails;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
      final FilterChain filterChain) throws ServletException, IOException {
    
    final String token = this.getJwtFromRequest(request);
    if (StringUtils.hasText(token) && this.jwtTokenProvider.validateToken(token)) {
      final String userName = this.jwtTokenProvider.getUserNameFromJwt(token);
      final UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(userName);
      final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          (Object) userDetails, (Object) null, userDetails.getAuthorities());
      authenticationToken.setDetails((Object) new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication((Authentication) authenticationToken);
    }
    filterChain.doFilter((ServletRequest) request, (ServletResponse) response);
  }

  private String getJwtFromRequest(final HttpServletRequest request) {
    
    final String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }
    return null;
  }
}
