package com.example;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Roles;
import com.example.model.Users;
import com.example.service.RoleCrudRepository;
import com.example.service.UserCrudRepository;

@RestController
@SpringBootApplication
public class SessionAuthenticatorApplication implements CommandLineRunner {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder ;
	
	@Autowired
	private UserCrudRepository userCrudRepository;
	
	@Autowired
	private RoleCrudRepository roleCrudRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SessionAuthenticatorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		List<Users> userDetail=new ArrayList<>();
		List<Roles> roles=new ArrayList<>();
		
		Roles roleAdmin=new Roles();
		roleAdmin.setRole("ROLE_ADMIN");
		roles.add(roleAdmin);

		Roles roleUser=new Roles();
		roleUser.setRole("ROLE_USER");
		roles.add(roleUser);

		Roles roleAPI=new Roles();
		roleAPI.setRole("ROLE_API");
		roles.add(roleAPI);
		
		roleCrudRepository.save(roles);

		Users users=null;

		users=new Users();
		users.setName("maniram");
		users.setEmail("maniram@gmail.com");
		users.setPassword(bCryptPasswordEncoder.encode("maniram"));
		users.setRoles(roles);
		userDetail.add(users);
		
		
		users=new Users();
		users.setName("smith");
		users.setEmail("smith@gmail.com");
		users.setPassword(bCryptPasswordEncoder.encode("smith"));
		users.setRoles(roles.subList(0, 2));
		userDetail.add(users);
		
		users=new Users();
		users.setName("raju");
		users.setEmail("raju@gmail.com");
		users.setPassword(bCryptPasswordEncoder.encode("raju"));
		users.setRoles(roles.subList(1, 3));
		userDetail.add(users);
		
		users=new Users();
		users.setName("monu");
		users.setEmail("monu@gmail.com");
		users.setPassword(bCryptPasswordEncoder.encode("monu"));
		users.setRoles(roles.subList(1, 2));
		userDetail.add(users);

		userCrudRepository.save(userDetail);
	}
	
}
