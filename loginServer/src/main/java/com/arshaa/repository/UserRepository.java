package com.arshaa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arshaa.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User getUsersByEmailAndPassword(String email, String password);

	Optional<User> getUsersByEmail(String email);
	
	User findUserByEmail(String email);
	void save(Optional<User> user);
	User findByEmail(String email);

}
