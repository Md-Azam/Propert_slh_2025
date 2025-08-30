//package com.arshaa.cronService;
//
//import java.sql.Date;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.web.client.RestTemplate;
//
//import com.arshaa.entity.CronNotification;
//import com.arshaa.entity.Guest;
//import com.arshaa.exception.ResourceNotFoundException;
//import com.arshaa.model.EmailResponse;
//import com.arshaa.model.PaymentRemainder;
//import com.arshaa.repository.GuestRepository;
//import com.arshaa.repository.SecurityDepositRepo;
//import com.arshaa.util.DateFormates;
//
//@EnableScheduling
//public class CronJobDueCalculation {
//
//	@Autowired(required = true)
//	private GuestRepository repository;
//	@Autowired
//	private CronServices cronServices;
//	@Autowired
//	@Lazy
//	private RestTemplate template;
//	private static final String cronJobSuccessEmailURL = "http://emailService/mail/sendCronjobSuccessNotification/";
//	private static final String cronJobFailEmailURL = "http://emailService/mail/cronjobFailureNotify/";
//
//	public void calculateRegularGuestDue(String guestId) throws ResourceNotFoundException {
//		try {
//			//taking guest information
//			Guest guest=repository.findById(guestId);
//			//check if guest is active and regular or not
//			if(guest.getOccupancyType().equalsIgnoreCase("regular") && guest.getGuestStatus().equalsIgnoreCase("active")) {
//				Double previousDue=guest.getDueAmount();
//				Double defaultRent=guest.getDefaultRent();
//				Double dueAmountCalculation=0.0;
//				ZonedDateTime lastBillGenerationDate=DateFormates.sqlToZoned(guest.getLastBillGenerationDate());
//				//for testing
//				LocalDate localDate = LocalDate.parse("2023-02-25");      
//				ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
//				ZonedDateTime currentDate=zonedDateTime;
//
////				ZonedDateTime currentDate=ZonedDateTime.now();
//				Date CreatedLastbillGenerationDate = null;
//				Date CreatedBillGeneratedTillDate = null;
//				Long differeneceDays=0L;
//				Long lastDayOfMonth=0L;
//			    //get the difference days between lastbillgenerationdate and current date  	
//				differeneceDays=DateFormates.zonedDateTimeDifference(lastBillGenerationDate, currentDate, ChronoUnit.DAYS);
//				lastDayOfMonth=DateFormates.lastDayOfMonth(currentDate);
//				if(differeneceDays==lastDayOfMonth) {
//					dueAmountCalculation=previousDue+defaultRent;
//					CreatedLastbillGenerationDate=DateFormates.ZonedToSql(currentDate);
//					CreatedBillGeneratedTillDate=DateFormates.ZonedToSql(currentDate.plusDays(lastDayOfMonth));
//				}
//				else {
//					dueAmountCalculation=previousDue;
//				}
//				guest.setDueAmount(dueAmountCalculation);
//				guest.setLastBillGenerationDate(CreatedLastbillGenerationDate);
//				guest.setBillGeneratedTill(CreatedBillGeneratedTillDate);
//				repository.save(guest);
//			}
//		}catch(Exception e)
//		{
//			throw new ResourceNotFoundException(false,"Something went wrong");
//		}	
//	}
//	
//	// Update Due
//	public ResponseEntity updateGuestDue() {
//		{
//			List<PaymentRemainder> getList = new ArrayList();
//			List<EmailResponse> getRes = new ArrayList<>();
//			List<Guest> getGuest = repository.findAll();
//			System.out.println("List:" + getGuest);
//			if (!getGuest.isEmpty()) {
//				getGuest.forEach(g -> {
//					if (g.getOccupancyType().equalsIgnoreCase("Regular")
//							&& g.getGuestStatus().equalsIgnoreCase("Active")) {
//						try {
//							calculateRegularGuestDue(g.getId());
//						} catch (ResourceNotFoundException e) {
//							e.printStackTrace();
//						}
//					}
//				});
//				return new ResponseEntity("Success", HttpStatus.OK);
//			} else {
//				return new ResponseEntity("Nodue", HttpStatus.OK);
//			}
//		}
//	}
//	
//	
//	/*
//	 * cron=
//	 * (" *                  *            *           *            *                 *  "
//	 * ) seconds minutes hours days months years
//	 * 
//	 * -------- while running this cron job please be patience----------
//	 */	
//	@Scheduled(cron = "0 7 16 * * *", zone = "Asia/Kolkata")
//	public void addDue2() throws Exception {
//		CronNotification cronNotification = new CronNotification();
//		ArrayList<String> devEmails = new ArrayList<>();
//		devEmails.add("faisal.azam@arshaa.com");
//		devEmails.add("sandhya.bandaru@arshaa.com");
//		try {
//		    cronNotification.setCronType("first cron ran");
//                    cronNotification.setDate(new java.util.Date());
//                    cronServices.saveCronNotification(cronNotification);
//		    updateGuestDue();
//		    devEmails.forEach(e -> {
//				template.getForObject(this.cronJobSuccessEmailURL + e, String.class);
//			});
//		} catch (Exception exec) {
//			cronNotification.setCronType("cron is failed; I Am sorry babu");
//			cronNotification.setDate(new java.util.Date());
//			cronServices.saveCronNotification(cronNotification);
//			devEmails.forEach(e -> {
//				template.getForObject(this.cronJobFailEmailURL + e, String.class);
//			});
//		}
//	}
//	
//	// see this is cron job timings
//	@Scheduled(cron = "0 6 16 * * *", zone = "Asia/Kolkata")
//	public void addDue() throws Exception {
//		ArrayList<String> devEmails = new ArrayList<>();
//		devEmails.add("manohar.chimata@arshaa.com");
//		devEmails.add("sandhya.bandaru@arshaa.com");
//		devEmails.add("faisal.azam@arshaa.com");
//		try {
//			CronNotification cronNotification = new CronNotification();
//			updateGuestDue();
//			System.out.println("updating due....");
//			cronNotification.setCronType("first cron ran");
//			cronNotification.setDate(new java.util.Date());
//			cronServices.saveCronNotification(cronNotification);
//			devEmails.forEach(e -> {
//				template.getForObject(this.cronJobSuccessEmailURL + e, String.class);
//			});
//		} catch (Exception exec) {
//			CronNotification cronNotification = new CronNotification();
//			cronNotification.setCronType("cron is failed; I Am sorry babu");
//			cronNotification.setDate(new java.util.Date());
//			cronServices.saveCronNotification(cronNotification);
//			devEmails.forEach(e -> {
//				template.getForObject(this.cronJobFailEmailURL + e, String.class);
//			});
//			System.out.println(exec.getMessage());
//		}
//	}
//	
//	@Scheduled(cron = "0 6 16 * * *", zone = "Asia/Kolkata")
//	public void azamDue() {
//		System.out.println("This is test cron");
//	}
//	
//	
//}
