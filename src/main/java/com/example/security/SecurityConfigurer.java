package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.filter.AccessDeniedException;
import com.example.filter.AnynomusAuthFilter;
import com.example.filter.AuthFailureHandler;
import com.example.filter.AuthenticationFilter;
import com.example.filter.LogOutHandler;
import com.example.filter.LogOutSuccessHandler;
import com.example.service.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true,order=2)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailServiceImpl userService;
	
	
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		
		return super.userDetailsServiceBean(); 
	}
	
	@Bean
	public BCryptPasswordEncoder getEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationFilter getAuthenticationFilter() throws Exception{
		AuthenticationFilter auth=new AuthenticationFilter();
		auth.setAuthenticationManager(authenticationManager());	
		return auth;
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(getEncoder());
	}
	
	
	
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected UserDetailsService userDetailsService() {
		return this.userService; 
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();


		http.authorizeRequests()
			.antMatchers(HttpMethod.POST,"/login").permitAll()
			.antMatchers("/h2-console/**").permitAll()
			.antMatchers(HttpMethod.POST,"/create").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").usernameParameter("userid")
			.passwordParameter("pass").failureHandler(new AuthFailureHandler())
			.and()
			.logout()
			.logoutUrl("/logout")
			.permitAll()
			.logoutSuccessHandler(new LogOutSuccessHandler())
			.addLogoutHandler(new LogOutHandler()).clearAuthentication(true)
			.deleteCookies("JSESSIONID")
			.invalidateHttpSession(true)
			.and()
			.addFilterBefore(getAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
			.anonymous().authenticationFilter(new AnynomusAuthFilter("Anynomus"))
			.and()
			.exceptionHandling().accessDeniedHandler(new AccessDeniedException());
			
	}
	

}
