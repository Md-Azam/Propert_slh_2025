package com.arshaa.service;

import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.arshaa.common.PaymentConfirmation;
import com.arshaa.model.EmailConstants;
import com.arshaa.model.EmailResponse;
import com.arshaa.model.Response;
import com.ctc.wstx.api.EmptyElementHandler.HtmlEmptyElementHandler;

@Service
public class EmailSender {
	@Autowired(required = true)
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	public void EmailService(TemplateEngine templateEngine, JavaMailSender mailSender) {
		this.templateEngine = templateEngine;
		this.mailSender = mailSender;
	}

	EmailConstants eCons = new EmailConstants();

	// Test Email
	public void postMail() {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo("muralikrishna.miriyala@arshaa.com");
		mail.setSubject("This is the test email");
		mail.setText("This is the message from spring boot");
		mailSender.send(mail);
	}

	// Payment Remainder service
	public ResponseEntity sendRemainder(String email, String Name, double dueAmount) {
		// TODO Auto-generated method stub
		EmailResponse response = new EmailResponse();
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);
			msg.setSubject("Sree Lakshmi Heavens: Payment Reminder");

			msg.setText("Hi " + Name + "," + "\n" + "\n"
					+ "I hope you're well. This is just to remind you that payment of Due Amount: " + dueAmount + "."
					+ "\n"
					+ "I'm sure you're busy, but I'd appreciate if you could take a moment and clear the due as soon as possible."
					+ "\n" + "\n" + "Please let me know if you have any questions" + "\n" + "\n" + "Regards," + "\n"
					+ "Manager" + "\n" + "Sree Lakshmi Heavens");
			mailSender.send(msg);
			response.setMessage("email sent successfully");
			response.setStatus(true);
			return new ResponseEntity(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage("something went wrong");
			response.setStatus(false);
			return new ResponseEntity(response, HttpStatus.OK);
		}

	}

	// Onboarding confirmation email service
	public ResponseEntity OnboardingConfirmation(String email, String name, double amountPaid, String bedId,
			String buildingName) {
		// TODO Auto-generated method stub
		EmailResponse response = new EmailResponse();
		try {

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);
			msg.setSubject(eCons.ONBOARD_CONFIRMATION);

			msg.setText("Hi " + name + "," + "\n" + "\n"
					+ "Welcome to Sree Lakshmi Heavens, you are checked in to the PG successfully with the below details :"
					+ " buildingName and GuestId : " + buildingName + ", bedId : " + bedId + ", Paid Amount : "
					+ amountPaid + "." + "\n" + "\n" + "Please let me know if you have any questions" + "\n" + "\n"
					+ "Regards," + "\n" + "Manager" + "\n" + "Sree Lakshmi Heavens");
			mailSender.send(msg);
			response.setMessage("email sent successfully");
			response.setStatus(true);
			return new ResponseEntity(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage("something went wrong");
			response.setStatus(false);
			return new ResponseEntity(response, HttpStatus.OK);
		}
	}

	public ResponseEntity sendPaymentConfirmation(PaymentConfirmation pc) {
		EmailResponse response = new EmailResponse();
		try {
			if (pc.getBuildingName().equalsIgnoreCase("Sree Nilayam")) {
				pc.setHostelName("SREE NILAYAM MENS HOSTEL");
			} else if (pc.getBuildingName().equalsIgnoreCase("Vasundara Nilayam")) {
				pc.setHostelName("Vasundara Nilayam MENS HOSTEL");
			} else if (pc.getBuildingName().equalsIgnoreCase("Sree Kala Nilayam")) {
				pc.setHostelName("Sree Kala Nilayam Womens HOSTEL");
			} else if (pc.getBuildingName().equalsIgnoreCase("Ananda Nilayam")) {
				pc.setHostelName("Ananda Nilayam MENS HOSTEL");
			} else if (pc.getBuildingName().equalsIgnoreCase("SREE SANKARA NILAYAM")) {
				pc.setHostelName("SREE SANKARA NILAYAM MENS HOSTEL");
			} else if (pc.getBuildingName().equalsIgnoreCase("Vijaya Lakshmi Nilayam")) {
				pc.setHostelName("Vijaya Lakshmi Nilayam MENS HOSTEL");
			}else {
				pc.setHostelName("Durga Nilayam");
			}
			Context context = new Context();
			context.setVariable("user", pc);
			String process = templateEngine.process("emails/receipt", context);
			javax.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject(eCons.PAYMENT_CONFIRMATION);
			helper.setText(process, true);
			helper.setTo(pc.getEmail());
			mailSender.send(mimeMessage);
			response.setStatus(true);
			response.setMessage("success");
			return new ResponseEntity(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getLocalizedMessage());
			response.setStatus(false);
			return new ResponseEntity(response, HttpStatus.OK);
		}

	}

	public ResponseEntity sendInitiateCheckOutNotification(String email, String name, Date noticeDate,
			Date plannedCheckOutDate, String buildingName, String bedId) {
		EmailResponse response = new EmailResponse();
		try {

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);
			msg.setSubject(eCons.INITIATEDCHECKOUT_CONFIRMATION);

			msg.setText("Hi " + name + "," + "\n" + "\n"
					+ "Welcome to Sree Lakshmi Heavens, you are Initiated CheckOut in your  to the PG successfully with the below details :"
					+ "\n" + " buildingName : " + buildingName + "\n" + ", bedId : " + bedId + "\n"
					+ ",notice Given Date :" + noticeDate + "\n" + " , Planned CheckOutDate : " + plannedCheckOutDate
					+ "." + "\n" + "\n" + "Please let me know if you have any questions" + "\n" + "\n" + "Regards,"
					+ "\n" + "Manager" + "\n" + "Sree Lakshmi Heavens");
			mailSender.send(msg);
			response.setMessage("email sent successfully");
			response.setStatus(true);
			return new ResponseEntity(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage("something went wrong");
			response.setStatus(false);
			return new ResponseEntity(response, HttpStatus.OK);
		}

	}

	public ResponseEntity guestCheckOutNotification(String email, String name, Date noticeDate,
			java.sql.Date checkOutDate, String buildingName, String bedId) {
		EmailResponse response = new EmailResponse();
		try {

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);
			msg.setSubject(eCons.ThanksForYourStay_At_SriLakshmiHeavens);

			msg.setText("Hi " + name + "," + "\n" + "\n"
					+ "Thanks to Stay At Sree Lakshmi Heavens, you are Now CheckedOut From  your  PG successfully with the below details :"
					+ "\n" + " buildingName : " + buildingName + "\n" + ", bedId : " + bedId + "\n"
					+ ",notice Given Date :" + noticeDate + "\n" + " , CheckOutDate : " + checkOutDate + "." + "\n"
					+ "\n" + "Please let me know if you have any questions" + "\n" + "\n" + "Regards," + "\n"
					+ "Manager" + "\n" + "Sree Lakshmi Heavens");
			mailSender.send(msg);
			response.setMessage("email sent successfully");
			response.setStatus(true);
			return new ResponseEntity(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage("something went wrong");
			response.setStatus(false);
			return new ResponseEntity(response, HttpStatus.OK);
		}

	}

	public ResponseEntity sendCronjobNotification(String email) {
		EmailResponse response = new EmailResponse();
		try {

			Context context = new Context();

			String process = templateEngine.process("emails/cronJobNotify", context);
			javax.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject(eCons.CRONJOB_FAILURE_NOTIFY);
			helper.setText(process, true);
			helper.setTo(email);
			mailSender.send(mimeMessage);
			response.setStatus(true);
			response.setMessage("success");
			return new ResponseEntity(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getLocalizedMessage());
			response.setStatus(false);
			return new ResponseEntity(response, HttpStatus.OK);
		}

	}

	public ResponseEntity sendCronjobSuccessNotification(String email) {
		EmailResponse response = new EmailResponse();
		try {

			Context context = new Context();

			String process = templateEngine.process("emails/CronSuccessNotify", context);
			javax.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject(eCons.CRONJOB_SUCCESS_NOTIFY);
			helper.setText(process, true);
			helper.setTo(email);
			mailSender.send(mimeMessage);
			response.setStatus(true);
			response.setMessage("success");
			return new ResponseEntity(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getLocalizedMessage());
			response.setStatus(false);
			return new ResponseEntity(response, HttpStatus.OK);
		}

	}

	public ResponseEntity notifyGuestForCheckOut(String email, String name, java.util.Date plannedCheckOutDate,
			String buildingName, String bedId) {
		EmailResponse response = new EmailResponse();
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);
			msg.setSubject(eCons.CHECKOUT_ALERT);

			msg.setText("Hi " + name + "," + "\n" + "\n"
					+ "We would like to notify you just your planned checkout date is :" + plannedCheckOutDate
					+ " In : " + buildingName + " with " + ", bedId : " + bedId + "\n"
					+ "Please contact manager if you want to extend your stay in hostel or any questions" + "\n" + "\n"
					+ "Regards," + "\n" + "Manager " + buildingName);
			mailSender.send(msg);
			response.setMessage("email sent successfully");
			response.setStatus(true);
			return new ResponseEntity(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage("something went wrong");
			response.setStatus(false);
			return new ResponseEntity(response, HttpStatus.OK);
		}

	}

//	private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
//	    MimeMessage message = emailSender.createMimeMessage();
//	    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//	    helper.setTo(to);
//	    helper.setSubject(subject);
//	    helper.setText(htmlBody, true);
//	    emailSender.send(message);
//	}

}
