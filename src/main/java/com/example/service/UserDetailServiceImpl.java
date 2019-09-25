package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.model.Roles;
import com.example.model.UserDTO;
import com.example.model.Users;

@Component
public class UserDetailServiceImpl implements UserDetailsService,UserService{
	
	@Autowired
	private UserCrudRepository userCrudRepository;

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users users=userCrudRepository.findByEmail(username);
		
		if(users==null) throw new UsernameNotFoundException("User not found");
		return new User(users.getEmail(),users.getPassword(), true, true, true, true, getAUthority(users));
	}
	
	private List<GrantedAuthority> getAUthority(Users user){
		List<GrantedAuthority> authority=new ArrayList<>();
		for(Roles roles:user.getRoles()){
			authority.add(new SimpleGrantedAuthority(roles.getRole()));
		}
		return authority;
	}
	
	@Override
	public Users createUser(UserDTO userDTO) {
		// TODO Auto-generated method stub
		return null;
	}

}
