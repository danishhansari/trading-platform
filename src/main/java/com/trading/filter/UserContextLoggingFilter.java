package com.trading.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserContextLoggingFilter extends OncePerRequestFilter {

    private static final String MDC_USER_KEY = "userId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Object userId = request.getAttribute("x-user-id");
            MDC.put(MDC_USER_KEY, userId != null ? userId.toString() : "anonymous");
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_USER_KEY);
        }
    }
}