package com.arshaa.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

import com.arshaa.common.PaymentRemainderData;
import com.arshaa.entity.Guest;
import com.arshaa.exception.ResourceNotFoundException;
import com.arshaa.repository.GuestRepository;
import com.arshaa.repository.RatesConfigRepository;

public class GuestdueCalculation {

	
	@Autowired(required = true)
	private GuestRepository repository;
	
	@Autowired
	private RatesConfigRepository rconfig;

	@Autowired
	@Lazy
	private RestTemplate template;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@PersistenceContext
	private EntityManager em;
	
	
	public double calculateDueAmounts(String id)
	{
		String url="http://paymentService/payment/getCountOfPaymentAmount/";
	    double dueAmount=0;			
		PaymentRemainderData data=template.getForObject(url+id,PaymentRemainderData.class);
		double amountPaidCount=data.getTotalAmountPaid();
		double refundAmountCount=data.getTotalRefundAmount();
		
		//getGuest detailes by guestid
		Guest getGuest = repository.findById(id).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
		if(getGuest.getGuestStatus().equalsIgnoreCase("Active"))
		{
			//get current date
			LocalDate now=LocalDate.now();
			System.out.println("current"+now);

			//convert checkin date type to util date to compare dates=>converted sql date to local date 
			Date  s=getGuest.getCheckInDate();
			LocalDate localDate1 = s.toLocalDate();
			System.out.println("date"+s);

			
			double  c=(int) ChronoUnit.DAYS.between(localDate1, now)+1;
			System.out.println("c"+c);

			if(c>30)
			{
				 double calcDays=Math.ceil(c/30);
					System.out.println("calcDays"+calcDays);
				 int round_up =  (int) calcDays ;				 
					System.out.println("round_up  😁😁"+round_up);

				 double countdueAmount=((round_up*getGuest.getDefaultRent()+getGuest.getSecurityDeposit()))-amountPaidCount+refundAmountCount;	
				 double totalAmount =  Math.ceil(countdueAmount);
				 dueAmount=totalAmount;
				 return dueAmount;
			}
			else {
				return dueAmount;
			}
		}
		else if(getGuest.getGuestStatus().equalsIgnoreCase("inNotice")){
			
			java.util.Date s=getGuest.getCheckInDate();
			java.util.Date m=getGuest.getPlannedCheckOutDate();
			//m  = date.toInstant();
			System.out.println("date"+s);
			System.out.println("m"+m);

//			System.out.println("checkIn"+checkIn);
			LocalDate now=LocalDate.now();
			Date  checkIn=getGuest.getCheckInDate();
			LocalDate localDate1 = checkIn.toLocalDate();

			LocalDate plannedCheckOut = m.toInstant()
	                  .atZone(ZoneId.systemDefault())
	                  .toLocalDate();
			System.out.println("plannedCheckOut"+plannedCheckOut);

			double  diff=(int) ChronoUnit.DAYS.between(localDate1, plannedCheckOut);
			System.out.println("diff"+diff);		
				 double perDayCharge=(getGuest.getDefaultRent()/30);
					System.out.println("perDayCharge"+perDayCharge);
				 //int round_up =  (int) calcDays ;
				 
					//System.out.println("round_up  😁😁"+round_up);

				 double countdueAmount=((diff*perDayCharge))-amountPaidCount+refundAmountCount;	
				 double totalAmount =  Math.round(countdueAmount);
				 dueAmount=totalAmount;
					System.out.println("dueAmount"+dueAmount);

				 return dueAmount;
			}
		return dueAmount;

		}

}
