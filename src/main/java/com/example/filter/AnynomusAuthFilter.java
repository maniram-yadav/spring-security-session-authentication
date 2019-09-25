package com.example.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.stereotype.Component;

public class AnynomusAuthFilter extends AnonymousAuthenticationFilter {
	
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		if(SecurityContextHolder.getContext().getAuthentication()==null){
			res.getOutputStream().write("Anynomus User".getBytes());
		}else{
			chain.doFilter(req, res);
		}
		
	}

	public AnynomusAuthFilter(String key) {
		super(key);
	}

}
