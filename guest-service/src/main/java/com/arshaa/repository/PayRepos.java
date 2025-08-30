package com.arshaa.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arshaa.entity.Payments;

@Repository
public interface PayRepos extends JpaRepository<Payments, Integer> {
	Payments findByGuestId(String guestId);

	Payments findDueAmountByGuestId(String guestId);

	List<Payments> findPaymentsByGuestId(String guestId);
	

	@Query(value = "select sum(due_amount) from Payments ", nativeQuery = true)
	public double getTotalDue();

	@Query(value = "SELECT COUNT(*) FROM payments WHERE due_amount>0 ", nativeQuery = true)
	public int getCount();

	List<Payments> findByTransactionDateAndBuildingId(Date transactionDate, Integer buildingId);

	Optional<List<Payments>> findTop30AllByOrderByTransactionDateDesc();

	@Query(value ="select * from payments where date(transaction_date)=curdate() and building_id=?1 and transaction_date is not null", nativeQuery = true)
	List<Payments> getTodaysPaymentsHistory(Integer buildingId);

	@Query(value = "SELECT * FROM `payments`   where building_id=?1 and MONTH(created_on) = MONTH(CURRENT_DATE()) and YEAR(created_on)= YEAR(CURRENT_DATE()) ORDER BY id DESC", nativeQuery = true)
	List<Payments> findAllByBuildingId(Integer buildingId);

	@Query(value = "SELECT * FROM `payments` where MONTH(created_on) = MONTH(CURRENT_DATE()) and  YEAR(created_on)= YEAR(CURRENT_DATE()) ORDER BY id DESC", nativeQuery = true)
	List<Payments> findAllByOrder();

	List<Payments> findAllByBuildingIdAndTransactionDateBetween(Integer buildingId, Date startDate, Date endDate);

	List<Payments> findAllPaymentsByGuestId(String guestId);

	List<Payments> getDueAmountByGuestId(String guestId);

	Optional<List<Payments>> findPaymentsByBuildingId(int buildingId);

	@Query(value = "SELECT SUM(amount_paid) FROM payments u WHERE guest_id=:id", nativeQuery = true)
	long getCountOfAmount(@Param("id") String guestId);

	@Query(value = "SELECT SUM(refund_amount) FROM payments u WHERE guest_id=:id", nativeQuery = true)
	long getCountOfRefundAmount(@Param("id") String guestId);

	@Query(value = "select SUM(amount_paid) from payments  where month(payments.transaction_date) = ?1 and year(payments.transaction_date)= ?2 and building_id=?3", nativeQuery = true)
	double getCountOfAmountPaidByBuildingId(@Param("month") Integer month, @Param("month") Integer year,
			@Param("id") int buildingId);

	@Query(value = "select SUM(refund_amount) from payments  where month(payments.transaction_date) = ?1 and year(payments.transaction_date)= ?2 and building_id=?3", nativeQuery = true)
	double getCountOfRefundByBuildingId(@Param("month") Integer month, @Param("month") Integer year,
			@Param("id") int buildingId);

	@Query(value = "select SUM(amount_paid) from payments where 1=1\r\n"
			+ "and date(transaction_date)=curdate() and building_id=?1 and transaction_date is not null;", nativeQuery = true)
	Double getCountOfTodaysIncome(@Param("buildingId") int buildingId);

	@Query(value = "SELECT SUM(amount_paid) FROM payments WHERE  building_id = ?1 and year(transaction_date) = ?2", nativeQuery = true)
	Double getYearlyIncome(@Param("buildingId") int buildingId, @Param("transaction_date") int year);

    @Query(value = "select sum(amount_paid) from payments where building_id=?1 and  MONTH(created_on) = MONTH(CURRENT_DATE()) and YEAR(created_on)= YEAR(CURRENT_DATE())",nativeQuery = true)
    Long getSumOfMonthbyBuildingId(Integer buildingId);
    @Query(value = "select sum(amount_paid) from payments where MONTH(created_on)=MONTH(CURRENT_DATE()) and YEAR(created_on)= YEAR(CURRENT_DATE())",nativeQuery = true)
    Long getSumOfMonthOfBuildings();
}