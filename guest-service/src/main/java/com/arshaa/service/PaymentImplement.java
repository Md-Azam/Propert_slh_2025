package com.arshaa.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import com.arshaa.entity.Guest;
import com.arshaa.entity.Payments;
import com.arshaa.exception.ResourceNotFoundException;
import com.arshaa.model.ApisResponse;
import com.arshaa.repository.GuestRepository;
import com.arshaa.repository.PayRepos;
import com.google.common.net.HttpHeaders;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.payment.common.PaymentApiDetails;
import com.payment.common.PaymentConfirmation;
import com.payment.common.PaymentRemainderData;
import com.payment.common.PostPayments;
import com.payment.common.RecentTransactions;
import com.payment.common.Response;
import com.payment.common.THistory;
import com.payment.model.DueResponse;
import com.payment.model.EmailResponse;
import com.payment.model.EmailTempModel;
import com.payment.model.MonthlySummary;

import com.arshaa.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class PaymentImplement implements PaymentService {

	@Autowired
	private PayRepos repo;
	@Autowired
	@Lazy
	private RestTemplate template;

	@Autowired
	private GuestRepository guestRepo;
	@Autowired
	private DueCalculateService dueCalcServ;

	Logger log = LoggerFactory.getLogger(PaymentImplement.class);

	// 2.METHOD TO FETCH PAYMENT DETAILS OF ONE PARTICULAR GUEST BY PAYMENTID .
	@Override
	public Payments getPaymentById(int paymentId) {
		// TODO Auto-generated method stub

		return repo.findById(paymentId).orElse(null);
	}

	// 4.METHOD TO CALL AT THE TIME WHEN USER IS ONBOARDING .
	@Override
	public Payments addPayment(Payments payment) {
		// TODO Auto-generated method stub
		Payments firstPay = new Payments();
		try {
			firstPay.setTransactionId(payment.getTransactionId());
			firstPay.setGuestId(payment.getGuestId());
			firstPay.setOccupancyType(payment.getOccupancyType());
			firstPay.setTransactionDate(payment.getTransactionDate());
			firstPay.setAmountPaid(payment.getAmountPaid());
			firstPay.setPaymentPurpose(payment.getPaymentPurpose());
			firstPay.setBuildingId(payment.getBuildingId());
			java.sql.Date c = new java.sql.Date(payment.getCreatedOn().getTime());
			payment.setCreatedOn(c);
			firstPay.setCreatedBy(payment.getCreatedBy());
			firstPay.setPaymentPurpose(payment.getPaymentPurpose());

			return repo.save(firstPay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Payments getPaymentByGuestId(String guestId) throws Exception {
		try {

			Payments responsePay = repo.findByGuestId(guestId);
			return responsePay;
		} catch (Exception e) {
			throw new ResourceNotFoundException(false, "guest Id not found");
		}
	}

	// 7.POSTING THE DATA OF GUEST AFTER ONBOARDING .
	@Override
	public String addPaymentAfterOnBoard(PostPayments payment) {
		String eUri = "http://emailService/mail/sendPaymentConfirmation/";
		Guest guest = new Guest();
		Payments secondpay = new Payments();
		try {
			secondpay.setAmountPaid(payment.getAmountPaid());
			secondpay.setBuildingId(payment.getBuildingId());
			secondpay.setTransactionId(payment.getTransactionId());
			secondpay.setPaymentPurpose(payment.getPaymentPurpose());
			secondpay.setOccupancyType(payment.getOccupancyType());
			secondpay.setGuestId(payment.getGuestId());
			java.sql.Date c = new java.sql.Date(payment.getCreatedOn().getTime());
			payment.setCreatedOn(c);

			secondpay.setTransactionDate(secondpay.getTransactionDate());
			secondpay.setRefundAmount(payment.getRefundAmount());
			Payments p = repo.save(secondpay);
			PaymentConfirmation pc = new PaymentConfirmation();

			com.arshaa.model.DueResponse dRes = dueCalcServ.updateDueAmount(p.getAmountPaid(), p.getRefundAmount(),
					p.getGuestId());
			Guest g = guestRepo.findById(p.getGuestId())
					.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));

			String buildingname = template.getForObject(
					"http://bedService/bed/getBuildingNameByBuildingId/" + secondpay.getBuildingId(), String.class);
			pc.setAmountPaid(payment.getAmountPaid());
			pc.setEmail(g.getEmail());

			pc.setName(g.getFirstName());
			pc.setBedId(g.getBedId());
			pc.setBuildingName(buildingname);
			pc.setTransactionId(payment.getTransactionId());
			pc.setPaymentId(p.getId());
			pc.setAmountPaid(p.getAmountPaid());
			pc.setRefundAmount(p.getRefundAmount());
			pc.setDate(secondpay.getTransactionDate());
			pc.setCheckInDate(g.getCheckInDate());
			pc.setPurpose(secondpay.getPaymentPurpose());
			EmailResponse pcEmail = template.postForObject(eUri, pc, EmailResponse.class);
			if (pcEmail.isStatus() == true && dRes.isStatus() == true) {
				return "Payment done, Due updated and email also sent successfully";
			} else if (dRes.isStatus() == true) {
				return "Payment done, Due updated";
			} else if (pcEmail.isStatus() == true) {
				return "Payment done, email  sent successfully";
			} else {
				return "Payment done";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@Override
	public PaymentRemainderData getCountOfPaymentAmount(String guestId) {
		PaymentRemainderData count = new PaymentRemainderData();

		try {
			long amountPaidCount = repo.getCountOfAmount(guestId);
			long refundAmonutCount = repo.getCountOfRefundAmount(guestId);
			count.setTotalAmountPaid(amountPaidCount);
			count.setTotalRefundAmount(refundAmonutCount);
			return count;
		} catch (Exception e) {

			return count;
		}

	}

	@Override
	public PaymentApiDetails getAllPays(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Payments> pagePost = this.repo.findAll(p);
		List<Payments> allPosts = pagePost.getContent();
		List<THistory> thist = new ArrayList<>();
		pagePost.forEach(h -> {
			THistory th = new THistory();
			th.setAmountPaid(h.getAmountPaid());
			th.setGuestId(h.getGuestId());
			Guest name = guestRepo.findById(h.getGuestId())
					.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
			th.setGuestName(name.getFirstName());
			String buildingName = template.getForObject(
					"http://bedService/bed/getBuildingNameByBuildingId/" + h.getBuildingId(), String.class);
			th.setBuildingName(buildingName);
			th.setBedId(name.getBedId());
			th.setPersonalNumber(name.getPersonalNumber());
			th.setRefundAmount(h.getRefundAmount());
			th.setPaymentPurpose(h.getPaymentPurpose());
			th.setBuildingId(h.getBuildingId());
			th.setTransactionId(h.getTransactionId());
			th.setTransactionDate(h.getTransactionDate());
			th.setId(h.getId());

			th.setOccupancyType(h.getOccupancyType());
			thist.add(th);
		});
		PaymentApiDetails postResponse = new PaymentApiDetails();

		postResponse.setContent(thist);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());

		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public ResponseEntity getMonthlySummary(int month, int year, int buildingId) {

		Response response = new Response();
		MonthlySummary ms = new MonthlySummary();
		try {
			double incomeAmount = repo.getCountOfAmountPaidByBuildingId(month, year, buildingId);
			double refundAmount = repo.getCountOfRefundByBuildingId(month, year, buildingId);
			ms.setIncomeAmount(incomeAmount);
			ms.setRefundAmount(refundAmount);
			response.setStatus(true);
			response.setData(ms);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setStatus(true);
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@Override
	public List<THistory> getByBuildingIdAndByTransactionDateBetween(Integer buildingId, Date startDate, Date endDate) {

		List<Payments> payment = repo.findAllByBuildingIdAndTransactionDateBetween(buildingId, startDate, endDate);

		List<THistory> thist = new ArrayList<>();
		payment.forEach(h -> {
			THistory th = new THistory();
			th.setAmountPaid(h.getAmountPaid());
			th.setGuestId(h.getGuestId());
			Guest name = guestRepo.findById(h.getGuestId())
					.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
			th.setGuestName(name.getFirstName());
			String buildingName = template.getForObject(
					"http://bedService/bed/getBuildingNameByBuildingId/" + h.getBuildingId(), String.class);
			th.setBuildingName(buildingName);
			th.setBedId(name.getBedId());
			th.setPersonalNumber(name.getPersonalNumber());
			th.setRefundAmount(h.getRefundAmount());
			th.setPaymentPurpose(h.getPaymentPurpose());
			th.setBuildingId(h.getBuildingId());
			th.setTransactionId(h.getTransactionId());
			th.setTransactionDate(h.getTransactionDate());
			th.setId(h.getId());

			th.setOccupancyType(h.getOccupancyType());
			thist.add(th);
		});
		return thist;
	}

	@Override
	public Double getTodaysIncome(int buildingId) {

		try {
			Double todaysIncome = repo.getCountOfTodaysIncome(buildingId);
			if (todaysIncome > 0) {
				double val = new BigDecimal(todaysIncome).intValue();
				return val;
			} else {
				return todaysIncome = 0.0;
			}
		} catch (Exception e) {
			System.out.println("Something went Wrong!" + e.getMessage());
		}
		return null;

	}

	public List<RecentTransactions> getRecent(Integer buildingId) {
		List<Payments> list;

		try {
			if (buildingId == 0) {
				list = repo.findAllByOrder();
				List<RecentTransactions> recent = new ArrayList<>();

				list.forEach(payment -> {
					RecentTransactions rt = new RecentTransactions();
					rt.setAmountPaid(payment.getAmountPaid());
					rt.setGuestId(payment.getGuestId());
					Guest name = guestRepo.findById(payment.getGuestId())
							.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
					rt.setGuestName(name.getFirstName());
					rt.setBedId(name.getBedId());
					String buildingName = template.getForObject(
							"http://bedService/bed/getBuildingNameByBuildingId/" + payment.getBuildingId(),
							String.class);
					rt.setBuildingName(buildingName);
					rt.setId(payment.getId());
					rt.setPaymentPurpose(payment.getPaymentPurpose());
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

					rt.setTransactionDate(payment.getTransactionDate());
					rt.setTransactionId(payment.getTransactionId());
					rt.setBuildingId(payment.getBuildingId());
					recent.add(rt);
				});
				return recent;
			} else {
				list = repo.findAllByBuildingId(buildingId);
				List<RecentTransactions> recent = new ArrayList<>();

				list.forEach(payment -> {
					RecentTransactions rt = new RecentTransactions();
					rt.setAmountPaid(payment.getAmountPaid());
					rt.setGuestId(payment.getGuestId());
					Guest name = guestRepo.findById(payment.getGuestId())
							.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
					rt.setGuestName(name.getFirstName());
					rt.setBedId(name.getBedId());
					String buildingName = template.getForObject(
							"http://bedService/bed/getBuildingNameByBuildingId/" + payment.getBuildingId(),
							String.class);
					rt.setBuildingName(buildingName);
					rt.setId(payment.getId());
					rt.setPaymentPurpose(payment.getPaymentPurpose());
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

					rt.setTransactionDate(payment.getTransactionDate());
					rt.setTransactionId(payment.getTransactionId());
					rt.setBuildingId(payment.getBuildingId());
					recent.add(rt);
				});

				return recent;

			}
		} catch (Exception e) {
			throw new ResourceAccessException("no data found");
		}

	}

	@Override
	public List<THistory> getByTransactionDateAndBuildingId(Date transactionDate, Integer buildingId) {

		List<Payments> payment = repo.findByTransactionDateAndBuildingId(transactionDate, buildingId);

		List<THistory> thist = new ArrayList<>();
		List<Guest> l = guestRepo.findAll();
		Map<String, String> map = l.stream().collect(Collectors.toMap(Guest::getId, Guest::getFirstName));
		log.info("guestNames{}", map);
		Map<String, String> maps = l.stream().collect(Collectors.toMap(Guest::getId, Guest::getBedId));
		payment.forEach(h -> {

			THistory th = new THistory();
			th.setAmountPaid(h.getAmountPaid());
			th.setGuestId(h.getGuestId());

			th.setGuestName(map.get(h.getGuestId()));
			th.setBedId(maps.get(h.getGuestId()));
			th.setRefundAmount(h.getRefundAmount());
			th.setPaymentPurpose(h.getPaymentPurpose());
			th.setBuildingId(h.getBuildingId());
			th.setTransactionId(h.getTransactionId());
			th.setTransactionDate(h.getTransactionDate());
			th.setId(h.getId());

			th.setOccupancyType(h.getOccupancyType());
			thist.add(th);
		});
		return thist;
	}

	@Override
	public ApisResponse getTodaysPayments(Integer buildingId) {
		List<Payments> list;
		ApisResponse response = new ApisResponse();

		List<Guest> l = guestRepo.findAll();

		Map<String, String> maps = l.stream().collect(Collectors.toMap(Guest::getId, Guest::getBedId));
		log.info("bedId {}", maps);
//		Map<String, String> map = l.stream().collect(Collectors.toMap(Guest::getId, Guest::getFirstName));
//		log.info("guestNames{}", map);
		try {
			list = repo.getTodaysPaymentsHistory(buildingId);
			List<RecentTransactions> recent = new ArrayList<>();
			list.forEach(payment -> {
				RecentTransactions rt = new RecentTransactions();
				rt.setAmountPaid(payment.getAmountPaid());
				rt.setGuestId(payment.getGuestId());
				rt.setBedId(maps.get(payment.getGuestId()));

				Guest name = guestRepo.findById(payment.getGuestId())
						.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
				rt.setGuestName(name.getFirstName());
				rt.setId(payment.getId());
				rt.setPaymentPurpose(payment.getPaymentPurpose());
				rt.setTransactionDate(payment.getTransactionDate());
				rt.setTransactionId(payment.getTransactionId());
				rt.setBuildingId(payment.getBuildingId());
				recent.add(rt);
			});
			response.setStatus(true);
			response.setMessage("data fetched successfully");
			response.setData(recent);
			return response;
		} catch (Exception e) {
			throw new IllegalAccessError("please wait after sometime");
		}
	}

	@Override
	public Long getSumofMonthlyIncomeByBuildingId(Integer buildingId) {
		try {
			if (buildingId == 0) {
				Long monthlySumBybuilding = repo.getSumOfMonthOfBuildings();
				return monthlySumBybuilding;

			} else {
				Long monthlySumBybuilding = repo.getSumOfMonthbyBuildingId(buildingId);
				return monthlySumBybuilding;

			}

		} catch (Exception e) {
			throw new IllegalAccessError("Building Id is not Present");
		}

	}

	@Override
	public ResponseEntity<byte[]> recordPaymenAndSendPDF(PostPayments payment) {
		String eUri = "http://emailService/mail/sendPaymentConfirmation/";
		Guest guest = new Guest();
		Payments secondpay = new Payments();
		secondpay.setAmountPaid(payment.getAmountPaid());
		secondpay.setBuildingId(payment.getBuildingId());
		secondpay.setTransactionId(payment.getTransactionId());
		secondpay.setPaymentPurpose(payment.getPaymentPurpose());
		secondpay.setOccupancyType(payment.getOccupancyType());
		secondpay.setGuestId(payment.getGuestId());
		java.sql.Date c = new java.sql.Date(payment.getCreatedOn().getTime());
		payment.setCreatedOn(c);

		secondpay.setTransactionDate(secondpay.getTransactionDate());
		secondpay.setRefundAmount(payment.getRefundAmount());
		Payments p = repo.save(secondpay);
		PaymentConfirmation pc = new PaymentConfirmation();

		com.arshaa.model.DueResponse dRes = dueCalcServ.updateDueAmount(p.getAmountPaid(), p.getRefundAmount(),
				p.getGuestId());
		Guest g = guestRepo.findById(p.getGuestId())
				.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));

		String buildingname = template.getForObject(
				"http://bedService/bed/getBuildingNameByBuildingId/" + secondpay.getBuildingId(), String.class);
		pc.setAmountPaid(payment.getAmountPaid());
		pc.setEmail(g.getEmail());

		pc.setName(g.getFirstName());
		pc.setBedId(g.getBedId());
		pc.setBuildingName(buildingname);
		pc.setTransactionId(payment.getTransactionId());
		pc.setPaymentId(p.getId());
		pc.setAmountPaid(p.getAmountPaid());
		pc.setRefundAmount(p.getRefundAmount());
		pc.setDate(secondpay.getTransactionDate());
		pc.setCheckInDate(g.getCheckInDate());
		pc.setPurpose(secondpay.getPaymentPurpose());
		EmailResponse pcEmail = template.postForObject(eUri, pc, EmailResponse.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, baos);

		document.open();

		// Title
		Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
		Paragraph title = new Paragraph("RECEIPT VOUCHER", titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);

		document.add(Chunk.NEWLINE);

		// Table with receipt data
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);

		addRow(table, "Voucher Reference No.", pc.getTransactionId());
		addRow(table, "Date", LocalDate.now().toString());
		addRow(table, "Name", pc.getName());
		addRow(table, "Amount Paid", pc.getAmountPaid() + " Rs");
		addRow(table, "Room No", pc.getBedId());
		addRow(table, "Check-in Date", pc.getCheckInDate().toString());
		addRow(table, "Purpose", pc.getPurpose());

		document.add(table);

		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("This is a system generated receipt, no signature required."));

		document.close();

		// 3. Return as downloadable response
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=receipt-" + pc.getPaymentId() + ".pdf")
				.contentType(MediaType.APPLICATION_PDF).body(baos.toByteArray());

	}

	private void addRow(PdfPTable table, String key, String value) {
		PdfPCell cell1 = new PdfPCell(new Phrase(key));
		PdfPCell cell2 = new PdfPCell(new Phrase(value));

		cell1.setPadding(5);
		cell2.setPadding(5);

		table.addCell(cell1);
		table.addCell(cell2);
	}

}
