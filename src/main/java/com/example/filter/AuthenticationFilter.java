package com.example.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.example.model.LoginRequest;
import com.example.model.ResponseDTO;
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

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		ResponseDTO responseDTO=new ResponseDTO();
		responseDTO.setResponseCode(HttpStatus.OK.value());
		responseDTO.setData("Account logged in successfully");
		response.getOutputStream().write(new ObjectMapper().writeValueAsString(responseDTO).getBytes());
	}	
	
	
	
}
