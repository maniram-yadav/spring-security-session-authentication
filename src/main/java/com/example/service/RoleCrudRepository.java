package com.example.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Roles;
import com.example.model.Users;

@Repository
public interface RoleCrudRepository extends CrudRepository<Roles, Integer> {
	public Users findByRole(String role);
	public Users findByRoleId(Integer roleId);
}
