package com.arshaa.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.arshaa.cronService.CronServices;
import com.arshaa.dtos.CheckOutConfirmation;
import com.arshaa.entity.CronNotification;
import com.arshaa.entity.Defaults;
import com.arshaa.entity.Guest;
import com.arshaa.entity.RatesConfig;
import com.arshaa.exception.ResourceNotFoundException;
import com.arshaa.model.DueResponse;
import com.arshaa.model.EmailResponse;
import com.arshaa.model.MonthlyAndDailyPlanExtension;
import com.arshaa.model.PaymentRemainder;
import com.arshaa.repository.GuestProfileRepo;
import com.arshaa.repository.GuestRepository;
import com.arshaa.repository.PayRepos;
import com.arshaa.repository.RatesConfigRepository;
import com.arshaa.repository.SecurityDepositRepo;
import com.arshaa.util.DateFormates;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DueCalculateService {
	@Autowired
	@Lazy
	private RestTemplate template;
	@Autowired(required = true)
	private GuestRepository repository;
	@Autowired
	private SecurityDepositRepo sceurityR;
	@Autowired
	private GuestProfileService profileService;
	@Autowired
	private GuestProfileRepo profilerepository;
	@Autowired
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private RatesConfigRepository rcr;
	@Autowired
	private CronServices cronServices;
	@Autowired
	private PaymentService payServ;
	@Autowired
	private PayRepos payRepo;
	String maila = "http://emailService/mail/guestCheckOutNotification/";
	private static final String guestCheckOutURL = "http://emailService/mail/guestCheckOutNotification/";
	private static final String cronJobSuccessEmailURL = "http://emailService/mail/sendCronjobSuccessNotification/";
	private static final String cronJobFailEmailURL = "http://emailService/mail/cronjobFailureNotify/";
//	@Autowired
//    private CronJobDueCalculation cron; 

	public double dueCalculationForActiveRegular(String guestId) {
		// step1
		// Take guest details by guestId
		Guest guestDetails = repository.findById(guestId)
				.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
		// step2
		// required parameters
		double previousDue = guestDetails.getDueAmount();
		Date lastBillGenerationDate = guestDetails.getLastBillGenerationDate();
		// converting lastbillgenerationdate to loacal date
		LocalDate convertedLBGD = lastBillGenerationDate.toLocalDate();
		double defaultRent = guestDetails.getDefaultRent();
		double dueAmount;
		double securityDeposit;
		double amountPaid;
		double totalAmountPaidAtOnboard = 0.0;
		double preOverAllDueAmount = guestDetails.getOverAllDue();
		double overAllDueAmount = 0.0;
		Date generatedLastBillGenerationDate;
		Date generateBillGeneratedTillDate;
		LocalDate currentDate;
		// taking current date
		currentDate = LocalDate.now();
		// this is for testing purpose
		// currentDate = LocalDate.of(2023, 07, 23);
		int differeneceDays = (int) ChronoUnit.DAYS.between(convertedLBGD, currentDate);
		// step3 -- if lastbill generation date and checkindate same we need to detuct
		// amountpaid and security deposit
		if ((guestDetails.getLastBillGenerationDate()).compareTo(guestDetails.getCheckInDate()) == 0) {
			securityDeposit = guestDetails.getSecurityDeposit();
			amountPaid = guestDetails.getAmountPaid();
			totalAmountPaidAtOnboard = securityDeposit + amountPaid;
		} else {
			securityDeposit = 0;
			amountPaid = 0;
			totalAmountPaidAtOnboard = securityDeposit + amountPaid;
		}
		// step4---check guest is active or not
		// step5---get days from current date and last bill generation date

		if (guestDetails.getGuestStatus().equalsIgnoreCase("Active")) {
			double calculateDue = 0;
			if (differeneceDays == 30) {
				calculateDue = (defaultRent + previousDue);
				dueAmount = calculateDue;
				overAllDueAmount = preOverAllDueAmount + defaultRent;
				// Generate LastbillgenerationDate
				// Local Date to Sql Date .
				generatedLastBillGenerationDate = Date.valueOf(currentDate);
				// Generate BillGeneratedTillDate
				Calendar cal = Calendar.getInstance();
				cal.setTime(generatedLastBillGenerationDate);
				cal.add(Calendar.DATE, 30);
				java.util.Date billGeneratedTill = cal.getTime();
				// Converting Java Util to Java Sql Date .
				generateBillGeneratedTillDate = new java.sql.Date(billGeneratedTill.getTime());
			}
			// let it be ther only .
			else if (differeneceDays == 31) {
				calculateDue = (defaultRent + previousDue);
				dueAmount = calculateDue;
				overAllDueAmount = preOverAllDueAmount + defaultRent;
				// Generate LastbillgenerationDate
				// Local Date to Sql Date .
				generatedLastBillGenerationDate = Date.valueOf(currentDate.minusDays(1));

				// Generate BillGeneratedTillDate
				Calendar cal = Calendar.getInstance();
				cal.setTime(generatedLastBillGenerationDate);
				cal.add(Calendar.DATE, 30);
				java.util.Date billGeneratedTill = cal.getTime();
				// Converting Java Util to Java Sql Date .

				generateBillGeneratedTillDate = new java.sql.Date(billGeneratedTill.getTime());
			}

			else {
				calculateDue = previousDue;
				dueAmount = calculateDue;
				generatedLastBillGenerationDate = lastBillGenerationDate;
				overAllDueAmount = preOverAllDueAmount;
				// Generate BillGeneratedTillDate

				Calendar cal = Calendar.getInstance();
				cal.setTime(generatedLastBillGenerationDate);
				cal.add(Calendar.DATE, 30);
				java.util.Date billGeneratedTill = cal.getTime();
//7655,7654
				// Converting Java Util to Java Sql Date .

				generateBillGeneratedTillDate = new java.sql.Date(billGeneratedTill.getTime());
			}

			guestDetails.setDueAmount(dueAmount);
			guestDetails.setLastBillGenerationDate(generatedLastBillGenerationDate);
			guestDetails.setBillGeneratedTill(generateBillGeneratedTillDate);
			guestDetails.setOverAllDue(overAllDueAmount);
			repository.save(guestDetails);
			return dueAmount;
		}
		return previousDue;
	}

	public double calculateDueGuest(String guestId) {
		// getGuest detailes by guestid
		Guest getGuest = repository.findById(guestId)
				.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
		// take amount paid or refund from the LBGD
		// String url="http://paymentService/payment/getCountOfPaymentAmount/";
		// Global Variables
		double previousDue = getGuest.getDueAmount();
		double dueAmount;
		// PaymentRemainderData
		// data=template.getForObject(url+getGuest.getLastBillGenerationDate()+"/"+guestId,PaymentRemainderData.class);
		// double amountPaidCount=data.getTotalAmountPaid();
		// double refundAmountCount=data.getTotalRefundAmount();
		double securityDeposit;
		double amountPaid;
		double guestDue;
		double refundAmount;
		double amountTobePaid;
		double preDueCheck;
		if ((getGuest.getLastBillGenerationDate()).compareTo(getGuest.getCheckInDate()) == 0) {
			securityDeposit = getGuest.getSecurityDeposit();
			amountPaid = getGuest.getAmountPaid();
			amountTobePaid = getGuest.getDefaultRent() + securityDeposit;
		} else {
			securityDeposit = 0;
			amountPaid = 0;
		}
		if (getGuest.getGuestStatus().equalsIgnoreCase("Active")) {
			/*
			 * ======= Normal Due Calculation =============== step1: take last bill
			 * generation date and securityDeposit at oneTime step2: take current date
			 * step3: get difference of those two dates step4: if(diff==30) take multiple of
			 * 30 as dueDays step5: calculateRent=dueDays* defaultRent | NOTE: update
			 * defaultRent whenever new Rent Updated By uniq rentId |
			 * step6:countdueAmount=(calculateRent+securityDeposit)-amountPaidCount+
			 * refundAmountCount | NOTE: take amountPaidSum and refundAmountSum from (LBGD
			 * TO NOW) | step7:ceil it and assign it to dueAmount step8:update dueAmount
			 * column, generate LBGD and update it in guest table and payments table
			 */

			Date lastBillGenerationDate = getGuest.getLastBillGenerationDate();
			// Sql Date to Local date .
			// LocalDate local = lastBillGenerationDate.toInstant()
			// .atZone(ZoneId.systemDefault())
			// .toLocalDate();
			// System.out.println("Local"+local);
			// Commented on 28 july
			LocalDate currentDate = LocalDate.now();
			// Dont Examine my patience here please 👏🤲🤝.
			// LocalDate currentDate =LocalDate.of(2022, 12, 17);
			// Please god make it working properly .
			// Local Date to Sql Date .
			Date date = Date.valueOf(currentDate); // Magic happens here!
			// converting sql date to local date for calculation
			LocalDate convertedLBGD = lastBillGenerationDate.toLocalDate();

			double differeneceDays = (int) ChronoUnit.DAYS.between(convertedLBGD, currentDate);
			// Have patience

			if (differeneceDays >= 30) {

				// double multipleOf30Days = Math.floor((double) differeneceDays / 30);
				double multipleOf30Days = Math.ceil((int) differeneceDays / 30);
				double multipleOf30Days1 = Math.ceil(differeneceDays / 30);
				log.info("multipleOf30Days1 {}", multipleOf30Days1);
				log.info("multipleOf30Days{}", multipleOf30Days);

				int multipleOfMonthToDays = (int) multipleOf30Days * 30;
				log.info("multipleOfMonthToDays {}", multipleOfMonthToDays);
				int dueDays = (int) multipleOf30Days1;

				// Generating last bill generation date and bill generated till according to the
				// cycles
				java.util.Date lastBillGen = lastBillGenerationDate;
				Calendar cal = Calendar.getInstance();
				cal.setTime(lastBillGen);
				cal.add(Calendar.DATE, multipleOfMonthToDays);
				lastBillGen = cal.getTime();
				log.info("Last Bill Generated {}", lastBillGen);
				// Converting Java Util to Java Sql Date .
				java.sql.Date convertedlastBillGen = new java.sql.Date(lastBillGen.getTime());
				java.util.Date billGeneratedTill = lastBillGenerationDate;
				Calendar cal2 = Calendar.getInstance();
				cal.setTime(billGeneratedTill);
				cal.add(Calendar.DATE, multipleOfMonthToDays + 30);
				billGeneratedTill = cal.getTime();
				// Converting Java Util to Java Sql Date .
				java.sql.Date convertedbillGeneratedTill = new java.sql.Date(billGeneratedTill.getTime());
				double calculateRent = multipleOf30Days1 * getGuest.getDefaultRent();

				double countdueAmount = calculateRent + securityDeposit - amountPaid;

				double totalAmount = Math.ceil(countdueAmount);

				dueAmount = previousDue + totalAmount;

				getGuest.setDueAmount(dueAmount);
				getGuest.setLastBillGenerationDate(convertedlastBillGen);
				getGuest.setBillGeneratedTill(convertedbillGeneratedTill);
				repository.save(getGuest);
				return dueAmount;
			} else {
				return previousDue;
			}
		}
		return previousDue;
	}

	public DueResponse updateDueAmount(double amountPaid, double refundAmount, String guestId) {
		DueResponse dRes = new DueResponse();
		try {
			Guest getGuest = repository.findById(guestId)
					.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
			double previousDue = getGuest.getDueAmount();
			double updateDue = previousDue - amountPaid + refundAmount;
			if (getGuest.getCheckInDate().equals(getGuest.getLastBillGenerationDate())) {
				dRes.setStatus(false);
				dRes.setMessage("Not needed");
				return dRes;
			} else {
				getGuest.setDueAmount(updateDue);
				repository.save(getGuest);
				dRes.setStatus(true);
				dRes.setMessage("Due updated Successfully");
				return dRes;
			}
		} catch (Exception e) {
			dRes.setStatus(false);
			dRes.setMessage("Something went wrong");
			return dRes;
		}
	}

	public ResponseEntity calculateDueForInNotice(String id) {

		Guest guest = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
		Defaults def = sceurityR.findByOccupancyType(guest.getOccupancyType());
		if (guest.getOccupancyType().equalsIgnoreCase("Regular") && guest.getGuestStatus().equalsIgnoreCase("active")) {
			double dueAmount = 0;
			double refundAmount = 0;
			java.util.Date plannedCheckedDate = guest.getPlannedCheckOutDate();
			java.util.Date billGeneratedTillDate = guest.getBillGeneratedTill();
			java.sql.Date sqlBillGeneratedTill = new java.sql.Date(billGeneratedTillDate.getTime());
			// Commented on 28 july
			LocalDate datePlanned = plannedCheckedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate currentDate = LocalDate.now();
			// Dont Examine my patience here please .
			java.sql.Date sqlPlannedCheckOut = new java.sql.Date(plannedCheckedDate.getTime());
			// LocalDate currentDate =LocalDate.of(2022, 12, 14); //Local Date to Sql Date .
			Date date = Date.valueOf(currentDate);
			// Magic happens here!
			// converting sql date to local date for calculation
			// LocalDate convertedPaidTill=paidTillDate.toLocalDate();
			LocalDate convertedBillGeneratedTillDate = sqlBillGeneratedTill.toLocalDate();
			double differeneceDays;
			double differenece = (int) ChronoUnit.DAYS.between(convertedBillGeneratedTillDate, datePlanned);
			if (differenece < 0) {
				differeneceDays = -1 * differenece;
			} else {
				differeneceDays = differenece;
			}

			if (convertedBillGeneratedTillDate.isBefore(datePlanned)) {

				// Guest should pay or Refund will see later
				double perDayCharge = (guest.getDefaultRent() / 30);
				log.info("perDayCharge {}", perDayCharge);

				double thenDues = ((differeneceDays * perDayCharge));
				log.info("thenDues {}", thenDues);
				double dueCalculation = (thenDues + guest.getDueAmount()) - guest.getSecurityDeposit()
						+ def.getMaintainanceCharge();
				if (dueCalculation < 0) {
					refundAmount = -1 * dueCalculation;
					dueAmount = -refundAmount;
				} else {
					dueAmount = dueCalculation;
				}

			} else if (convertedBillGeneratedTillDate.isAfter(datePlanned)) {
				double perDayCharge = (guest.getDefaultRent() / 30);
				// Definitely Refund
				double thoDues = ((differeneceDays * perDayCharge));

				double refundCalculation = (thoDues - def.getMaintainanceCharge() + guest.getSecurityDeposit())
						- guest.getDueAmount();

				if (refundCalculation < 0) {
					dueAmount = -1 * refundCalculation;
				} else {
					refundAmount = refundCalculation;
					dueAmount = -refundAmount;
				}
			} else if (convertedBillGeneratedTillDate.isEqual(datePlanned)) {
				// Definietly Refund
				double refundCalculation = guest.getSecurityDeposit() - guest.getDueAmount()
						- def.getMaintainanceCharge();
				if (refundCalculation < 0) {
					dueAmount = -1 * refundCalculation;
				} else {
					refundAmount = refundCalculation;
					dueAmount = -refundAmount;
				}

			}
			guest.setDueAmount(dueAmount);
			guest.setInNoticeDue(dueAmount);
			guest.setGuestStatus("InNotice");
			repository.save(guest);
		}
		return null;
	}

	public ResponseEntity updatePackageIdInGuest() {
		String bURL = "http://bedService/bed/getStatusByGuestId/";
		try {
			List<Guest> getAll = repository.findAll();
			if (!getAll.isEmpty()) {
				getAll.forEach(g -> {
					log.info("id {}", g.getId() + g.getGuestStatus());
					if (g.getGuestStatus().equalsIgnoreCase("active")) {
						Guest guest = repository.findById(g.getId())
								.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
						String bedStatus = template.getForObject(bURL + g.getId(), String.class);
						Integer packageId = rcr.findByBuildingIdAndOccupancyTypeAndPriceAndRoomType(g.getBuildingId(),
								g.getOccupancyType(), g.getDefaultRent(), bedStatus).getId();
						guest.setPackageId(packageId);
						repository.save(guest);
					}
				});
				return new ResponseEntity("Success", HttpStatus.OK);
			} else {
				return new ResponseEntity("DATA NOT FOUND", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.OK);
		}
	}

	// Plan extension for daily and monthly
	public ResponseEntity dueForPlanExtensionForDandR(MonthlyAndDailyPlanExtension mAndDplan) {
		try {
			Guest g = repository.findById(mAndDplan.getGuestId())
					.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
			double dueAmount = 0;
			LocalDate localPlannedDate = mAndDplan.getPlannedCheckOutDate().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			LocalDate datePlanned = g.getPlannedCheckOutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			double differenece = (int) ChronoUnit.DAYS.between(datePlanned, localPlannedDate);
			log.info("differenece {}", differenece);

			if (g.getOccupancyType().equalsIgnoreCase("daily")) {
				double perDayCharge = g.getDefaultRent();
				dueAmount = ((differenece * perDayCharge));
				g.setDueAmount(dueAmount);
				repository.save(g);
				return new ResponseEntity(dueAmount, HttpStatus.OK);
			} else if (g.getOccupancyType().equalsIgnoreCase("OneMonth")) {
				if (differenece == 31.0 || differenece == 28.0) {
					differenece = 30.0;
					log.info("differenece {}", differenece);
				}
				double perDayCharge = (g.getDefaultRent() / 30);
				log.info("perDayCharge {}", perDayCharge);
				dueAmount = (Math.round((differenece * perDayCharge)));

				g.setDueAmount(dueAmount);
				repository.save(g);
				return new ResponseEntity(dueAmount, HttpStatus.OK);
			} else {
				return new ResponseEntity(dueAmount, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity("Something went wrong", HttpStatus.OK);
		}
	}

	public ResponseEntity planExtensionForDandR(MonthlyAndDailyPlanExtension mAndDplan) {
		try {
			Guest g = repository.findById(mAndDplan.getGuestId())
					.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
			if (g.getOccupancyType().equalsIgnoreCase("daily") || g.getOccupancyType().equalsIgnoreCase("OneMonth")
					&& g.getGuestStatus().equalsIgnoreCase("active")) {
				g.setPreviousPlannedCheckOutDate(g.getPlannedCheckOutDate());
				g.setPlannedCheckOutDate(mAndDplan.getPlannedCheckOutDate());
				g.setDueAmount(0);
				repository.save(g);
				return new ResponseEntity("Plan Extension Successfull", HttpStatus.OK);
			} else {
				return new ResponseEntity("not a valid guest for plan extension", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity("Plan Extension Failed", HttpStatus.OK);
		}
	}

	public double calculateDueForRegularPlandBedChange(String guestId) {
		Guest guest = repository.findById(guestId)
				.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
		if (guest.getOccupancyType().equalsIgnoreCase("Regular") && guest.getGuestStatus().equalsIgnoreCase("active")) {
			double dueAmount = 0;
			double refundAmount = 0;

			java.util.Date billGeneratedTillDate = guest.getBillGeneratedTill();
			log.info("billGeneratedTillDate {}", billGeneratedTillDate);
			java.sql.Date sqlBillGeneratedTill = new java.sql.Date(billGeneratedTillDate.getTime());
			LocalDate currentDate = LocalDate.now();
			Date date = Date.valueOf(currentDate);
			log.info("sqlBillGeneratedTill {}", sqlBillGeneratedTill);
			LocalDate convertedBillGeneratedTillDate = sqlBillGeneratedTill.toLocalDate();
			log.info("convertedBillGeneratedTillDate{}", convertedBillGeneratedTillDate);
			double differeneceDays;
			double differenece = (int) ChronoUnit.DAYS.between(convertedBillGeneratedTillDate, currentDate);
			log.info("difference {}", differenece);
			if (differenece < 0) {
				differeneceDays = -1 * differenece;
				log.info("differeneceDays{}", differeneceDays);
			} else {
				differeneceDays = differenece;
				log.info("differeneceDays {}", differeneceDays);
			}
			if (convertedBillGeneratedTillDate.isBefore(currentDate)) {
				// Guest should pay or Refund will see later
				double perDayCharge = (guest.getDefaultRent() / 30);
				log.info("perDayCharge {}", perDayCharge);
				double thenDues = ((differeneceDays * perDayCharge));
				log.info("thenDues {}", thenDues);
				double dueCalculation = (thenDues + guest.getDueAmount());
				if (dueCalculation < 0) {
					refundAmount = -1 * dueCalculation;
					dueAmount = -refundAmount;
					log.info("dueRefund {}", dueAmount);
				} else {
					dueAmount = dueCalculation;
					log.info("actualDue {}", dueAmount);
				}
				return dueAmount;
			} else if (convertedBillGeneratedTillDate.isAfter(currentDate)) {
				double perDayCharge = (guest.getDefaultRent() / 30);
				// Definitely Refund
				double thoDues = ((differeneceDays * perDayCharge));
				log.info("due base on per day charge: {}", thoDues);
				log.info("dueAmounr {}", guest.getDueAmount());
				double refundCalculation = thoDues - guest.getDueAmount();
				log.info("refund amount {}", refundCalculation);
				if (refundCalculation < 0) {
					dueAmount = -1 * refundCalculation;
				} else {
					refundAmount = refundCalculation;
					dueAmount = -refundAmount;
				}
				return dueAmount;
			} else if (convertedBillGeneratedTillDate.isEqual(currentDate)) {
				// Definietly Refund
				double refundCalculation = guest.getDueAmount();
				dueAmount = refundCalculation;
				log.info("refund defenately amount {}", dueAmount);
				return dueAmount;
			} else {
				return guest.getDueAmount();
			}
		} else {
			return guest.getDueAmount();
		}
	}

	public double calculateDueForMonthlyAndDailyPlandBedChange(String guestId) {
		Guest guest = repository.findById(guestId)
				.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
		if (guest.getOccupancyType().equalsIgnoreCase("OneMonth")
				&& guest.getGuestStatus().equalsIgnoreCase("active")) {
			double dueAmount = 0;
			double refundAmount = 0;
			java.util.Date PlannedCheckOutDate = guest.getPlannedCheckOutDate();
			java.sql.Date conertPlannedCheckOutDate = new java.sql.Date(PlannedCheckOutDate.getTime());
			LocalDate currentDate = LocalDate.now();
			Date date = Date.valueOf(currentDate);
			LocalDate convertedPlannedCheckoutDate = conertPlannedCheckOutDate.toLocalDate();
			double differeneceDays;
			double differenece = (int) ChronoUnit.DAYS.between(convertedPlannedCheckoutDate, currentDate);
			if (differenece < 0) {
				differeneceDays = -1 * differenece;
				log.info("inside if differeneceDays {}", differeneceDays);

			} else {
				differeneceDays = differenece;
			}
			log.info("Days {}", differeneceDays);
			if (convertedPlannedCheckoutDate.isBefore(currentDate)) {
				// Guest should pay or Refund will see later
				double perDayCharge = (guest.getDefaultRent() / 30);

				double thenDues = ((differeneceDays * perDayCharge));
				log.info("thenDues {}", thenDues);
				double dueCalculation = (thenDues + guest.getDueAmount());
				log.info("dueCalculation {}", dueCalculation);
				if (dueCalculation < 0) {
					refundAmount = -1 * dueCalculation;
					log.info("refundAmount {}", refundAmount);
					dueAmount = -refundAmount;
					log.info("dueAmount {}", dueAmount);
				} else {
					dueAmount = dueCalculation;
					log.info("dueAmount {}", dueAmount);
				}
				return dueAmount;
			} else if (convertedPlannedCheckoutDate.isAfter(currentDate)) {
				double perDayCharge = (guest.getDefaultRent() / 30);
				log.info(" perDayCharge inside else if {}", perDayCharge);
				// Definitely Refund
				double thoDues = ((differeneceDays * perDayCharge));
				log.info("thoDues inside else if {}", thoDues);

				double refundCalculation = (thoDues) - guest.getDueAmount();
				log.info("refundCalculation {}", refundCalculation);
				if (refundCalculation < 0) {
					dueAmount = -1 * refundCalculation;
					log.info("dueAmount {}", dueAmount);
				} else {
					refundAmount = refundCalculation;
					log.info("refundCalculation {}", refundAmount);
					dueAmount = -refundAmount;
					log.info("dueAmount {}", dueAmount);
				}
				return dueAmount;
			} else if (convertedPlannedCheckoutDate.isEqual(currentDate)) {
				// Definietly Refund
				double refundCalculation = guest.getDueAmount();
				log.info("refundCalculation {}", refundCalculation);
				if (refundCalculation < 0) {
					dueAmount = -1 * refundCalculation;
				} else {
					refundAmount = refundCalculation;
					dueAmount = -refundAmount;
				}
				return dueAmount;
			} else {
				return guest.getDueAmount();
			}
		} else if (guest.getOccupancyType().equalsIgnoreCase("daily")
				&& guest.getGuestStatus().equalsIgnoreCase("active")) {
			double dueAmount = 0;
			double refundAmount = 0;
			java.util.Date PlannedCheckOutDate = guest.getPlannedCheckOutDate();
			java.sql.Date conertPlannedCheckOutDate = new java.sql.Date(PlannedCheckOutDate.getTime());
			LocalDate currentDate = LocalDate.now();
			Date date = Date.valueOf(currentDate);
			LocalDate convertedPlannedCheckoutDate = conertPlannedCheckOutDate.toLocalDate();
			double differeneceDays;
			double differenece = (int) ChronoUnit.DAYS.between(convertedPlannedCheckoutDate, currentDate);
			if (differenece < 0) {
				differeneceDays = -1 * differenece;
			} else {
				differeneceDays = differenece;
			}

			if (convertedPlannedCheckoutDate.isBefore(currentDate)) {
				double perDayCharge = (guest.getDefaultRent());

				double thenDues = ((differeneceDays * perDayCharge));
				log.info("thenDues {}", thenDues);
				double dueCalculation = (thenDues + guest.getDueAmount());
				if (dueCalculation < 0) {
					refundAmount = -1 * dueCalculation;
					dueAmount = -refundAmount;
				} else {
					dueAmount = dueCalculation;
				}
				return dueAmount;
			} else if (convertedPlannedCheckoutDate.isAfter(currentDate)) {
				double perDayCharge = (guest.getDefaultRent());
				double thoDues = ((differeneceDays * perDayCharge));
				log.info("difference dues {}", thoDues);
				double refundCalculation = (thoDues) - guest.getDueAmount();

				if (refundCalculation < 0) {
					dueAmount = -1 * refundCalculation;
				} else {
					refundAmount = refundCalculation;
					dueAmount = -refundAmount;
				}
				return dueAmount;
			} else if (convertedPlannedCheckoutDate.isEqual(currentDate)) {
				dueAmount = 0;
				return guest.getDueAmount();
			} else {
				return guest.getDueAmount();
			}
		} else {
			return guest.getDueAmount();
		}

	}

	// Hey I Am here to calculate due and send notification so dont worry

	public void calculateRegularGuestDue(String guestId) throws ResourceNotFoundException {
		try {
			// taking guest information
			Guest guest = repository.findById(guestId)
					.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
			// check if guest is active and regular or not
			if (guest.getOccupancyType().equalsIgnoreCase("Regular")
					&& guest.getGuestStatus().equalsIgnoreCase("active")) {
				Double previousDue = guest.getDueAmount();
				Double defaultRent = guest.getDefaultRent();
				Double dueAmountCalculation = 0.0;
				Date d = guest.getLastBillGenerationDate();
				log.info("lbgd {} ", d);
				ZonedDateTime lastBillGenerationDate = DateFormates.sqlToZoned(guest.getLastBillGenerationDate());
				log.info("lgbt converted {}", lastBillGenerationDate);
				ZonedDateTime currentDate = ZonedDateTime.now();
//				ZonedDateTime currentDate = zonedDateTime;
//				LocalDate ld = LocalDate.of(2023, 8, 27);	
//				ZonedDateTime currentDate = ld.atStartOfDay(ZoneId.systemDefault());
				Date CreatedLastbillGenerationDate = null;
				Date CreatedBillGeneratedTillDate = null;
				Long differeneceDays = 0L;
				Long lastDayOfMonth = 0L;
				// get the difference days between lastbillgenerationdate and current date
				differeneceDays = DateFormates.zonedDateTimeDifference(lastBillGenerationDate, currentDate,
						ChronoUnit.DAYS);
				lastDayOfMonth = DateFormates.lastDayOfMonth(lastBillGenerationDate);
				if (differeneceDays == lastDayOfMonth) {
					dueAmountCalculation = previousDue + defaultRent;
					CreatedLastbillGenerationDate = DateFormates.ZonedToSql(currentDate);
					CreatedBillGeneratedTillDate = DateFormates
							.ZonedToSql(currentDate.plusDays(DateFormates.lastDayOfMonth(currentDate)));
					guest.setLastBillGenerationDate(CreatedLastbillGenerationDate);
					guest.setBillGeneratedTill(CreatedBillGeneratedTillDate);
				} else {
					dueAmountCalculation = previousDue;
				}
				guest.setDueAmount(dueAmountCalculation);

				repository.save(guest);
			}
		} catch (Exception e) {
			throw new ResourceNotFoundException(false, e.getMessage());
		}
	}

	// Calling Calculate due Method here for Updating Due .Hey method immedietly
	// update due for guest .
	public ResponseEntity updateGuestDue() {
		{
			List<PaymentRemainder> getList = new ArrayList();
			List<EmailResponse> getRes = new ArrayList<>();
			List<Guest> getGuest = repository.findAll();
			log.info("List {}", getGuest);
			if (!getGuest.isEmpty()) {
				getGuest.forEach(g -> {

					if (g.getOccupancyType().equalsIgnoreCase("Regular")
							&& g.getGuestStatus().equalsIgnoreCase("active")) {
						try {
							calculateRegularGuestDue(g.getId());
							log.info("due Calculation successfully ends ");
						} catch (ResourceNotFoundException e) {
							log.info("error in due calculation{}", e.getMessage());
						}
					}
				});
				return new ResponseEntity("Success", HttpStatus.OK);
			} else {
				return new ResponseEntity("Nodue", HttpStatus.OK);
			}
		}
	}

	// Dont comment here .
	@Scheduled(cron = "0 10 10 * * *", zone = "Asia/Kolkata")
	public void addDue2() throws Exception {
		CronNotification cronNotification = new CronNotification();
		ArrayList<String> devEmails = new ArrayList<>();
		devEmails.add("faisal.azam@arshaa.com");
		try {
			cronNotification.setCronType("first cron ran");
			cronNotification.setDate(LocalDate.now());
			cronServices.saveCronNotification(cronNotification);
			updateGuestDue();
			devEmails.forEach(e -> {
				template.getForObject(this.cronJobSuccessEmailURL + e, String.class);
			});
		} catch (Exception exec) {
			cronNotification.setCronType("cron is failed; I Am sorry babu");
			cronNotification.setDate(LocalDate.now());
			cronServices.saveCronNotification(cronNotification);
			devEmails.forEach(e -> {
				template.getForObject(this.cronJobFailEmailURL + e, String.class);
			});
		}
	}

	// Dont comment here .
	@Scheduled(cron = "0 31 16 * * *", zone = "Asia/Kolkata")
	public void addDue() throws Exception {
		CronNotification cronNotification = new CronNotification();
		ArrayList<String> devEmails = new ArrayList<>();
		devEmails.add("faisalazam1999@gmail.com");
		try {
			cronNotification.setCronType("first cron ran");
			cronNotification.setDate(LocalDate.now());
			cronServices.saveCronNotification(cronNotification);
			updateGuestDue();
			devEmails.forEach(e -> {
				template.getForObject(this.cronJobSuccessEmailURL + e, String.class);
			});
		} catch (Exception exec) {
			cronNotification.setCronType("cron is failed; I Am sorry babu");
			cronNotification.setDate(LocalDate.now());
			cronServices.saveCronNotification(cronNotification);
			devEmails.forEach(e -> {
				template.getForObject(this.cronJobFailEmailURL + e, String.class);
			});
		}
	}

	// send check out alerts:
	@Scheduled(cron = "0 21 15 * * *", zone = "Asia/Kolkata")
	public ResponseEntity sendCheckOutAlerts() {
	    String url = "http://emailService/mail/sendCheckoutAlerts";
	    List<EmailResponse> getRes = new ArrayList<>();
	    List<Guest> getGuest = repository.findGuestsWithPlannedCheckoutInTwoDays();
	    System.out.println("getGuest: "+ getGuest);
	    System.out.println("List:" + getGuest);
	    if (!getGuest.isEmpty()) {
	        getGuest.forEach(g -> {
	        	String name = template.getForObject(
						"http://bedService/bed/getBuildingNameByBuildingId/" + g.getBuildingId(), String.class);
				
	            LocalDate today = LocalDate.now();
	            LocalDate twoDaysFromToday = today.plusDays(2);

	            // Null-safe check for plannedCheckOutDate
	            if (g.getPlannedCheckOutDate() != null) {
	                LocalDate plannedDate = g.getPlannedCheckOutDate()
	                        .toInstant()
	                        .atZone(ZoneId.systemDefault())
	                        .toLocalDate();

	                if (plannedDate.isEqual(twoDaysFromToday)) {
	                    CheckOutConfirmation pr = new CheckOutConfirmation();
	                    pr.setBedId(g.getBedId());
	                    pr.setBuildingName(name);
	                    pr.setEmail(g.getEmail());
	                    pr.setPlannedCheckOutDate(g.getPlannedCheckOutDate());
	                    pr.setName(g.getFirstName());

	                    EmailResponse parRes = template.postForObject(url, pr, EmailResponse.class);
	                    EmailResponse er = new EmailResponse();
	                    er.setStatus(parRes.isStatus());
	                    er.setMessage(parRes.getMessage());
	                    getRes.add(er);
	                }
	            } else {
	                log.warn("Guest {} has no planned checkout date, skipping date-based check", g.getFirstName());
	            }
	        });

	        return new ResponseEntity<>(getRes, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("Not Alert Sent", HttpStatus.OK);
	    }
	}


	
	

}
