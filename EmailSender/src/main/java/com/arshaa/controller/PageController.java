package com.arshaa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class PageController {

	
	 @RequestMapping("/service/stechnologies-github-servlet")
	    public String home( ) {
//	        System.out.println("Home page handler");
//	        // sending data to view
//	        model.addAttribute("name", "Substring Technologies");
//	        model.addAttribute("youtubeChannel", "Learn Code With Durgesh");
//	        model.addAttribute("githubRepo", "https://github.com/learncodewithdurgesh/");
	        return "My Dearest,\r\n"
	        		+ "I’m writing to you today with a heavy heart, but also with a heart full of love and hope. \r\n"
	        		+ "First and foremost, I want to sincerely apologize for whatever I uttered this afternoon. I realize how \r\n"
	        		+ "much it hurt you and how it has affected our relationship. I am truly sorry for my actions and the pain I \r\n"
	        		+ "have caused you. It was never my intention to hurt you.\r\n"
	        		+ " \r\n"
	        		+ "Yours ..";
	    }

	 @RequestMapping("/lob/compensation/jenkins")
		 public ResponseEntity<String> getProposal(@RequestParam("name") String name) {
	         String response = "<b style='color:red;'>I LOVE YOU " + name.toUpperCase() + "</b>";
	         return new ResponseEntity<>(response, HttpStatus.OK);
	 }
	 //http://localhost:8087/lob/compensation/jenkins?name=saman
	 //http://localhost:8087/service/stechnologies-github-servlet
	 
	 //http://31.187.75.117:8087/service/stechnologies-github-servlet
	 //http://31.187.75.117:8087/lob/compensation/jenkins?name=saman

	}
