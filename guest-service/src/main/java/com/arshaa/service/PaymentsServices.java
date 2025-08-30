//package com.arshaa.service;
//
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import com.arshaa.common.PaymentRemainderData;
//import com.arshaa.entity.Guest;
//import com.arshaa.model.DueGuestsList;
//import com.arshaa.repository.GuestRepository;
//@Service
//public class PaymentsServices {
//
//	@Autowired(required = true)
//	private GuestInterface service;
//
//	@Autowired
//	@Lazy
//	private RestTemplate template;
//
//	@Autowired(required = true)
//	private GuestRepository repository;
//
////	public List<GuestsInNotice> getAll() {
////		List<Guest> getList = repository.findByCheckOut();
////		List<GuestsInNotice> gin = new ArrayList<>();
//	public List<DueGuestsList> getAll() {
//			List<Guest> getGuest=repository.findAll();
//			  System.out.println("List:"+getGuest); 
//	List<DueGuestsList> dg = new ArrayList();
//	
//			getGuest.forEach(g->{
//			
//				if(g.getOccupancyType()=="Regular")
//				{
//					DueGuestsList pr=new DueGuestsList(); 
//
//					
//	
//					double dueAmount=calculateDueAmount(g.getId());
//					System.out.println(dueAmount);
//					if((dueAmount)>0)
//							{
//						pr.setDueAmount(dueAmount);
//						pr.setEmail(g.getEmail());
//						pr.setGuestId(g.getId());
//						pr.setGuestName(g.getFirstName()+g.getLastName());
//						pr.setPhoneNumber(g.getPersonalNumber());
//						pr.setBedId(g.getBedId());
////			            String name=template.getForObject("http://bedService/bed/getBuildingNameByBuildingId/"+ g.getBuildingId(), String.class);
////
////						pr.setBuildingName(name);
//						
//						//PaymentRemainder parRes = template.postForObject(url, pr, PaymentRemainder.class);
//			 			dg.add(pr);
//
//					  System.out.println(dg); 
//							}
//
//				}
//			});
//			
//			return dg ;
//	
//	
//	
//	}
//
//	public double calculateDueAmount(String id) {
//		String url = "http://paymentService/payment/getCountOfPaymentAmount/";
//		double dueAmount = 0;
//		PaymentRemainderData data = template.getForObject(url + id, PaymentRemainderData.class);
//		double amountPaidCount = data.getTotalAmountPaid();
//		double refundAmountCount = data.getTotalRefundAmount();
//
//		// getGuest detailes by guestid
//		Guest getGuest = repository.findById(id);
//		if (getGuest.getGuestStatus().equalsIgnoreCase("Active")) {
//			// get current date
//			LocalDate now = LocalDate.now();
//			System.out.println("current" + now);
//
//			// convert checkin date type to util date to compare dates
//			java.util.Date s = getGuest.getCheckInDate();
//			System.out.println("date" + s);
//
//			LocalDate local = s.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
//			// compare 2 dates
////				Period p=Period.between(now, local);
////				System.out.println("period"+p);
////				int diff=p.getDays();
////				System.out.println("diff"+diff);
//
//			double c = (int) ChronoUnit.DAYS.between(local, now) + 1;
//			System.out.println("c" + c);
//
//			if (c > 30) {
//				double calcDays = Math.ceil(c / 30);
//				System.out.println("calcDays" + calcDays);
//				int round_up = (int) calcDays;
//
//				System.out.println("round_up  😁😁" + round_up);
//
//				double countdueAmount = ((round_up * getGuest.getDefaultRent() + getGuest.getSecurityDeposit()))
//						- amountPaidCount + refundAmountCount;
//				double totalAmount = Math.ceil(countdueAmount);
//				dueAmount = totalAmount;
//				return dueAmount;
//			} else {
//				return dueAmount;
//			}
//		} else if (getGuest.getGuestStatus().equalsIgnoreCase("inNotice")) {
//
//			java.util.Date s = getGuest.getCheckInDate();
//			java.util.Date m = getGuest.getPlannedCheckOutDate();
//			System.out.println("date" + s);
//			System.out.println("m" + m);
//
//			LocalDate checkIn = s.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//			System.out.println("checkIn" + checkIn);
//
//			LocalDate plannedCheckOut = m.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//			System.out.println("plannedCheckOut" + plannedCheckOut);
//
//			double diff = (int) ChronoUnit.DAYS.between(checkIn, plannedCheckOut);
//			System.out.println("diff" + diff);
//			double perDayCharge = (getGuest.getDefaultRent() / 30);
//			System.out.println("perDayCharge" + perDayCharge);
//			// int round_up = (int) calcDays ;
//
//			// System.out.println("round_up 😁😁"+round_up);
//
//			double countdueAmount = ((diff * perDayCharge)) - amountPaidCount + refundAmountCount;
//			double totalAmount = Math.round(countdueAmount);
//			dueAmount = totalAmount;
//			System.out.println("dueAmount" + dueAmount);
//
//			return dueAmount;
//		}
//		return dueAmount;
//
//	}
//
//}
