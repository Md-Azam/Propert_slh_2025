package com.arshaa.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.arshaa.entity.User;
import com.arshaa.exceptions.EmailNotFoundException;
import com.arshaa.exceptions.UserNotFoundException;
import com.arshaa.model.PasswordReset;
import com.arshaa.repository.UserRepository;

@Service
public class UserService {

	@Autowired(required = true)
	private UserRepository repository;

	
	public List<User> findUsers() {
		return repository.findAll();
	}

	public void AddUser(User user) {
		repository.save(user);
	}
	
	public ResponseEntity updateUserByEmail(PasswordReset reset) {
		User returnedUser = repository.findByEmail(reset.getEmail());
		try {
			if (returnedUser != null) {
				if (returnedUser.getEmail().equals(reset.getEmail())) {
					Date lpcDate = new Date(reset.getLastPasswordChangedDate().getTime());
					returnedUser.setPwd_1(returnedUser.getPassword());
					returnedUser.setLastPasswordChangedDate(lpcDate);
					returnedUser.setPassword(reset.getConfirmPassword());
					if (returnedUser.isFlag() == false) {
						returnedUser.setFlag(true);
					}
					repository.save(returnedUser);
					return new ResponseEntity(returnedUser, HttpStatus.OK);
				} else {
					throw new EmailNotFoundException("Email not matched");
				}
			} else {
				throw new UserNotFoundException("User Not Found");
			}
		} catch (UserNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (EmailNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
