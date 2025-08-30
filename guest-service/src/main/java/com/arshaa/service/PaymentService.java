package com.arshaa.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import com.arshaa.entity.Payments;
import com.arshaa.model.ApisResponse;
import com.payment.common.PaymentApiDetails;
import com.payment.common.PaymentRemainderData;
import com.payment.common.PostPayments;
import com.payment.common.RecentTransactions;
import com.payment.common.THistory;

public interface PaymentService {

	// 2. FETCHING PAYMENT DETAILS BY PARTICULAR GUESTID
	public Payments getPaymentByGuestId(String guestId) throws Exception;

	// 3.FETCHING PAYMENTS DETAILS BY PAYMENTID .
	public Payments getPaymentById(int paymentId);

	// 4. METHOD TO UPDATING DATA OF PAYMENT HISTORY BY MANAGER .
//	public Payments updatePayment(Payments payment);

	// 5. METHOD TO CALL AT THE TIME WHEN USER IS ONBOARDING .
	public Payments addPayment(Payments payment);

	// 7.POSTING THE DATA OF GUEST AFTER ONBOARDING .
	public String addPaymentAfterOnBoard(PostPayments payment);

	public PaymentRemainderData getCountOfPaymentAmount(String guestId);

	public ResponseEntity getMonthlySummary(int month, int year, int buildingId);

	List<THistory> getByBuildingIdAndByTransactionDateBetween(Integer buildingId, Date startDate, Date endDate);

	public Double getTodaysIncome(int buildingId);

	public ApisResponse getTodaysPayments(Integer buildingId);

	public List<THistory> getByTransactionDateAndBuildingId(Date transactionDate, Integer buildingId);

//	public ApisResponse getTodaysPayments(Integer buildingId);

	PaymentApiDetails getAllPays(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	public List<RecentTransactions> getRecent(Integer buildingId);
	Long getSumofMonthlyIncomeByBuildingId(Integer buildingId);
}
