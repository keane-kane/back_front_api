package com.uchk.app.config;

// JwtAuthenticationFilter and JwtAuthenticationEntryPoint classes

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public  class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(jakarta.servlet.http.HttpServletRequest  request) {
        String path = request.getRequestURI();
        System.out.println("Incoming request:  " + path);
        return path.startsWith("/api/auth");
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                    jakarta.servlet.http.HttpServletResponse response,
                                    jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, java.io.IOException {


        String header = request.getHeader("Authorization");
        String path = request.getRequestURI();
        System.out.println("Incoming requestII: " + header + " " + path);

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token from "Bearer <token>"
        String token = header.replace("Bearer ", "");
        System.out.println("Incoming request: " + token + " " + path);

        try {
            if (jwtTokenUtil.validateToken(token)) {
                String username = jwtTokenUtil.getUsernameFromToken(token);

                org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                org.springframework.security.authentication.UsernamePasswordAuthenticationToken auth =
                        new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            // Token validation failed
            org.springframework.security.core.context.SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
