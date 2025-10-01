package com.example.springjdbc.Filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest)servletRequest;
        System.out.println("requested url:"+req.getRequestURI());

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
