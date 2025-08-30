package com.arshaa.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.arshaa.common.CheckOutConfirmation;
import com.arshaa.common.InitiatingCheckOut;
import com.arshaa.common.OnboardingConfirmation;
import com.arshaa.common.PaymentConfirmation;
import com.arshaa.entity.PaymentRemainder;
import com.arshaa.service.EmailSender;

@RestController
@RequestMapping("/mail")
public class EmailSenderController {

	@Autowired
	EmailSender emailSender;
	@Autowired
	@Lazy
	private RestTemplate template;

// Test Email
	@PostMapping(value = "/postmail")
	public String send() throws AddressException, MessagingException, IOException {
		emailSender.postMail();

		return "Email Sent Successfully";
	}
	
	//check out alert
	@PostMapping("/sendCheckoutAlerts")
	public ResponseEntity alertGuestForCheckout(@RequestBody CheckOutConfirmation checkout) 
		throws AddressException , MessagingException, IOException {
			return emailSender.notifyGuestForCheckOut(checkout.getEmail(),checkout.getName(),checkout.getPlannedCheckOutDate(),checkout.getBuildingName(),checkout.getBedId());
		
	}
//Payment Remainder Email for due Amount	
	@PostMapping("/sendPaymentRemainder")
	public ResponseEntity sendPaymentRemainder(@RequestBody PaymentRemainder sendPayRem)
			throws AddressException, MessagingException, IOException {
		return emailSender.sendRemainder(sendPayRem.getEmail(), sendPayRem.getName(), sendPayRem.getDueAmount());
	}
	@PostMapping("/sendOnboardingConfirmation")
	public ResponseEntity sendOnboardingConfirmation(@RequestBody OnboardingConfirmation onBoard)
			throws AddressException, MessagingException, IOException {
		return emailSender.OnboardingConfirmation(onBoard.getEmail(), onBoard.getName(), onBoard.getAmountPaid(),
				onBoard.getBedId(), onBoard.getBuildingName());
	}
	
	@PostMapping("/sendPaymentConfirmation")
	public ResponseEntity sendPaymentConfirmation(@RequestBody PaymentConfirmation pconfirm)
			throws AddressException, MessagingException, IOException {
		return emailSender.sendPaymentConfirmation(pconfirm);
	}
	
	 @GetMapping("/cronjobFailureNotify/{email}")
	    public ResponseEntity sendCronjobNotification(@PathVariable String email)
	    {
	        return emailSender.sendCronjobNotification(email);
	    }
	    
	    @GetMapping("/sendCronjobSuccessNotification/{email}")
	    public ResponseEntity sendCronjobSuccessNotification(@PathVariable String email)
	    {
	        return emailSender.sendCronjobSuccessNotification(email);
	    }
	
	
	@PostMapping("/sentInitiatingCheckOutRemainder")
	public ResponseEntity sendInitiatingCheckOutConfirmation(@RequestBody InitiatingCheckOut notice) 
		throws AddressException, MessagingException , IOException {
		return  emailSender.sendInitiateCheckOutNotification(notice.getEmail(),notice.getName(),notice.getNoticeDate() ,notice.getPlannedCheckOutDate(),notice.getBuildingName(), notice.getBedId());
	}
	
	@PostMapping("/guestCheckOutNotification")
	public ResponseEntity guestCheckOutNotification(@RequestBody CheckOutConfirmation checkout) 
	throws AddressException , MessagingException, IOException {
		return emailSender.guestCheckOutNotification(checkout.getEmail(),checkout.getName(),checkout.getNoticeDate(),checkout.getCheckOutDate(),checkout.getBuildingName(),checkout.getBedId());	
	}	
	
	
}