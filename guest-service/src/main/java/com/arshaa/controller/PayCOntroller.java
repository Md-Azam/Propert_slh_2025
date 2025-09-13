package com.arshaa.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.arshaa.common.Payment;
import com.arshaa.entity.Payments;
import com.arshaa.model.ApisResponse;
import com.arshaa.repository.GuestRepository;
import com.arshaa.repository.PayRepos;
import com.arshaa.service.DueCalculateService;
import com.arshaa.service.PaymentService;
import com.payment.common.AppConstants;
import com.payment.common.PaymentApiDetails;
import com.payment.common.PaymentHistory;
import com.payment.common.PostPayments;
import com.payment.common.RecentTransactions;
import com.payment.common.THistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin("*")
@RequestMapping("/payment")
public class PayCOntroller {

	@Autowired
	private PaymentService serve;

	@Autowired
	@Lazy
	private RestTemplate template;

	@Autowired
	private PayRepos repos;

	@Autowired
	private GuestRepository guestRepo;
	@Autowired
	private DueCalculateService dueCalcServ;
	
	@PostMapping("/getrecordPaymenAndSendPDF")
	public ResponseEntity<byte[]> generateReceiptForGuest(@RequestBody PostPayments p){
		return this.serve.recordPaymenAndSendPDF(p);
	}

	// http://localhost:8989/payment/addPaymentAtOnBoarding
	// ADDING PAYMENT AT ONBOARDING TIME .
	@PostMapping("/addPaymentAtOnBoarding")
	public Payments addPayment(@RequestBody Payments payment) {
		return this.serve.addPayment(payment);
	}

	@GetMapping("/todayPayment/{buildingId}")
	public ResponseEntity<ApisResponse> getTodayPayments(@PathVariable Integer buildingId) {
		ApisResponse recentList = serve.getTodaysPayments(buildingId);
		return new ResponseEntity<ApisResponse>(recentList, HttpStatus.OK);
	}

	// Get todays payments by buildingid .
	@GetMapping("/todaysPayments/{buildingId}")
	public ResponseEntity<List<THistory>> getTodaysPayments(@RequestParam Date transactionDate,
			@PathVariable Integer buildingId) {
		List<THistory> recentList = serve.getByTransactionDateAndBuildingId(transactionDate, buildingId);
		return new ResponseEntity<List<THistory>>(recentList, HttpStatus.OK);
	}

	

	// http://localhost:8989/payment/getPaymentDetail/{paymentId}
	// RETRIEVE PAYMENT DETAILS BASED ON PAYMENT ID .
	@GetMapping("/getPaymentDetail/{paymentId}")
	public Payments getPaymentById(@PathVariable int paymentId) {
		return this.serve.getPaymentById(paymentId);
	}

	// GET THE TRANSACTION HISTORY BASED ON GUESTID .
	@GetMapping("/getPaymentByGuestId/{guestId}")
	public Payments findByGuestId(@PathVariable String guestId) throws Exception {
		return this.serve.getPaymentByGuestId(guestId);
	}

	// POSTING INFORMATION OF PAYMENT BASED ON GUEST TYPE .
	@PostMapping("/addAfterOnBoard")
	public String addPaymentAfterOnBoar(@RequestBody PostPayments payment) {
		return this.serve.addPaymentAfterOnBoard(payment);
	}

	@GetMapping("/getTrasactionHistoryByGuestId/{guestId}")
	public List<Payments> findTransactionsByGuestId(@PathVariable String guestId) {
		return repos.findAllPaymentsByGuestId(guestId);
	}

	@GetMapping("/recentTransactions/{buildingId}")
	public List<RecentTransactions> getRecentPays(@PathVariable Integer buildingId) {
		try {
			List<RecentTransactions> l = serve.getRecent(buildingId);
			return l;
		} catch (Exception e) {
			throw new ResourceAccessException("no data found");
		}

	}
	// API FOR GET RECENT TRANSACTION BASED ON BUILDING ID ;

	@GetMapping("/getBuildingwisePaymentSummary/{buildingId}")
	public List<PaymentHistory> getPaymentsByBuildingId(@PathVariable int buildingId) {
		List<PaymentHistory> hist = new ArrayList<>();
		Optional<List<Payments>> pay = repos.findPaymentsByBuildingId(buildingId);
		// pay.setBuildingId(buildingId);
		if (pay.isPresent()) {
			pay.get().forEach(pays -> {
				PaymentHistory ph = new PaymentHistory();
				ph.setId(pays.getId());
				ph.setAmountPaid(pays.getAmountPaid());
				ph.setGuestId(pays.getGuestId());
				ph.setTransactionId(pays.getTransactionId());
				ph.setBuildingId(pays.getBuildingId());
				hist.add(ph);
			});

		}

		return hist;
	}
	
	
	

	@GetMapping("/getTodaysIncome/{id}")
	public ResponseEntity getTodaysIncome(@PathVariable("id") int buildingId) {
		Double income = serve.getTodaysIncome(buildingId);
		if (income != null) {
			return new ResponseEntity(income, HttpStatus.OK);
		} else {
			return new ResponseEntity(0.0, HttpStatus.OK);
		}

	}

	@GetMapping("/getyearlyIncome/{buildingId}/{year}")
	public ResponseEntity getYearlyIncome(@PathVariable int buildingId, @PathVariable int year) {
		Double income = repos.getYearlyIncome(buildingId, year);
		System.out.println(income);

		if (income == null) {
			double td = 0.0;
			return new ResponseEntity(td, HttpStatus.OK);
		} else {
			BigDecimal bigDecimal = new BigDecimal(income);// form to BigDecimal
			String str = bigDecimal.toString();
			return new ResponseEntity(str, HttpStatus.OK);
		}
	}

	@GetMapping("/getCountOfPaymentAmount/{guestId}")
	public ResponseEntity getCountOfPaymentAmount(@PathVariable String guestId) {
		return new ResponseEntity(serve.getCountOfPaymentAmount(guestId), HttpStatus.OK);
	}

	@GetMapping("/getMonthlySummary/{month}/{year}/{buildingId}")
	public ResponseEntity getMonthlySummary(@PathVariable int month, @PathVariable int year,
			@PathVariable int buildingId) {
		return serve.getMonthlySummary(month, year, buildingId);
	}

	// localhost:8095/api/getAllPost?pageNumber=1&pageSize=3&sortBy=postId&sortDir=desc
	@GetMapping("/getTotalPaid/{guestId}")
	public long getTotalPaid(@PathVariable String guestId) {
		long p = repos.getCountOfAmount(guestId);
		return p;

	}

	@GetMapping("/posts")
	public ResponseEntity<PaymentApiDetails> getAllPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

		PaymentApiDetails postResponse = this.serve.getAllPays(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PaymentApiDetails>(postResponse, HttpStatus.OK);
	}

	// http://localhost:8086/payment/byDate?startDate=2022-09-02&endDate=2022-09-08
	@GetMapping("/byDate/{buildingId}")
	public List<THistory> getByDates(@PathVariable Integer buildingId, @RequestParam Date startDate,
			@RequestParam Date endDate) {

		List<THistory> pay = serve.getByBuildingIdAndByTransactionDateBetween(buildingId, startDate, endDate);
		return pay;
	}

	@GetMapping("/getMonthlyCountByBuildingId/{buildingId}")
	public Long getMonthlyCountByBuildingId(@PathVariable Integer buildingId) {
		Long p = serve.getSumofMonthlyIncomeByBuildingId(buildingId);
		return p;
	}
}