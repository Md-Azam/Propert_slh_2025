package com.arshaa.controller;

import com.arshaa.common.Bed;
import com.arshaa.common.GuestModel;
import com.arshaa.common.InitiateCheckoutByGuestId;
import com.arshaa.common.UpdateGuestDetails;
import com.arshaa.dtos.GuestData;
import com.arshaa.dtos.GuestDto;
import com.arshaa.dtos.AppConstants;
import com.arshaa.dtos.ForceCheckOutMsg;
import com.arshaa.dtos.RatedDto;
import com.arshaa.entity.Guest;
import com.arshaa.entity.GuestProfile;

import com.arshaa.entity.RatesConfig;
import com.arshaa.entity.SecurityDeposit;
import com.arshaa.exception.ResourceNotFoundException;
import com.arshaa.entity.Defaults;
import com.arshaa.model.ApisResponse;
import com.arshaa.model.BedInfoForBedChange;
import com.arshaa.model.Constants;
import com.arshaa.model.DueGuestsList;
import com.arshaa.model.EmailTempModel;
import com.arshaa.model.GuestImageDisplay;
import com.arshaa.model.GuestsInNotice;
import com.arshaa.model.InnoticeToRegular;
import com.arshaa.model.MonthlyAndDailyPlanExtension;
import com.arshaa.model.PreviousGuests;
import com.arshaa.model.ResponseFile;
import com.arshaa.model.ResponseMessage;
import com.arshaa.model.VacatedGuests;
import com.arshaa.repository.GuestProfileRepo;
import com.arshaa.repository.GuestRepository;
import com.arshaa.repository.RatesConfigRepository;
import com.arshaa.service.DueCalculateService;
import com.arshaa.service.GuestInterface;
import com.arshaa.service.GuestProfileService;
import com.arshaa.service.SecurityDepositService;
import com.arshaa.service.SetSecurityInterface;
import com.google.common.net.HttpHeaders;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.StreamUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URISyntaxException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;

@CrossOrigin("*")
@RestController
@RequestMapping("/guest")
@Slf4j
public class GuestController {

	@Autowired
	@PersistenceContext
	private EntityManager em;

	@Autowired(required = true)
	private GuestRepository repository;

	@Autowired
	@Lazy
	private RestTemplate template;

	@Autowired(required = true)
	private GuestInterface service;
	@Autowired
	private GuestProfileService gpServe;
	@Autowired
	private SecurityDepositService securityDepositService;
	Constants constants = new Constants();

	@Autowired
	private GuestProfileRepo repost;

	@Autowired
	private SetSecurityInterface sint;

	@Autowired
	private RatesConfigRepository rcr;

	@Autowired
	private DueCalculateService dueService;
	
	
	@PostMapping("/addingnewRent/{buildingId}")
	public ResponseEntity<RatesConfig> addingNewRates(@RequestBody RatesConfig config,@PathVariable Integer buildingId){
		System.out.println("adding Building Rents ........."+ config);
		service.addingNewRent(config, buildingId);
		System.out.println("adding Building Rents sucess .........");
		return new ResponseEntity<RatesConfig>(config,HttpStatus.OK);
	}
	
	
	//http://localhost:8989/guest/tab/3/SKN-212-A
	@GetMapping("/tab/{buildingId}/{bedId}")	
	public ResponseEntity<GuestDto> getGuestForTablet(@PathVariable Integer buildingId,@PathVariable String bedId){
		GuestDto dto =  this.service.getGuestForTab(buildingId,bedId);
		return new ResponseEntity<GuestDto>(dto,HttpStatus.OK);
	}
	
	@GetMapping("/dining/{buildingId}")
	public ResponseEntity<List<GuestDto>> getDiningData(@PathVariable Integer buildingId){
		List<GuestDto> dto = this.service.findGuestForDining(buildingId);
		return new ResponseEntity<List<GuestDto>>(dto,HttpStatus.OK);
	}

	@GetMapping("/changeToRegular/{guestId}")
	public ResponseEntity<ApisResponse> updateGuestToRegular(@PathVariable String guestId) {
		ApisResponse guest = this.service.changeRegularFromInNotice(guestId);
		return new ResponseEntity<ApisResponse>(guest, HttpStatus.OK);
	}

	// Guest Reports Sorted .
		@GetMapping("/getAllGuests/{buildingId}")
		public List<GuestDto> getGuests(@PathVariable Integer buildingId, @RequestParam String field,
				@RequestParam Date startDate,@RequestParam Date endDate)
		{
			return service.getGuests(buildingId,field,startDate,endDate);
		}
	@DeleteMapping("/del/{guestId}")
	public String deleteProfile(@PathVariable String guestId) throws RuntimeException {
		try {
			repost.deleteByGuestId(guestId);
			return "deleted sucessfully";
		} catch (Exception e) {
			throw new IllegalArgumentException("Id is not found");
		}
	}

	@DeleteMapping("/d/{guestId}")
	public String deleteProfileByGuestId(@PathVariable String guestId) throws RuntimeException {
		try {
			repost.deleteGuestProfileByGuestId(guestId);
			return "deleted sucessfully";
		} catch (Exception e) {
			throw new IllegalArgumentException("Id is not found");
		}
	}
	@GetMapping("/findExceedGuest/{buildingId}")
	public List<GuestDto> findExceededGuestByBuildingId (@PathVariable Integer buildingId) throws ParseException {
		List<String> ustatus = new ArrayList<>();
		ustatus.add("Active");
		ustatus.add("InNotice");
		LocalDate date1 = LocalDate.now();
		
		 LocalDate returnvalue= date1.minusDays(1);
		log.info("local date is {}",date1);
		log.info("local date -1 day is {}",returnvalue);
log.info("status of guest {}",ustatus);
		java.util.Date utilDate = new java.util.Date();
		log.info("current date in util=:{}",utilDate);
		java.util.Date date = Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant());
		log.info("converted util date in util=:{}",date);
		String  mdyFormat =	new SimpleDateFormat("MM-dd-yyyy").format(utilDate);
		return this.sint.findExceededGuestByBuildingId(buildingId,new java.sql.Date(date.getTime()));
	}
	
	@GetMapping("/findTodayOut/{buildingId}")
	public List<GuestDto> findTodaysCheckout (@PathVariable Integer buildingId) throws ParseException {
		List<String> ustatus = new ArrayList<>();
		ustatus.add("Active");
		ustatus.add("InNotice");
		LocalDate date1 = LocalDate.now();
		log.info("local date is {}",date1);
log.info("status of guest {}",ustatus);
		java.util.Date utilDate = new java.util.Date();
		log.info("current date in util=:{}",utilDate,date1);
		java.util.Date date = Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant());
		log.info("converted util date in util=:{}",date);
		String  mdyFormat =	new SimpleDateFormat("MM-dd-yyyy").format(utilDate);
		
		return this.sint.findTodaysCheckOutByBuildingId(buildingId,date);
	}
	
	
	// @GetMapping("/getDuesCalc")
//    public ResponseEntity checkDueOfGuest() {
//        try {
//            return new ResponseEntity(dueService.clearDueAmount(), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity(e.getMessage(), HttpStatus.OK);
//        }
//    }

	@PostMapping("/initiatecheckoutbyguestid/{id}")
	public ResponseEntity initiateCheckOut(@RequestBody InitiateCheckoutByGuestId gcb, @PathVariable String id) {
		Guest guest = new Guest();
		try {
			if (id != null) {
				return new ResponseEntity(service.newInitiateCheckout(gcb, id), HttpStatus.OK);
			}
		} catch (Exception e) {
			System.out.println("id not available" + e.getLocalizedMessage());
		}
		return new ResponseEntity(service.newInitiateCheckout(gcb, id), HttpStatus.OK);

	}

	@PutMapping("/editGuestDetails/{guestId}")
	public ResponseEntity editGuest(@RequestBody UpdateGuestDetails ud, @PathVariable String guestId) {
		Guest guest = new Guest();
		try {
			if (guest.getId() != null) {
				return new ResponseEntity(service.updateGuestDetails(ud, guestId), HttpStatus.OK);
			}

		} catch (Exception e) {
			System.out.println("id not available" + e.getLocalizedMessage());
		}
		return new ResponseEntity(service.updateGuestDetails(ud, guestId), HttpStatus.OK);
	}

	// Posting Rates based on sharing
	@PostMapping("/postRates")
	public RatesConfig saveRates(@RequestBody RatesConfig rates) {
		try {
			return rcr.save(rates);
		} catch (Exception e) {
			System.out.println("cant config Rents" + e.getLocalizedMessage());
		}
		return null;
	}

	// delete rents ;
	@DeleteMapping("/deleteRents/{id}")
	public void deleteRents(@PathVariable int id) {
		rcr.deleteById(id);
	}

	@GetMapping("/getRatesByOccupancyType/{occupancyType}")
	public List<RatesConfig> findByOccupancyType(@PathVariable String occupancyType) {
		return service.findByOccupancyType(occupancyType);
	}

	// Guest Reports Sorted .
	// localhost:8095/api/getAllPost?pageNumber=1&pageSize=10&sortBy=id&sortDir=desc
	@GetMapping("/getAllGuests")
	public ResponseEntity<GuestData> getAllPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

		GuestData postResponse = this.service.getAllGuestData(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<GuestData>(postResponse, HttpStatus.OK);
	}

	@GetMapping("/getRatesByBuildingId/{buildingId}")
	public List<RatesConfig> getByBuildingId(@PathVariable int buildingId) {
		return service.getByBuildingId(buildingId);
	};

	@GetMapping("/getRatesByBuildingId/{buildingId}/{occupancyType}")
	public List<RatesConfig> getByBuildingId(@PathVariable int buildingId, @PathVariable String occupancyType) {
		return service.findByBuildingIdAndOccupancyType(buildingId, occupancyType);
	}

//	@GetMapping("/getRatesByOccupancyType/{occupancyType}")
//	public List<RatesConfig> findByOccupancyType(@PathVariable String occupancyType)
//	{
//		return service.findByOccupancyType(occupancyType);
//	}
	@PostMapping("/addGuest")
	public Guest saveGuest(@RequestBody Guest guest) {

		return service.addGuest(guest);

	}

	@PutMapping("/updateRoomRent/{id}")
	public RatesConfig updateRoomRents(@RequestBody RatedDto rdto, @PathVariable Integer id) {
		return this.service.updateRoomRent(rdto, id);
	}

//	@PostMapping("/addPastGuest")
//	public Guest addPostGuest(@RequestBody PreviousGuests guest) {
//		return service.addPostGuest(guest);
//
//	}

	@GetMapping("/getGuestByGuestId/{id}")
	public Guest getOneGuest(@PathVariable("id") String id) {
		return service.getGuestById(id);
	}

	@DeleteMapping("/deleteGuestByGuestId/{id}")
	public void delete(@PathVariable("id") String id) {
		service.deleteGuest(id);
	}

	@PutMapping("/updateDueAmount")
	public double updateDueAmount(@RequestBody Guest guest) {
		return service.updateGuest(guest);
	}

	// http://localhost:7000/guest/getDueAmountOnDashBoard.
	// FETCHING OverAllDUE AMOUNT. .
	@GetMapping("/getDueAmountOnDashBoard")
	public long getTotalDue() {
		return service.getTotalDue();

	}

	@GetMapping("/getGuestByBedId/{guestStatus}/{bedId}")
	public ResponseEntity<GuestModel> getGuestByBedIdAndGuestStatus(@PathVariable String guestStatus, String bedId) {
		GuestModel gm = new GuestModel();
		try {
			Guest guest = repository.getGuestBybedIdAndGuestStatus(bedId, guestStatus);
			if (guest.getGuestStatus().equalsIgnoreCase("active")
					|| guest.getGuestStatus().equalsIgnoreCase("inNotice")) {
				gm.setFirstName(guest.getFirstName());
				gm.setId(guest.getId());
				return new ResponseEntity(guest, HttpStatus.OK);
			}
			return new ResponseEntity("Guest is Inactive", HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity(e.getMessage(), HttpStatus.OK);

		}
	}

	@GetMapping("/getDueGuestsCount")
	public ResponseEntity getDueGuestsCount() {
		return new ResponseEntity(service.getDueGuestsCount(), HttpStatus.OK);
	}

	@GetMapping("/finalCheckout/{id}")
	public ResponseEntity finalCheckOutGuest(@PathVariable String id) {
		return new ResponseEntity(service.getFinalDueAmountById(id), HttpStatus.OK);
	}

	@PutMapping("/forceCheckout/{id}")
	public ResponseEntity forceCheckOutGuest(@RequestBody ForceCheckOutMsg checkOutConfirmation,
			@PathVariable String id) {
		return new ResponseEntity(service.forceCheckOut(checkOutConfirmation, id), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/getBedIdById/{id}")
	public ResponseEntity geeGuestBedByGuestId(@PathVariable String id) {
		Guest guest = repository.getBedIdById(id);
		return new ResponseEntity(guest.getBedId(), HttpStatus.OK);
	}

	@GetMapping("/getcheckInByGuestId/{id}")
	public ResponseEntity getcheckInByGuestId(@PathVariable String id) {
		Guest g = repository.findById(id).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
		EmailTempModel em = new EmailTempModel();
		em.setCheckInDate(g.getCheckInDate());
		return new ResponseEntity(em, HttpStatus.OK);
	}

	// GuestProfile API's
	@PostMapping("/upload/{guestId}")
	public ResponseEntity<ResponseMessage> uploadFile(@PathVariable String guestId,
			@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			gpServe.store(file, guestId);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Can't able to upload file" + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	@GetMapping("/files/{guestId}")
	public ResponseEntity<ResponseFile> getFilebyID(@PathVariable String guestId) {
		ApisResponse r = new ApisResponse<>();

		try {
			GuestProfile fileDB = gpServe.getFileByID(guestId);
			ResponseFile file = new ResponseFile();
			file.setData(fileDB.getData());
			file.setName(fileDB.getName());
			file.setType(fileDB.getType());
			file.setSize(fileDB.getData().length);
			// r.setData(file);
			return new ResponseEntity(file, HttpStatus.OK);

		} catch (Exception e) {
			r.setData(null);
			return new ResponseEntity(r, HttpStatus.OK);
		}
	}

	@GetMapping("/getImage/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		ApisResponse r = new ApisResponse();

		try {
			GuestProfile fileDB = gpServe.getFileByID(id);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
					.body(fileDB.getData());
		} catch (Exception e) {
			r.setData(null);
			return new ResponseEntity(r, HttpStatus.OK);

		}
	}

	// SecurityDeposit API's
	@PostMapping("/addSecurityDeposit")
	public ResponseEntity<Defaults> addData(@RequestBody Defaults sdepo) {

		return securityDepositService.addData(sdepo);

	}

	@GetMapping("/getSecurityDeposit")
	public ResponseEntity<List<Defaults>> getData() {
		return securityDepositService.getData();
	}

	@PutMapping("/updateSecurityDeposit/{id}")
	public ResponseEntity updateDataById(@PathVariable int id, @RequestBody Defaults sdepo) {
		return securityDepositService.updateDataById(id, sdepo);
	}

	@DeleteMapping("deleteSecurityDeposit/{id}")
	public ResponseEntity deleteDataById(@PathVariable int id) {
		return securityDepositService.deleteDataById(id);
	}

	// Get Security Deposit By Occupency Type API
	@GetMapping("/getSecurityDepositByOccupancyType/{occupancyType}")
	public ResponseEntity getSecurityDepositByOccupancyType(@PathVariable String occupancyType) {
		return securityDepositService.getSecurityDepositByOccupancyType(occupancyType);

	}

	// Api for Showing guest About to check Out .
	@GetMapping("/getGuestAboutToCheckOut/RegulatInNotice/Daily-Monthly-Active/{buildingId}")
	public List<GuestsInNotice> getAll(@PathVariable Integer buildingId,@RequestParam(value="fromDate" ,required = false)Date fromDate,
			@RequestParam(value="toDate",required=false)Date toDate) {
		List<Guest> getList = repository.findByBuildingIdAndNoticeDateBetween(buildingId,fromDate,toDate);
		List<GuestsInNotice> gin = new ArrayList<>();

		// GuestsInNotice gs=new GuestsInNotice();
		getList.forEach(g -> {
			GuestsInNotice gs = new GuestsInNotice();
			gs.setBedId(g.getBedId());
			String name = template.getForObject(
					"http://bedService/bed/getBuildingNameByBuildingId/" + g.getBuildingId(), String.class);
			gs.setBuildingName(name);
			gs.setOccupancyType(g.getOccupancyType());
			gs.setPlannedCheckOutDate(g.getPlannedCheckOutDate());
			gs.setCheckInDate(g.getCheckInDate());
			gs.setCheckOutDate(g.getCheckOutDate());
			gs.setEmail(g.getEmail());
			gs.setBedId(g.getBedId());
			gs.setNoticeDate(g.getNoticeDate());
			gs.setFirstName(g.getFirstName());
			gs.setPersonalNumber(g.getPersonalNumber());
			gs.setId(g.getId());
			gin.add(gs);
		});

		return gin;
	}

	@GetMapping("/paymentRemainder/{buildingId}")
	public ResponseEntity paymentRemainder(@PathVariable int buildingId) {
		return service.paymentRemainder(buildingId);
	}

	@GetMapping("/buildingSummaryForRat/{buildingId}/{status}")
	public ResponseEntity buildingSummaryForRat(@PathVariable Integer buildingId, @PathVariable String status) {
		return service.buildingSummaryForRat(buildingId, status);
	}

	@GetMapping("/dueGuestsList/{buildingId}")
	public ResponseEntity dueGuestsList(@PathVariable int buildingId,@RequestParam(required =false) String field) {
		return service.duesGuestsList(buildingId,field);
	}

	// @GetMapping("/getdueAllbuildings")
	// public List<DueGuestsList> getDueForAll() {
	// return pservices.getAll();
	// }

	@GetMapping("/getGuestsAllbuildings")
	public List<Guest> getGuestsForAll() {
		return repository.findAll();
	}

//new changes

	@GetMapping("/getEmailByGuestId/{id}")
	public ResponseEntity getEmailById(@PathVariable String id) {
		Guest guest = repository.getEmailById(id);
		return new ResponseEntity(guest.getEmail(), HttpStatus.OK);
	}

//-------------------SecurityDepositSetting apis ------------------
	@PostMapping("/setSecurityDeposit")
	public SecurityDeposit saveSecurityDeposit(@RequestBody SecurityDeposit security) {
		return this.sint.saveSecurityDeposit(security);
	}

	@GetMapping("/getAllconfiguredSecurityDeposit")
	public List<SecurityDeposit> getallSecuritydeposit() {
		return sint.getallSecuritydeposit();
	}

	@PutMapping("/postUpdateSecurityDeposit/{id}")
	public SecurityDeposit updateSecurityDeposit(@PathVariable int id, @RequestBody SecurityDeposit sd) {
		return this.sint.updateSecurityDeposit(id, sd);
	}

//June 30 .
//@GetMapping("/paymentRemainder/{buildingId}")
//public ResponseEntity paymentRemainder(@PathVariable int buildingId) {
//	return service.paymentRemainder(buildingId);
//}
//
//@GetMapping("/calculateDueAmount/{id}")
//public double calculateDueAmount(@PathVariable String id) {
//	return service.calculateDueAmount(id);
//}

	/* =================Notes Api's============== */

	
	

	@GetMapping("/getAllRents/{occupancyType}/{buildingId}/{sharing}")
	public ResponseEntity getAllRents(@PathVariable String occupancyType, @PathVariable int buildingId,
			@PathVariable int sharing) {
		try {

			return service.getAllRents(occupancyType, buildingId, sharing);
		} catch (Exception e) {
			System.out.println("ABCD");
		}
		return null;
	}

	// Due related Api
	@GetMapping("/calculateGuestDueByGuestId/{guestId}")
	public double calculateDueGuest(@PathVariable String guestId) {
		return dueService.calculateDueGuest(guestId);
	}

	@GetMapping("/updateDueAmount/{amountPaid}/{refundAmount}/{guestId}")
	public ResponseEntity updateDueAmount(@PathVariable double amountPaid, @PathVariable double refundAmount,
			@PathVariable String guestId) {
		return new ResponseEntity(dueService.updateDueAmount(amountPaid, refundAmount, guestId), HttpStatus.OK);
	}

	@GetMapping("/calculationForInnotice/{id}")
	public ResponseEntity calculateDueOnlyForInNoticeguy(@PathVariable String id) {
		return dueService.calculateDueForInNotice(id);
	}

	@GetMapping("/updatePackageIdInGuest")
	public ResponseEntity updatePackageIdInGuest() {
		return dueService.updatePackageIdInGuest();
	}

	@GetMapping("/getRate/{buildingId}/{occupancyType}/{price}/{roomType}")
	public ResponseEntity getpackageIdByAllTypes(@PathVariable int buildingId, @PathVariable String occupancyType,
			@PathVariable double price, @PathVariable String roomType) {
		return new ResponseEntity(
				rcr.findByBuildingIdAndOccupancyTypeAndPriceAndRoomType(buildingId, occupancyType, price, roomType),
				HttpStatus.OK);
	}

	@GetMapping("/getBedInfoForBedChange/{guestId}/{buildingId}/{bedId}")
	public ResponseEntity getBedInfoForBedChange(@PathVariable String guestId, @PathVariable int buildingId,
			@PathVariable String bedId) {
		return new ResponseEntity(service.getBedInfoForBedChange(guestId, buildingId, bedId), HttpStatus.OK);
	}

	@PutMapping("/finishBedChange/{guestId}")
	public ResponseEntity finishBedChange(@PathVariable String guestId, @RequestBody BedInfoForBedChange bedChange) {
		return new ResponseEntity(service.finishBedChange(guestId, bedChange), HttpStatus.OK);
	}

	@GetMapping("/BedChangeForDailyAndMonthly/{guestId}/{buildingId}/{bedId}")
	public ResponseEntity BedChangeForDailyAndMonthly(@PathVariable String guestId, @PathVariable int buildingId,
			@PathVariable String bedId) {
		return new ResponseEntity(service.BedChangeForDailyAndMonthly(guestId, buildingId, bedId), HttpStatus.OK);
	}

	@PutMapping("/finishBedChangeForDailyAndMonthly/{guestId}")
	public ResponseEntity finishBedChangeForDailyAndMonthly(@PathVariable String guestId,
			@RequestBody BedInfoForBedChange bedChange) {
		return new ResponseEntity(service.finishBedChangeForDailyAndMonthly(guestId, bedChange), HttpStatus.OK);
	}

	
	//http://31.187.75.117:8989/guest/dueForPlanExtensionForDandR
	// Edit due amount for guest
	@PutMapping("/updateDue/{guestId}")
	public ResponseEntity<?> editDueAmount(@PathVariable String guestId, @RequestParam Double dueAmount) {
		try {
			ApisResponse r = this.service.editDueAmount(guestId, dueAmount);
			return new ResponseEntity<ApisResponse>(r, HttpStatus.OK);
		} catch (Exception e) {
			throw new IllegalAccessError("id not found" + guestId);
		}
	}

	// api to check guest due generating correctly or not
	@GetMapping("/calculateDueForInNotice/{id}")
	public ResponseEntity calculateDueForInNotice(@PathVariable String id) {
		return new ResponseEntity(dueService.calculateDueForInNotice(id), HttpStatus.OK);
	}

	// Plan Extension api's
	@PutMapping("/dueForPlanExtensionForDandR")
	public ResponseEntity dueForPlanExtensionForDandR(@RequestBody MonthlyAndDailyPlanExtension mandDplanEx) {
		return new ResponseEntity(dueService.dueForPlanExtensionForDandR(mandDplanEx), HttpStatus.OK);
	}

	@PutMapping("/planExtensionForDandR")
	public ResponseEntity planExtensionForDandR(@RequestBody MonthlyAndDailyPlanExtension mandDplanEx) {
		return new ResponseEntity(dueService.planExtensionForDandR(mandDplanEx), HttpStatus.OK);
	}

	@GetMapping("/getFilterBedsData/{buildingId}")
	public ResponseEntity getFilterBedsData(@PathVariable int buildingId) {
		ApisResponse res = new ApisResponse();
		try {
			res.setStatus(true);
			res.setMessage(constants.FETCHING);
			res.setData(service.getFilterBedsCountInRAT(buildingId));
			return new ResponseEntity(res, HttpStatus.OK);
		} catch (Exception e) {
			res.setStatus(false);
			res.setMessage(e.getMessage());
			return new ResponseEntity(res, HttpStatus.OK);
		}
	}

	@GetMapping("/findGuestAreVacated/{guestStatus}/{buildingId}")
	public ResponseEntity<List<VacatedGuests>> findByGuestStatus(@PathVariable String guestStatus,@PathVariable Integer buildingId,
			@RequestParam(value="field" ,required = false) String field,
			@RequestParam(value="fromDate" ,required = false) Date fromDate,
			@RequestParam(value="toDate" ,required = false) Date toDate) {
			
		List<VacatedGuests> vg = service.findByGuestStatus(guestStatus,buildingId,field,fromDate,toDate);
		return new ResponseEntity<List<VacatedGuests>>(vg,HttpStatus.OK);
	}

	@GetMapping("/files")
	public Stream<GuestProfile> getAllFiles() {
		return gpServe.getAllFiles();
	}

}
