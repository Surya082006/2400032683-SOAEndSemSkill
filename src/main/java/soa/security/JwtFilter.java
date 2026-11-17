package soa.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        return !("/api/users".equals(
                    request.getRequestURI())
                && "POST".equalsIgnoreCase(
                    request.getMethod()));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            unauthorized(response);
            return;
        }

        String token = header.substring(7);

        if (!jwtUtil.validateToken(token)) {
            unauthorized(response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void unauthorized(
            HttpServletResponse response)
            throws IOException {

        response.setStatus(401);
        response.setContentType("text/plain");
        response.getWriter().write(
                "Token Validation Failed");
    }
}