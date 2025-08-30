package com.arshaa.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arshaa.entity.LoginInfo;
import com.arshaa.entity.User;
import com.arshaa.exceptions.UserNotFoundException;
import com.arshaa.model.PasswordReset;
import com.arshaa.model.Response;
import com.arshaa.model.UserModel;
import com.arshaa.model.UserStatus;
import com.arshaa.repository.UserRepository;
import com.arshaa.service.LoginInfoServce;
import com.arshaa.service.UserService;

@RestController
@RequestMapping("/login")
@CrossOrigin("*")
public class LoginController {

    @Autowired(required = true)
    private UserService service;
    
    @Autowired
    private LoginInfoServce loginInfoService ;
    
    @Autowired(required = true)
    UserRepository userRepo;

    @GetMapping("/getUsers")
    private List<User> getUsers() {
        return service.findUsers();
    }
    
	/*
	 * @GetMapping("/getUsersByUserId/{") private List<User> getUsersById() { return
	 * service.findUsers(); }
	 */    
    @GetMapping ("/getUsersByUserEmailId")
    private ResponseEntity<UserModel> getUsersByEmailId(@RequestParam String email, @RequestParam String password) {
		 Response<UserModel> response=new Response<UserModel>();
		 LoginInfo info = new LoginInfo();
    	//List<User> dataUser=userRepo.findAll();
		UserModel um=new UserModel();
		try {
		Optional<User> user=userRepo.getUsersByEmail(email);
    	if(user.isPresent())
    	{    	    	
    		if(user.get().getPassword().equals(password))
    		{

    			response.setStatus(true);
    			response.setMessage("Login Success");
    		    if(response.isStatus()==true)
        		{
        	        User user1=userRepo.findUserByEmail(email);
        			user1.setLoginStatus(true);
        			user1.setUserLoggedinDate(Timestamp.valueOf(LocalDateTime.now()));
        	        userRepo.save(user1);
        		}
    		    um.setUserId(user.get().getUserId());
    	    	um.setBuildingId(user.get().getBuildingId());
    	    	um.setEmail(user.get().getEmail());
    	    	um.setFlag(user.get().isFlag());
    	    	um.setLastPasswordChangedDate(user.get().getLastPasswordChangedDate());
    	    	um.setUserType(user.get().getUserType());
    		    response.setData(um);
    		    
    		    
    		    info.setBuildingId(user.get().getBuildingId());
    		    info.setEmail(user.get().getEmail());
    		    info.setUserLoggedinDate(new Date());
    		    info.setUserName(user.get().getUserName());
    		    info.setUserType(user.get().getUserType());
    		    loginInfoService.saveLoginDetails(info);
    	        return new ResponseEntity(response,HttpStatus.OK);
    	        
    		}
    		
    		else {
    			response.setStatus(false);
    	    	response.setMessage("Invalid Mail or Password");
    	        return new ResponseEntity(response,HttpStatus.OK);
    		}
    		
    	}
    	else {
    		response.setStatus(false);
    		response.setMessage("Enter valid emailId");
	        return new ResponseEntity(response,HttpStatus.OK);
    	}
		}
		catch(Exception e)
		{
			response.setStatus(false);
			response.setMessage("Something went wrong please try again");
	        return new ResponseEntity(response,HttpStatus.OK);
		}
    }

    
    @PostMapping("/addUsers")
    private ResponseEntity<User> saveUsers(@RequestBody User user) {
    	try {
    		Timestamp tSqlDate1 =Timestamp.valueOf(LocalDateTime.now());
			 user.setUserLoggedinDate(tSqlDate1);    	
			 java.sql.Date tSqlDate = new java.sql.Date(user.getLogoutDate().getTime());
   user.setLogoutDate(tSqlDate);
   service.AddUser(user);
   return new ResponseEntity("User Added Successfully",HttpStatus.OK);
    	}
    	catch(Exception e)
    	{
    		   return new ResponseEntity(e.getMessage(),HttpStatus.OK);

    	}
    	
    }
    @PutMapping("userLogout/{email}")
    private ResponseEntity<User> addlogin(@PathVariable String email) {
		 Response response=new Response();
		 UserStatus us=new UserStatus();
		 try {
			 User user=userRepo.findUserByEmail(email);
		    	user.setLoginStatus(false);
		    	java.sql.Date tSqlDate = new java.sql.Date(user.getLogoutDate().getTime());
		        user.setLogoutDate(tSqlDate);
		        userRepo.save(user);
		        us.setLoginStatus(user.isLoginStatus());
		        response.setStatus(true);
		        response.setData(us);
		        response.setMessage("Logout successfully");
		        
		        return new ResponseEntity(response,HttpStatus.OK);

		 }
		 catch (Exception e) {
			// TODO: handle exception
			 response.setStatus(false);
			 response.setMessage(e.getMessage());
		     return new ResponseEntity(response,HttpStatus.OK);
		}
    	    }
       
    @PutMapping("/updateUserByEmail")
    public ResponseEntity<UserNotFoundException> updateUserByEmail(@RequestBody PasswordReset reset){
        return service.updateUserByEmail(reset);
    }
  
}
