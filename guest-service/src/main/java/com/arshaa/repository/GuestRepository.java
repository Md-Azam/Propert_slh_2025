
package com.arshaa.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.arshaa.entity.Guest;
import org.springframework.data.domain.Sort;
@Repository
public interface GuestRepository extends JpaRepository<Guest, String> {
//	Guest findById(String guestId);

	List<Guest> findByBuildingIdAndGuestStatusIn(Integer buildingId, List<String> guestStatus);
	
	@Query(value = "SELECT * FROM guest WHERE guest_status != 'VACATED' AND planned_check_out_date = DATE_ADD(CURDATE(), INTERVAL 2 DAY)", nativeQuery = true)
	List<Guest> findGuestsWithPlannedCheckoutInTwoDays();

	List<Guest> findByGuestStatusIn(List<String> guestStatus);

	Guest getGuestBybedId(String bedId);

	Guest getGuestBybedIdAndGuestStatus(String bedId, String guestStatus);

	Optional<Guest> findByBuildingId(int buildingId);

	Boolean existsByBedIdAndGuestStatusIn(String bedId, List<String> guestStatus);

	List<Guest> getByBuildingId(int buildingId);

	Optional<List<Guest>> findByBuildingIdAndGuestStatus(int buildingId, String guestStatus);

	Optional<List<Guest>> findByBuildingIdAndGuestStatusAndOccupancyType(int buildingId, String guestStatus,
			String occupancyType);

	List<Guest> findByGuestStatus(String guestStatus);

	Guest getByGuestStatus(String guestStatus);

	Guest getPersonalNumberById(String id);

	Page<Guest> findAllByGuestStatus(Pageable p, String guestStatus);

	Guest getBedIdById(String id);

	Guest getNameById(String id);

	List<Guest> findByGuestStatusAndBuildingId(Integer buildingId, String status);

	// Find Guest Who are About to checkOut(Regular + Monthly + Daily) .
	@Query(value = "select * from guest where planned_check_out_date IS NOT NULL and guest_status ='InNotice' and occupancy_type ='Regular'\r\n"
			+ "UNION\r\n"
			+ "select * from guest where planned_check_out_date IS NOT NULL and guest_status ='Active' and occupancy_type IN ('Daily','OneMonth') ", nativeQuery = true)
	List<Guest> findByCheckOut();

	Guest getEmailById(String id);

	@Modifying
	Guest findByPackageId(Integer id);

	@Modifying
	@Transactional
	List<Guest> findAllByPackageId(Integer packageId);

	@Query(value = "select SUM(due_amount) from guest where due_amount>0 and guest_status in('active','InNotice')  ", nativeQuery = true)
	Long getTotalDueByAllGuests();

	@Query(value = "select sum(due_amount) from guest where due_amount>0 and guest_status in('active','InNotice') and building_id =?1", nativeQuery = true)
	Long getGuestTotalDueByBuildingId(@Param("building_id") Integer buildinId);

	@Query(value = "SELECT count(*)from guest where guest.due_amount =0 and guest.building_id=?1  and guest_status in(\"active\")  ", nativeQuery = true)
	int getCountOfActiveGuestsWithoutDue(int buildingId);

	@Query(value = "SELECT count(*) from guest where guest.due_amount <=0 and guest.building_id=?1  and guest_status in\r\n"
			+ "(\"active\") and guest.occupancy_type=?2 ", nativeQuery = true)
	int getCountOfActiveGuestsWithoutDueByOccupancyType(int buildingId, String occupancyType);

	@Query(value = "SELECT count(*) FROM  guest where  guest.due_amount >0 and guest.building_id=?1 and guest.occupancy_type=?2 and guest_status in(\"active\",\"InNotice\")", nativeQuery = true)
	int getCountOfDueGuestsByOccupancyType(int buildingId, String occupancyType);

	@Query(value = "SELECT count(*)  FROM  guest where  guest.due_amount >0 and guest.building_id=?1 and guest_status in(\"active\",\"InNotice\")", nativeQuery = true)
	int getCountOfDueGuests(int buildingId);

//	@Query(value = "SELECT count(*) from  guest where occupancy_type in(\"OneMonth\",\"daily\") and guest_status='active' and building_id=?1 "
//			+ " and guest.planned_check_out_date < curdate(); ", nativeQuery = true)
//	int exceededGuestsNotRegular(int buildingId);
	@Query(value = "SELECT count(*) FROM guest WHERE guest_status != 'VACATED' AND occupancy_type='OneMonth' AND planned_check_out_date < CURDATE() AND building_id=?1",nativeQuery = true)
	int exceededGuestsNotRegular(int buildingId);

	@Query(value = "SELECT count(*) from  guest where occupancy_type = \"Regular\" and guest_status='InNotice' and building_id=?1 "
			+ " and planned_check_out_date < curdate() ; ", nativeQuery = true)
	int exceededGuestsRegular(int buildingId);

	@Query(value = "SELECT count(*) from  guest where occupancy_type in(\"OneMonth\",\"daily\") and guest_status='Active' and building_id=?1 "
			+ " and planned_check_out_date = curdate(); ", nativeQuery = true)
	int checkoutTodayGuestsNotRegular(int buildingId);

	@Query(value = "SELECT count(*) from  guest where occupancy_type = \"Regular\" and guest_status='InNotice' and building_id=?1 and planned_check_out_date =curdate()", nativeQuery = true)
	int checkoutTodayGuestsRegular(int buildingId);

	List<Guest> findAllByBuildingIdAndCheckInDateBetween(Integer buildingId,Sort field, java.sql.Date startDate,
			java.sql.Date endDate);

	List<Guest> findAllByGuestStatusAndBuildingIdAndCheckOutDateBetween(String guestStatus, Integer buildingId,Sort s,
			java.sql.Date fromDate, java.sql.Date toDate);

	List<Guest> findByBuildingIdAndNoticeDateBetween(Integer buildingId, Date fromDate, Date toDate);

	List<Guest> findByBuildingIdAndPlannedCheckOutDateBefore(Integer BuildingId, @Param(value = "plannedCheckOutDate") Date plannedCheckOutDate);

	List<Guest> findByBuildingIdAndGuestStatusInAndPlannedCheckOutDateEquals(Integer BuildingId,
			List<String> guestStatus, @Param(value = "plannedCheckOutDate") Date plannedCheckOutDate);

	// Current month check out guest
	@Query(value = "select * from guest where MONTH(check_out_date)=MONTH(CURRENT_DATE()) and YEAR(check_out_date)= YEAR(CURRENT_DATE()) and guest_status='VACATED' and check_out_date is not null order by check_out_date desc ", nativeQuery = true)
	public List<Guest> findByVacatedGuest();
	
	//guest for tablet-info
	 @Query("SELECT g FROM Guest g WHERE  g.buildingId = :buildingId AND g.bedId LIKE %:bedId% AND g.guestStatus IN ('Active', 'InNotice')")
	    Guest findGuestsByBedIdAndBuildingIdAndGuestStatus (@Param("buildingId") Integer buildingId,@Param(value="bedId") String bedId);

	// Current month checked In
	@Query(value = "select * from guest where MONTH(check_in_date)=MONTH(CURRENT_DATE()) and YEAR(check_in_date)= YEAR(CURRENT_DATE()) and guest_status='Active' and  check_in_date is not null order by check_in_date desc", nativeQuery = true)
	public List<Guest> getCurrentMonthAddmitted();
	
}