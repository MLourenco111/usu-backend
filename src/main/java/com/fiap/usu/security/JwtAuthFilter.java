package com.fiap.usu.security;

import com.fiap.usu.dtos.auth.UserPrincipalDto;
import com.fiap.usu.entities.User;
import com.fiap.usu.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        final String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.validateToken(token)) {
                Long userId = Long.parseLong(jwtUtil.getUserId(token));
                User user = userService.getUser(userId);
                UserPrincipalDto principal = new UserPrincipalDto(userId, user.getEmail(), Collections.emptyList());

                var roles = jwtUtil.getRoles(token).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                var auth = new UsernamePasswordAuthenticationToken(principal, null, roles);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(req, res);
    }
}
