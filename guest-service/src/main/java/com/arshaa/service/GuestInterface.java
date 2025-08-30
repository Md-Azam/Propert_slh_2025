package com.arshaa.service;

import com.arshaa.common.Bed;
import com.arshaa.common.GuestName;
import com.arshaa.common.InitiateCheckoutByGuestId;
import com.arshaa.common.UpdateGuestDetails;
import com.arshaa.dtos.ForceCheckOutMsg;
import com.arshaa.dtos.GuestData;
import com.arshaa.dtos.GuestDto;
import com.arshaa.dtos.RatedDto;
import com.arshaa.entity.Guest;
import com.arshaa.entity.RatesConfig;
import com.arshaa.exception.ResourceNotFoundException;
import com.arshaa.model.ApisResponse;
import com.arshaa.model.BedInfoForBedChange;
import com.arshaa.model.DueGuestsList;
import com.arshaa.model.FilterBedsCountInRAT;
import com.arshaa.model.GuestsInNotice;
import com.arshaa.model.InnoticeToRegular;
import com.arshaa.model.PreviousGuests;
import com.arshaa.model.VacatedGuests;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface GuestInterface {

	GuestData getAllGuestData(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	public List<GuestDto> getGuests(String field);

	public Guest getGuestById(String guestId);

	public Guest addGuest(Guest guest);
	
	public List<GuestDto> getGuests(Integer buildingId,String field, java.sql.Date startDate,
			java.sql.Date endDate);

	
	public double updateGuest(Guest guest);

	public void deleteGuest(String guestId);

	public long getTotalDue();

	public ApisResponse getFinalDueAmountById(String id);

	public double getOnlyDues(String id);

	public ResponseEntity paymentRemainder(int buildingId);

	public ResponseEntity duesGuestsList(int buildingId,String field);

	public List<RatesConfig> getByBuildingId(int buildingId);

	public RatesConfig updateRoomRent(RatedDto Rdto,Integer id);
	
	public RatesConfig addingNewRent(RatesConfig rates,Integer buildingId);

	public List<RatesConfig> findByBuildingIdAndOccupancyType(int buildingId, String occupancyType);

	public List<RatesConfig> findByOccupancyType(String occupancyType);

	public List<VacatedGuests> findByGuestStatus(String guestStatus,Integer buildingId,String field,java.sql.Date fromDate,java.sql.Date toDate);
		

	public ResponseEntity getAllRents(String occupancyType, int buildingId, int sharing);

	public ApisResponse changeRegularFromInNotice(String guestId);

	public GuestName getNameAndBed(String id);

	public ResponseEntity updateGuestDetails(UpdateGuestDetails editGuest, String id);

	public ResponseEntity GuestCheckoutBody(InitiateCheckoutByGuestId gcb, String id);

	public ResponseEntity buildingSummaryForRat(Integer buildingId, String status);
	
	public InitiateCheckoutByGuestId newInitiateCheckout(InitiateCheckoutByGuestId data,String id ) ;

	public long getDueGuestsCount();
	// public GuestData getAllGuestData(Integer pageNumber, Integer pageSize, String
	// sortBy, String sortDir);

	public ApisResponse getBedInfoForBedChange(String guestId, int buildingId, String bedId);

	public ApisResponse finishBedChange(String guestId, BedInfoForBedChange bedChange);

	public ApisResponse BedChangeForDailyAndMonthly(String guestId, int buildingId, String bedId);

	public ApisResponse finishBedChangeForDailyAndMonthly(String guestId, BedInfoForBedChange bedChange);

	public ApisResponse forceCheckOut(ForceCheckOutMsg checkOutConfirmation, String id);

	public ApisResponse editDueAmount(String guestId, double dueAmount);

	public List<FilterBedsCountInRAT> getFilterBedsCountInRAT(int buildingId);
	
	public List<GuestDto> findGuestForDining(Integer buildingId);
	
	public GuestDto getGuestForTab(Integer buildingId,String bedId);
	}
