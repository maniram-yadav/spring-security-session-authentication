package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.model.UserDTO;
import com.example.model.Users;
import com.example.service.UserCrudRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder ;


	@Autowired
	private UserCrudRepository userCrudRepository;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(path= "/create",method=RequestMethod.POST)
	public ResponseEntity<Users> createUser(@RequestBody UserDTO userDTO){
		Users users=userCrudRepository.findByEmail(userDTO.getEmail());
		
		if(users==null){
			users=new Users();
		users.setName(userDTO.getName());
		users.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
		users.setEmail(userDTO.getEmail());
		
		userCrudRepository.save(users);
		}
		return new ResponseEntity<>( users,HttpStatus.OK);
	}	

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@RequestMapping(path= "/{id}",method=RequestMethod.GET)
	public ResponseEntity<Users> getUser(@PathVariable("id") Integer id){
		Users user=userCrudRepository.findByUserId(id);
		
		if(user==null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();;
		return ResponseEntity.ok(user);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Iterable<Users>> getAllUser(){
		
		return ResponseEntity.ok(userCrudRepository.findAll());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(path= "/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteUser(@PathVariable("id") Integer id){
		userCrudRepository.delete(id);
		return ResponseEntity.ok("User deleted successfully");
	}


	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<Users> updateUser(@RequestBody Users users,Principal principal){
		UserDetails userDetails=(UserDetails)principal;
		if(!users.getEmail().equalsIgnoreCase(userDetails.getUsername())){
			throw new RuntimeException("You cannot another user detail");
		}
		userCrudRepository.save(users);
		return ResponseEntity.ok(users);
	}

}
