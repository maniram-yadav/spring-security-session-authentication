package com.example.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.example.model.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


	Logger LOGGER=LoggerFactory.getLogger(AuthenticationFilter.class);

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		LoginRequest log=null;
		LOGGER.debug("Inside attemptAuthentication ");
		try {
			 log=new ObjectMapper().readValue(request.getInputStream(),LoginRequest.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception : ",e);
		} 
		Authentication auth=new UsernamePasswordAuthenticationToken(log.getEmail(), log.getPassword());
		SecurityContextHolder.getContext().setAuthentication(auth);
		return getAuthenticationManager().authenticate(auth);
		
	}	
	
}
