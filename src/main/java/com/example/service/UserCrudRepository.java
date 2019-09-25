package com.example.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Users;

@Repository
public interface UserCrudRepository extends CrudRepository<Users, Integer> {
	public Users findByEmail(String email);
	public Users findByUserId(Integer userId);
}
