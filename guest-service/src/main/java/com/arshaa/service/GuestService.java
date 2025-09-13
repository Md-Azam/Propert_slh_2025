package com.arshaa.service;

import com.arshaa.common.CheckOutIntiated;
import com.arshaa.common.CheckoutResponse;
import com.arshaa.common.FinalCheckOutConfimation;
import com.arshaa.common.GuestName;
import com.arshaa.common.InitiateCheckoutByGuestId;
import com.arshaa.common.InitiatingCheckOut;
import com.arshaa.common.OnboardingConfirmation;
import com.arshaa.model.EmailResponse;
import com.arshaa.model.FilterBedsCountInRAT;
import com.arshaa.model.InnoticeToRegular;
import com.arshaa.common.Bed;
import com.arshaa.common.UpdateGuestDetails;
import com.arshaa.dtos.CheckOutConfirmation;
import com.arshaa.dtos.ForceCheckOutMsg;
import com.arshaa.dtos.GuestData;
import com.arshaa.dtos.GuestDto;
import com.arshaa.dtos.RatedDto;
import com.arshaa.entity.Defaults;
import com.arshaa.entity.Guest;
import com.arshaa.entity.Payments;
import com.arshaa.entity.RatesConfig;
import com.arshaa.exception.ResourceNotFoundException;
import com.arshaa.model.ApisResponse;
import com.arshaa.model.AvailableBedsResponse;
import com.arshaa.model.BedInfoForBedChange;
import com.arshaa.model.BedsCount;
import com.arshaa.model.BuildingSummary;
import com.arshaa.model.DueGuestsList;
import com.arshaa.model.PaymentRemainder;
import com.arshaa.model.PreviousGuests;
import com.arshaa.model.VacatedGuests;
import com.arshaa.repository.GuestProfileRepo;
import com.arshaa.repository.GuestRepository;
import com.arshaa.repository.ImageRepository;
import com.arshaa.repository.PayRepos;
import com.arshaa.repository.RatesConfigRepository;
import com.arshaa.repository.SecurityDepositRepo;
import com.arshaa.util.DateFormates;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Optional;

@Slf4j
@Service
public class GuestService implements GuestInterface {
	
	@Value("${project.imageUrl}")
	private String path;

	@Autowired
	private PaymentService payServ;

	@Autowired
	private SecurityDepositRepo sceurityR;

	@Autowired
	private PayRepos payRepo;
	@Autowired
	private RatesConfigRepository rconfig;
	@Autowired(required = true)
	private GuestRepository repository;
	@Autowired
	@Lazy
	private RestTemplate template;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private GuestProfileRepo guestProfileRepo;
	@Autowired
	private DueCalculateService dueService;
	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private SecurityDepositRepo securityDepositRepo;

	private static final String BED_INFO_FOR_CHANGEBED = "http://bedService/bed/getBedInformation/";
	private static final String bedUriForMakeBedAvailable = "http://bedservice/bed/updateBedStatusByGuestId/";
	private static final String bedUriForMakeBedOccupied = "http://bedService/bed/updateBedStatusBydBedId";
	private static final String AVAILABLE_BEDS_COUNT = "http://bedService/bed/getAvailableBedsCount/";

	@Override
	public List<GuestDto> getGuests(String field) {
		List<Guest> getGuest = repository.findAll(Sort.by(Sort.Direction.DESC, field));
		List<GuestDto> gdto = new ArrayList<>();

		getGuest.forEach(s -> {
			GuestDto d = new GuestDto();
			d.setAadharNumber(s.getAadharNumber());
			d.setBedId(s.getBedId());
			d.setBuildingId(s.getBuildingId());
			d.setGuestName(s.getFirstName());
			d.setAmountPaid(s.getAmountPaid());
			d.setBuildingId(s.getBuildingId());
			String name = template.getForObject(
					"http://bedService/bed/getBuildingNameByBuildingId/" + s.getBuildingId(), String.class);
			d.setBuildingName(name);
			d.setPersonalNumber(s.getPersonalNumber());
			d.setCheckInDate(s.getCheckInDate());
			d.setCheckOutDate(s.getCheckOutDate());
			d.setAddressLine1(s.getAddressLine1());
			d.setAddressLine2(s.getAddressLine2());
			d.setId(s.getId());
			d.setDefaultRent(s.getDefaultRent());
			gdto.add(d);

		});
		return gdto;
	}

	public ResponseEntity getAllRents(String occupancyType, int buildingId, int sharing) {
		RatesConfig rc = rconfig.findByOccupancyTypeAndBuildingIdAndSharing(occupancyType, buildingId, sharing);
		return new ResponseEntity(rc, HttpStatus.OK);
	}

	@Override
	public GuestData getAllGuestData(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Guest> pagePost = this.repository.findAll(p);
		List<Guest> allPosts = pagePost.getContent();
		List<GuestDto> data = new ArrayList<>();

		pagePost.forEach(s -> {
			GuestDto d = new GuestDto();
			d.setAadharNumber(s.getAadharNumber());
			d.setBedId(s.getBedId());
			d.setBuildingId(s.getBuildingId());
			d.setGuestName(s.getFirstName());
			d.setAmountPaid(s.getAmountPaid());
			d.setBuildingId(s.getBuildingId());
			String name = template.getForObject(
					"http://bedService/bed/getBuildingNameByBuildingId/" + s.getBuildingId(), String.class);
			d.setBuildingName(name);
			d.setPersonalNumber(s.getPersonalNumber());
			d.setCheckInDate(s.getCheckInDate());
			d.setCheckOutDate(s.getCheckOutDate());
			d.setAddressLine1(s.getAddressLine1());
			d.setAddressLine2(s.getAddressLine2());
			d.setId(s.getId());
			d.setDefaultRent(s.getDefaultRent());
			data.add(d);

		});
		GuestData postResponse = new GuestData();

		postResponse.setContent(data);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());

		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public Guest getGuestById(String guestId) {
		Guest getGuest = repository.findById(guestId).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
		return getGuest;
	}

	public Guest addGuest(Guest guest) {
		String bedUri = "http://bedService/bed/updateBedStatusBydBedId";
		String mailUri = "http://emailService/mail/sendOnboardingConfirmation";
		System.out.println("adding guest call starts ........");
		ArrayList<String> ustatus = new ArrayList<>();
		ustatus.add("Active");
		ustatus.add("InNotice");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		java.sql.Date tSqlDate = new java.sql.Date(guest.getTransactionDate().getTime());
		guest.setTransactionDate(tSqlDate);
		java.sql.Date createDate = new java.sql.Date(guest.getCreatedOn().getTime());
		guest.setCreatedOn(createDate);
		if (guest.getBuildingId() == 0 || guest.getBedId() == null) {
			Guest g = null;
			return g;
		} else if (repository.existsByBedIdAndGuestStatusIn(guest.getBedId(), ustatus)) {
			Guest g = null;
			return g;
		} else {
			System.out.println("saving guest before payments guest call starts ........");
			repository.save(guest);

			if (guest.getOccupancyType().equalsIgnoreCase("Daily")) {
				java.util.Date m = guest.getCheckInDate();
				java.sql.Date ms = new java.sql.Date(m.getTime());
				Calendar cal = Calendar.getInstance();
				cal.setTime(m);
				cal.add(Calendar.DATE, guest.getDuration());
				m = cal.getTime();
				log.info("ms: " + ms);
				guest.setPlannedCheckOutDate(m);
				guest.setGuestStatus("Active");
				repository.save(guest);
			} else if (guest.getOccupancyType().equalsIgnoreCase("OneMonth")) {
				guest.setDuration(1);
				System.out.println("adding guest call fro one Months starts ........");
				repository.save(guest);
				log.info("real checkInDate {}", guest.getCheckInDate());
				ZonedDateTime chekedInDate = DateFormates.sqlToZoned(guest.getCheckInDate());
				log.info("chekedInDate converted {}", chekedInDate);
				Long lastDayOfMonth = 0L;
				lastDayOfMonth = DateFormates.lastDayOfMonth(chekedInDate);
				log.info("lastDay converted {}", lastDayOfMonth);
				ZonedDateTime plannedDate = chekedInDate.plusDays(lastDayOfMonth);
				log.info("converted planned checkout date {}", plannedDate);
				java.util.Date finalplannedDate = DateFormates.ZonedToSql(plannedDate);
				log.info("finalplannedDate {}", finalplannedDate);
				guest.setPlannedCheckOutDate(finalplannedDate);
				guest.setGuestStatus("Active");
				repository.save(guest);
			} else {

				// setting chekindate as lastbillgeneration date for the first time
				// generate bill generated till
				System.out.println("adding guest call regular customer ........");
				java.util.Date m = guest.getCheckInDate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(m);
				int res = cal.getActualMaximum(Calendar.DATE);
				cal.add(Calendar.DATE, res);
				m = cal.getTime();
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
				String formattedDate = formatter1.format(m);
				java.sql.Date sqlDate1 = java.sql.Date.valueOf(formattedDate);

				java.sql.Date lbgd = java.sql.Date.valueOf(formatter1.format(guest.getCheckInDate()));
				guest.setLastBillGenerationDate(lbgd);

				guest.setBillGeneratedTill(sqlDate1);
				guest.setGuestStatus("active");

				repository.save(guest);
			}
			guest.setGuestStatus("active");

			repository.save(guest);
			System.out.println(guest.getDueAmount());
			Bed bedReq = new Bed();
			Payments payReq = new Payments();
			// bed setting
			bedReq.setBedId(guest.getBedId());

			bedReq.setGuestId(guest.getId());
			template.put(bedUri, bedReq, Bed.class);
			// payment setting
			payReq.setGuestId(guest.getId());
			payReq.setBuildingId(guest.getBuildingId());
			payReq.setTransactionId(guest.getTransactionId());
			payReq.setOccupancyType(guest.getOccupancyType());
			payReq.setTransactionDate(guest.getTransactionDate());
			// payReq.setCheckinDate(cSqlDate);
			payReq.setAmountPaid(guest.getAmountPaid());
			payReq.setPaymentPurpose(guest.getPaymentPurpose());
			repository.save(guest);
			payServ.addPayment(payReq);

			OnboardingConfirmation mail = new OnboardingConfirmation();
			mail.setName(guest.getFirstName());
			mail.setAmountPaid(guest.getAmountPaid());
			String name = template.getForObject(
					"http://bedService/bed/getBuildingNameByBuildingId/" + guest.getBuildingId(), String.class);
			mail.setBuildingName(name + " " + "GuestId: " + guest.getId());
			mail.setBedId(guest.getBedId());
			mail.setEmail(guest.getEmail());
			OnboardingConfirmation res = template.postForObject(mailUri, mail, OnboardingConfirmation.class);

			return guest;
		}

	}

	@Override
	public double updateGuest(Guest guest) {
		Guest newGuest = repository.findById(guest.getId()).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
		newGuest.setDueAmount(guest.getDueAmount());
		repository.save(newGuest);
		return newGuest.getDueAmount();
	}

	@Override
	public void deleteGuest(String guestId) {
		Guest deleteGuest = repository.findById(guestId).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
		repository.delete(deleteGuest);
	}

	// Method to fetch all the dueamount for dashboard .
	@SuppressWarnings("unchecked")
	@Override
	public long getTotalDue() {
		if (Objects.isNull(repository.getTotalDueByAllGuests()))
			return 0;
		else
			return repository.getTotalDueByAllGuests();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApisResponse getFinalDueAmountById(String id) {
		String maila = "http://emailService/mail/guestCheckOutNotification";
		String updateBedStatusURL = "http://bedservice/bed/updateBedStatusByGuestId/";
		Guest getGuest = repository.findById(id).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
		ApisResponse apiResponse = new ApisResponse();
		ForceCheckOutMsg fc = new ForceCheckOutMsg();
		try {
			template.getForObject(updateBedStatusURL + id, String.class);
			// generate current date
			if (getGuest.getDueAmount() <= 0) {
				long millis = System.currentTimeMillis();
				java.sql.Date date = new java.sql.Date(millis);
				getGuest.setGuestStatus("VACATED");
				getGuest.setCheckOutDate(date);
				getGuest.setDueAmount(0.0);
				repository.save(getGuest);
				guestProfileRepo.deleteByGuestId(id);
				CheckOutConfirmation mail = new CheckOutConfirmation();
				mail.setName(getGuest.getFirstName());
				mail.setNoticeDate(getGuest.getNoticeDate());
				mail.setCheckOutDate(getGuest.getCheckOutDate());
				String name = template.getForObject(
						"http://bedService/bed/getBuildingNameByBuildingId/" + getGuest.getBuildingId(), String.class);
				mail.setBuildingName(name);
				mail.setBedId(getGuest.getBedId());
				mail.setEmail(getGuest.getEmail());
				CheckOutConfirmation res = template.postForObject(maila, mail, CheckOutConfirmation.class);
				apiResponse.setStatus(true);
				apiResponse.setMessage("guest checked out successfully");
				apiResponse.setData(getGuest);
				return apiResponse;
			} else {
				apiResponse.setStatus(true);
				apiResponse.setMessage("Please send valid data");
				return apiResponse;
			}
		} catch (Exception e) {
			apiResponse.setMessage("use not found");
			apiResponse.setStatus(false);
			return apiResponse;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public double getOnlyDues(String id) {
		// TODO Auto-generated method stub
		Guest guest = repository.findById(id).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
		return guest.getDueAmount();
	}

	@Override
	public List<VacatedGuests> findByGuestStatus(String guestStatus, Integer buildingId, String field,
			java.sql.Date fromDate, java.sql.Date toDate) {
		Guest guest = new Guest();
//		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		List<Guest> getList = repository.findAllByGuestStatusAndBuildingIdAndCheckOutDateBetween(guestStatus,
				buildingId, Sort.by(Sort.Direction.DESC, field), fromDate, toDate);
		List<Integer> buildingIdList = getList.stream().map(x -> x.getBuildingId()).collect(Collectors.toList());
		Map<String, String> map = template.postForObject("http://bedService/bed/buildingsNames", guest, Map.class);
		List<VacatedGuests> vacatedGuests = new ArrayList<>();
		getList.forEach(g -> {
			VacatedGuests gs = new VacatedGuests();
			log.info(guestStatus);
			gs.setBedId(g.getBedId());
			gs.setBuildingName(map.get(String.valueOf(g.getBuildingId())));
			gs.setCheckOutDate(g.getCheckOutDate());
			gs.setEmail(g.getEmail());
			gs.setBedId(g.getBedId());
			gs.setFirstName(g.getFirstName());
			gs.setPersonalNumber(g.getPersonalNumber());
			gs.setId(g.getId());
			gs.setGuestStatus(g.getGuestStatus());
			vacatedGuests.add(gs);
		});

		return vacatedGuests;
	}

	public ResponseEntity paymentRemainder(int buildingId) {
		String url = "http://emailService/mail/sendPaymentRemainder/";
		List<PaymentRemainder> getList = new ArrayList();
		List<EmailResponse> getRes = new ArrayList<>();
		List<Guest> getGuest = repository.getByBuildingId(buildingId);
		System.out.println("List:" + getGuest);

		if (!getGuest.isEmpty()) {
			getGuest.forEach(g -> {
				String ss = g.getOccupancyType();
				boolean s = "Regular".contentEquals(ss);
				log.info("s" + s);
				if (s == true) {
					PaymentRemainder pr = new PaymentRemainder();
					if ((g.getDueAmount()) > 0) {
						pr.setDueAmount(g.getDueAmount());
						pr.setEmail(g.getEmail());
						pr.setGuestId(g.getId());
						pr.setName(g.getFirstName());
						EmailResponse parRes = template.postForObject(url, pr, EmailResponse.class);
						EmailResponse er = new EmailResponse();
						er.setStatus(parRes.isStatus());
						er.setMessage(parRes.getMessage());
						getRes.add(er);
						log.info("List of guestis {}" + getList);
					}

				}
			});

			return new ResponseEntity(getRes, HttpStatus.OK);
		} else {
			return new ResponseEntity("Nodue", HttpStatus.OK);
		}
	}

	public ResponseEntity duesGuestsList(int buildingId, String field) {

		List<DueGuestsList> getList = new ArrayList();

		if (buildingId == 0) {
			List<Guest> getGuest = repository.findAll(Sort.by(Sort.Direction.DESC, field));
			System.out.println("List:" + getGuest);

			if (!getGuest.isEmpty()) {
				getGuest.forEach(g -> {
					String ss = g.getOccupancyType();
					boolean s = "Regular".contentEquals(ss);
					String sts = g.getGuestStatus();
					boolean st = "Active".contentEquals(sts);
					System.out.println("s" + s);
					if (s == true) {
						if (g.getOccupancyType().equalsIgnoreCase("Regular")) {
							DueGuestsList pr = new DueGuestsList();

							if ((g.getDueAmount()) > 1 && g.getGuestStatus().equalsIgnoreCase("Active")) {
								java.sql.Date dueDate = g.getLastBillGenerationDate();
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
								LocalDate finaldueDate = dueDate.toLocalDate();
								String std = formatter.format(finaldueDate);
								pr.setDueAmount(g.getDueAmount());
								pr.setEmail(g.getEmail());
								pr.setGuestId(g.getId());
								pr.setBillGeneratedTill(std);
								pr.setGuestName(g.getFirstName());
								pr.setPhoneNumber(g.getPersonalNumber());
								pr.setBedId(g.getBedId());
								String name = template.getForObject(
										"http://bedService/bed/getBuildingNameByBuildingId/" + g.getBuildingId(),
										String.class);

								pr.setBuildingName(name);
								getList.add(pr);

								log.info("list of guest {}", getList);
							}

						}
					}
				});

				return new ResponseEntity(getList, HttpStatus.OK);
			} else {
				return new ResponseEntity(getList, HttpStatus.OK);
			}
		} else {
			List<Guest> getGuest = repository.getByBuildingId(buildingId);
			System.out.println("List:" + getGuest);

			if (!getGuest.isEmpty()) {
				getGuest.forEach(g -> {
					String ss = g.getOccupancyType();
					boolean s = "Regular".contentEquals(ss);
					log.info("s", s);
					String sts = g.getGuestStatus();
					boolean st = "Active".contentEquals(sts);
					if (g.getOccupancyType().equalsIgnoreCase("Regular")) {

						DueGuestsList pr = new DueGuestsList();
						if (st = true) {
							if ((g.getDueAmount()) > 1 && g.getGuestStatus().equalsIgnoreCase("Active")) {
								log.info(g.getId());
								java.sql.Date dueDate = g.getLastBillGenerationDate();
								log.info("dueData {}", dueDate);
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
								LocalDate finaldueDate = dueDate.toLocalDate();
								String std = formatter.format(finaldueDate);
								pr.setDueAmount(g.getDueAmount());
								pr.setEmail(g.getEmail());
								pr.setGuestId(g.getId());
								pr.setBillGeneratedTill(std);
								pr.setGuestName(g.getFirstName());
								pr.setPhoneNumber(g.getPersonalNumber());
								pr.setBedId(g.getBedId());
								String name = template.getForObject(
										"http://bedService/bed/getBuildingNameByBuildingId/" + g.getBuildingId(),
										String.class);

								pr.setBuildingName(name);
								getList.add(pr);

								log.info("list", getList);
							}
						}
					}
				});

				return new ResponseEntity(getList, HttpStatus.OK);
			} else {
				return new ResponseEntity(getList, HttpStatus.OK);

			}
		}

	}

	@Override
	public List<RatesConfig> getByBuildingId(int buildingId) {
		return rconfig.findByBuildingId(buildingId);
	}

	@Override
	public List<RatesConfig> findByBuildingIdAndOccupancyType(int buildingId, String occupancyType) {

		return rconfig.findByBuildingIdAndOccupancyType(buildingId, occupancyType);
	}

	@Override
	public List<RatesConfig> findByOccupancyType(String occupancyType) {

		return rconfig.findByOccupancyType(occupancyType);
	}

	@Override
	public ResponseEntity updateGuestDetails(UpdateGuestDetails editGuest, String id) {
		Guest guest = repository.findById(id).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
		{
			guest.setId(id);
			guest.setFirstName(editGuest.getFirstName());
			guest.setEmail(editGuest.getEmail());
			guest.setPersonalNumber(editGuest.getPersonalNumber());
			guest.setAadharNumber(editGuest.getAadharNumber());
			guest.setAddressLine1(editGuest.getAddressLine1());
			guest.setDateOfBirth(editGuest.getDateOfBirth());
			guest.setGender(editGuest.getGender());
			guest.setVehicleNo(editGuest.getVehicleNo());

			guest.setPincode(editGuest.getPincode());
			guest.setState(editGuest.getState());
			guest.setCity(editGuest.getCity());

			return new ResponseEntity(repository.save(guest), HttpStatus.OK);
		}
	}

	// Initiate checkout api it will calculate due for InNotice
	@Override
	public ResponseEntity GuestCheckoutBody(InitiateCheckoutByGuestId gcb, String id) {
		String mailUri = "http://emailService/mail/sentInitiatingCheckOutRemainder/";

		try {
			Guest guest = repository.findById(id).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
			guest.setId(id);
			guest.setNoticeDate(gcb.getNoticeDate());
			guest.setPreviousDues(guest.getDueAmount());
			// guest.setPlannedCheckOutDate(gcb.getPlannedCheckOutDate());
			// guest.setOccupancyType(gcb.getOccupancyType());
			dueService.calculateDueForInNotice(id);
			guest.setGuestStatus("InNotice");

			repository.save(guest);
			InitiatingCheckOut mail = new InitiatingCheckOut();
			mail.setName(guest.getFirstName());
			mail.setNoticeDate(guest.getNoticeDate());
			mail.setPlannedCheckOutDate(guest.getPlannedCheckOutDate());
			String name = template.getForObject(
					"http://bedService/bed/getBuildingNameByBuildingId/" + guest.getBuildingId(), String.class);
			mail.setBuildingName(name);
			mail.setBedId(guest.getBedId());
			mail.setEmail(guest.getEmail());
			InitiatingCheckOut res = template.postForObject(mailUri, mail, InitiatingCheckOut.class);

			return new ResponseEntity("Checkout intiated successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.OK);
		}

	}

	@Override
	public RatesConfig updateRoomRent(RatedDto Rdto, Integer id) {
		try {
			RatesConfig r = rconfig.getById(id);
			r.setPrice(Rdto.getPrice());
			log.info("new price {}", r.getPrice());

			RatesConfig rc = rconfig.save(r);

			log.info("new price {}", rc);
			if (rc.isDuplicatePackage() == true) {
				Guest guest = repository.findByPackageId(id);
				guest.setDefaultRent(r.getPrice());
				log.info("new default rent {}", guest.getDefaultRent());
				repository.save(guest);
			} else {
				List<Guest> updateListofPackage = repository.findAllByPackageId(id);
				updateListofPackage.forEach(updatePackage -> updatePackage.setDefaultRent(rc.getPrice()));
				repository.saveAll(updateListofPackage);
				log.info("list of package updated in  guest {}");

			}
			return rc;
		} catch (Exception e) {
			return new RatesConfig();
		}
	}

	@Override
	public GuestName getNameAndBed(String id) {
		Guest guest = repository.findById(id).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
		GuestName gn = new GuestName();
		if (guest.getId().isEmpty())
			return null;
		gn.setBedId(guest.getBedId());
		gn.setGuestName(guest.getFirstName());
		return gn;
	}

	@Override
	public ApisResponse changeRegularFromInNotice(String guestId) {
		ApisResponse response = new ApisResponse();
		// LocalDate ld = LocalDate.of( 2023 , 2 , 5 );
		Guest guest = repository.findById(guestId).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
		try {
			if (guest.getGuestStatus().equalsIgnoreCase("InNotice")) {
				Defaults def = securityDepositRepo.findByOccupancyType(guest.getOccupancyType());
				guest.setGuestStatus("active");
				guest.setNoticeDate(null);
				Double previousGuestDue = guest.getDueAmount();
				java.util.Date plannedCheckOutDate = guest.getPlannedCheckOutDate();
				java.sql.Date billGeneratedTillDate = guest.getBillGeneratedTill();
				java.util.Date utilDateBillTill = new java.util.Date(billGeneratedTillDate.getTime());
				Double differeneceDays = 0.0;
				Double extraDaysCharge = 0.0;
				Double amountShouldUndo = 0.0;

				Double amountShouldAdd = 0.0;
				Double dueRent = 0.0;
				Double maintenanceCharge = Double.valueOf(def.getMaintainanceCharge());
				Double differenece = Double.valueOf(ChronoUnit.DAYS.between(
						DateFormates.dateToInstant(plannedCheckOutDate), DateFormates.dateToInstant(utilDateBillTill)));
				log.info("d1", differenece);
				differeneceDays = (differenece < 0) ? (-1 * differenece) : differenece;
				extraDaysCharge = (guest.getDefaultRent() / 30) * differeneceDays;
				if (LocalDate.now().isAfter(DateFormates.dateToLocalDate(utilDateBillTill))
						|| LocalDate.now().equals(DateFormates.dateToLocalDate(utilDateBillTill))) {
					dueRent = guest.getDefaultRent();

					Calendar cal = Calendar.getInstance();
					cal.setTime(billGeneratedTillDate);
					cal.add(Calendar.DATE, 30);
					java.util.Date billGeneratedTill = cal.getTime();
					guest.setLastBillGenerationDate(guest.getBillGeneratedTill());
					guest.setBillGeneratedTill(DateFormates.utilToSql(utilDateBillTill));

				}
				if (DateFormates.dateToLocalDate(plannedCheckOutDate)
						.isAfter(DateFormates.dateToLocalDate(utilDateBillTill))) {
					amountShouldUndo = extraDaysCharge + maintenanceCharge;
					amountShouldAdd = guest.getSecurityDeposit();

				} else if (DateFormates.dateToLocalDate(plannedCheckOutDate)
						.isBefore(DateFormates.dateToLocalDate(utilDateBillTill))) {
					amountShouldUndo = maintenanceCharge;
					amountShouldAdd = guest.getSecurityDeposit() + extraDaysCharge;
				} else {
					amountShouldUndo = maintenanceCharge;
					amountShouldAdd = guest.getSecurityDeposit();
				}
				Double calculateDue = (previousGuestDue - amountShouldUndo) + amountShouldAdd + dueRent;
				guest.setDueAmount(calculateDue);
				guest.setPlannedCheckOutDate(null);
				guest.setCancelCheckOutDate(new java.util.Date());
				guest.setInNoticeDue(0.0);
				Guest updateGuest = this.repository.save(guest);
				response.setData(updateGuest);
				response.setStatus(true);
				response.setMessage("Cancel checkout Successfull");
				return response;
			} else {
				response.setStatus(false);
				response.setMessage("Guest should be inNotice");
				return response;
			}
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	@Override
	public ResponseEntity buildingSummaryForRat(Integer buildingId, String status) {
		BuildingSummary bsummary = new BuildingSummary();
		try {
			long guest = repository.findByBuildingIdAndGuestStatus(buildingId, status).get().size();
			System.out.println(guest);
			List<String> guestStatus = new ArrayList<>();
			guestStatus.add("Active");
			guestStatus.add("InNotice");
			List<Guest> getAllGuests = repository.findByBuildingIdAndGuestStatusIn(buildingId, guestStatus);

			List<Guest> dueGuestsList = new ArrayList<>();
			getAllGuests.forEach(item -> {
				if (item.getDueAmount() > 0) {
					dueGuestsList.add(item);
				}
			});
			long dueGuestscount = dueGuestsList.size();
			double totalDueofBuilding = 0;

			if (Objects.isNull(repository.getGuestTotalDueByBuildingId(buildingId)))
				totalDueofBuilding = 0.0;
			else
				totalDueofBuilding = repository.getGuestTotalDueByBuildingId(buildingId);
			bsummary.setGuestsInNotice(guest);
			bsummary.setGuestsOnDue(dueGuestscount);
			bsummary.setTotalDueAmount(totalDueofBuilding);
			return new ResponseEntity(bsummary, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.OK);
		}

	}

	@Override
	public long getDueGuestsCount() {
		List<String> status = new ArrayList<>();
		status.add("Active");
		status.add("InNotice");
		List<Guest> guestData = repository.findByGuestStatusIn(status);
		List<Guest> dueGuests = new ArrayList<>();
		long count = 0;
		guestData.forEach(g -> {

			if (g.getDueAmount() > 1) {
				dueGuests.add(g);
			}
		});
		count = dueGuests.size();
		return count;
	}

	@Override
	public ApisResponse getBedInfoForBedChange(String guestId, int buildingId, String bedId) {
		ApisResponse response = new ApisResponse();

		try {
			BedInfoForBedChange bedInfoForBedChange = new BedInfoForBedChange();
			Guest guest = repository.findById(guestId).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
			if (guest.getOccupancyType().equalsIgnoreCase("Regular")
					&& guest.getGuestStatus().equalsIgnoreCase("active")) {
				double due = 0;
				BedInfoForBedChange bedinfo = template.getForObject(
						BED_INFO_FOR_CHANGEBED + guest.getBedId() + "/" + buildingId, BedInfoForBedChange.class);
				double price = rconfig
						.findByBuildingIdAndOccupancyTypeAndRoomTypeAndSharingAndDuplicatePackage(buildingId,
								guest.getOccupancyType(), bedinfo.getRoomType(), bedinfo.getSharing(), false)
						.getPrice();
				bedInfoForBedChange.setBedId(bedId);
				log.info("bed Id {}", bedId);
				bedInfoForBedChange.setDefaultRent(price);
				bedInfoForBedChange.setOccupancyType(guest.getOccupancyType());
				bedInfoForBedChange.setRoomType(bedinfo.getRoomType());
				bedInfoForBedChange.setSharing(bedinfo.getSharing());
				bedInfoForBedChange.setGuestId(guestId);
				bedInfoForBedChange.setBuildingId(buildingId);
				bedInfoForBedChange.setBuildingName(bedinfo.getBuildingName());

				due = dueService.calculateDueForRegularPlandBedChange(guestId);
				log.info("due before {}", due);
				bedInfoForBedChange.setDueAmount(due);
				log.info("due before1 {}", due);
				guest.setDueAmount(due);
				log.info("due before2 {}", due);
				repository.save(guest);
				response.setStatus(true);
				response.setMessage("Fetching Bed Information");
				response.setData(bedInfoForBedChange);
				return response;
			} else {
				response.setStatus(true);
				response.setMessage("Please send valid data");
				return response;
			}
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage(e.getMessage());
			return response;
		}

	}

	@Override
	public ApisResponse finishBedChange(String guestId, BedInfoForBedChange bedChange) {

		ApisResponse response = new ApisResponse();
		try {
			Guest guest = repository.findById(guestId).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
			if (guest.getOccupancyType().equalsIgnoreCase("Regular")
					&& guest.getGuestStatus().equalsIgnoreCase("active")) {
				ZonedDateTime currentDate = ZonedDateTime.now();
				java.sql.Date convertedBillGeneratedTill = DateFormates
						.ZonedToSql(currentDate.plusDays(DateFormates.lastDayOfMonth(currentDate)));
				log.info("converted bill generated till{}", convertedBillGeneratedTill);
				int rcconfigId = rconfig.findByBuildingIdAndOccupancyTypeAndRoomTypeAndSharingAndDuplicatePackage(
						bedChange.getBuildingId(), guest.getOccupancyType(), bedChange.getRoomType(),
						bedChange.getSharing(), false).getId();
				guest.setPackageId(rcconfigId);
				guest.setPreviousBedId(guest.getBedId());
				guest.setBillGeneratedTill(convertedBillGeneratedTill);
				guest.setLastBillGenerationDate(DateFormates.ZonedToSql(currentDate));
				guest.setDefaultRent(bedChange.getDefaultRent());
				guest.setBedId(bedChange.getBedId());
				guest.setPreviousDefaultRent(guest.getDefaultRent());
				guest.setBuildingId(bedChange.getBuildingId());
				guest.setDueAmount(bedChange.getDueAmount() + guest.getDefaultRent());
				log.info("due {}", bedChange.getDueAmount() + guest.getDefaultRent());
				repository.save(guest);
				log.info("Guest {}", guestId);
				template.getForObject(bedUriForMakeBedAvailable + guestId, CheckoutResponse.class);
				Bed bedReq = new Bed();
				bedReq.setBedId(bedChange.getBedId());
				bedReq.setGuestId(guest.getId());
				template.put(bedUriForMakeBedOccupied, bedReq);
				bedChange.setBillGeneratedTill(convertedBillGeneratedTill);
				bedChange.setLastBillGenerationDate(DateFormates.ZonedToSql(currentDate));
				response.setStatus(true);
				response.setData(bedChange);
				return response;
			} else {
				response.setStatus(true);
				response.setMessage("Please send valid data");
				return response;
			}
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage(e.getMessage());
			return response;
		}

	}

	@Override
	public ApisResponse BedChangeForDailyAndMonthly(String guestId, int buildingId, String bedId) {
		ApisResponse response = new ApisResponse();
		try {
			Guest guest = repository.findById(guestId).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
			if (guest.getOccupancyType().equalsIgnoreCase("OneMonth")
					|| guest.getOccupancyType().equalsIgnoreCase("daily")
							&& guest.getGuestStatus().equalsIgnoreCase("active")) {
				BedInfoForBedChange bedInfoForBedChange = new BedInfoForBedChange();
				log.info("bedInfoForBedChange {}", bedInfoForBedChange);

				double due = 0;
				BedInfoForBedChange bedinfo = template.getForObject(
						BED_INFO_FOR_CHANGEBED + guest.getBedId() + "/" + buildingId, BedInfoForBedChange.class);
				double price = rconfig
						.findByBuildingIdAndOccupancyTypeAndRoomTypeAndSharingAndDuplicatePackage(buildingId,
								guest.getOccupancyType(), bedinfo.getRoomType(), bedinfo.getSharing(), false)
						.getPrice();
				bedInfoForBedChange.setBedId(bedId);
				bedInfoForBedChange.setDefaultRent(price);
				bedInfoForBedChange.setOccupancyType(guest.getOccupancyType());
				bedInfoForBedChange.setRoomType(bedinfo.getRoomType());
				bedInfoForBedChange.setSharing(bedinfo.getSharing());
				bedInfoForBedChange.setGuestId(guestId);
				bedInfoForBedChange.setBuildingId(buildingId);
				bedInfoForBedChange.setBuildingName(bedinfo.getBuildingName());
				due = dueService.calculateDueForMonthlyAndDailyPlandBedChange(guestId);
				log.info("monthly bed change due {}", due);
				bedInfoForBedChange.setDueAmount(due);
				response.setStatus(true);
				response.setMessage("Fetching Bed Information");
				response.setData(bedInfoForBedChange);
				return response;
			} else {
				response.setStatus(true);
				response.setMessage("Please send valid data");
				return response;
			}

		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage(e.getMessage());
			return response;
		}

	}

	@Override
	public ApisResponse finishBedChangeForDailyAndMonthly(String guestId, BedInfoForBedChange bedChange) {
		ApisResponse response = new ApisResponse();
		try {

			Guest guest = repository.findById(guestId).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
			if (guest.getOccupancyType().equalsIgnoreCase("OneMonth")
					|| guest.getOccupancyType().equalsIgnoreCase("daily")
							&& guest.getGuestStatus().equalsIgnoreCase("active")) {
				int rcconfigId = rconfig.findByBuildingIdAndOccupancyTypeAndRoomTypeAndSharingAndDuplicatePackage(
						bedChange.getBuildingId(), guest.getOccupancyType(), bedChange.getRoomType(),
						bedChange.getSharing(), false).getId();
				guest.setPackageId(rcconfigId);
				guest.setDefaultRent(bedChange.getDefaultRent());
				guest.setBedId(bedChange.getBedId());
				guest.setPreviousDefaultRent(guest.getDefaultRent());
				guest.setBuildingId(bedChange.getBuildingId());
				guest.setDueAmount(bedChange.getDueAmount());
				repository.save(guest);
				log.info("Guest", guestId);
				CheckoutResponse cRes = template.getForObject(bedUriForMakeBedAvailable + guestId,
						CheckoutResponse.class);
				Bed bedReq = new Bed();
				bedReq.setBedId(bedChange.getBedId());
				bedReq.setGuestId(guest.getId());
				template.put(this.bedUriForMakeBedOccupied, bedReq);
				response.setStatus(true);
				response.setMessage("Bed changed successfully");
				response.setData(bedChange);
				return response;
			} else {
				response.setStatus(true);
				response.setMessage("Please send valid data");
				return response;
			}
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage(e.getMessage());
			return response;
		}

	}

	@Override
	public ApisResponse editDueAmount(String guestId, double dueAmount) {
		ApisResponse response = new ApisResponse();

		try {
			Guest guest = repository.findById(guestId).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
			if (guest.getId() != null)
				guest.setId(guestId);
			guest.setDueAmount(dueAmount);
			repository.save(guest);
			response.setStatus(true);
			response.setMessage("due updated successfully");
			response.setData(guest);
			return response;
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage(e.getMessage());
			return response;
		}

	}

	@Override
	public ApisResponse forceCheckOut(ForceCheckOutMsg checkOutConfirmation, String id) {
		String maila = "http://emailService/mail/guestCheckOutNotification";
		String updateBedStatusURL = "http://bedservice/bed/updateBedStatusByGuestId/";

		ApisResponse response = new ApisResponse();
		try {
			Guest getGuest = repository.findById(id).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
			ForceCheckOutMsg fc = new ForceCheckOutMsg();
			if (getGuest.getDueAmount() > 0) {
				long millis1 = System.currentTimeMillis();
				java.sql.Date date1 = new java.sql.Date(millis1);
				getGuest.setCheckOutExceptionalDue(checkOutConfirmation.getCheckOutExceptionalDue());
				getGuest.setCheckOutExceptionalMessage(checkOutConfirmation.getCheckOutExceptionalMessage());
				getGuest.setGuestStatus("VACATED");
				getGuest.setCheckOutDate(date1);
				repository.save(getGuest);
				template.getForObject(updateBedStatusURL + id, String.class);
				guestProfileRepo.deleteByGuestId(id);
				CheckOutConfirmation mail = new CheckOutConfirmation();
				mail.setName(getGuest.getFirstName());
				mail.setNoticeDate(getGuest.getNoticeDate());
				mail.setCheckOutDate(getGuest.getCheckOutDate());
				String name = template.getForObject(
						"http://bedService/bed/getBuildingNameByBuildingId/" + getGuest.getBuildingId(), String.class);
				mail.setBuildingName(name);
				mail.setBedId(getGuest.getBedId());
				mail.setEmail(getGuest.getEmail());
				CheckOutConfirmation res = template.postForObject(maila, mail, CheckOutConfirmation.class);
				response.setMessage("guest Checkout");
				response.setStatus(true);
				response.setData(checkOutConfirmation);
				return response;

			} else {
				response.setStatus(true);
				response.setMessage("Please send valid data");
				return response;
			}
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage(e.getMessage());
			return response;
		}

	}

	@Override
	public List<FilterBedsCountInRAT> getFilterBedsCountInRAT(int buildingId) {
		List<FilterBedsCountInRAT> getList = new ArrayList<>();
		AvailableBedsResponse availableBeds = template.getForObject(AVAILABLE_BEDS_COUNT + buildingId,
				AvailableBedsResponse.class);
		int availableBedsCount = 0;
		if (availableBeds.isStatus()) {
			availableBedsCount = availableBeds.getData().getTotalAvailbleBeds();
		}
		int activeGuestsCount = 0;
		int dueGuests = 0;
		int inNotice = 0;
		int exceededGuests = 0;
		int todaysCheckout = 0;

		activeGuestsCount = repository.getCountOfActiveGuestsWithoutDue(buildingId);
		dueGuests = repository.getCountOfDueGuests(buildingId);
		inNotice = repository.findByBuildingIdAndGuestStatus(buildingId, "InNotice").get().size();
//		exceededGuests = repository.exceededGuestsNotRegular(buildingId) + repository.exceededGuestsRegular(buildingId);
		exceededGuests = repository.exceededGuestsNotRegular(buildingId);	
		todaysCheckout = repository.checkoutTodayGuestsNotRegular(buildingId)
				+ repository.checkoutTodayGuestsRegular(buildingId);
		getList.add(
				new FilterBedsCountInRAT(1, "Available Beds", "availableBed", "AVAILABLE_BEDS", availableBedsCount));
		getList.add(new FilterBedsCountInRAT(2, "Guests on Due", "dueGuests", "DUE_GUESTS", dueGuests));
		getList.add(new FilterBedsCountInRAT(3, "Guests-In Notice", "inNotice", "IN_NOTICE", inNotice));
		getList.add(new FilterBedsCountInRAT(4, "Guests-With No Due", "activeGuests", "ACTIVE_WITHOUTDUE",
				activeGuestsCount));
		getList.add(new FilterBedsCountInRAT(5, "Exceeded Checkout Date", "checkoutExceed", "CHECKOUT_EXCEED",
				exceededGuests));
//		getList.add(
//				new FilterBedsCountInRAT(6, "Today’s Checkouts", "todayCheckout", "TODAY_CHECKOUT", todaysCheckout));
		return getList;
	}

	@Override
	public List<GuestDto> getGuests(Integer buildingId, String field, java.sql.Date startDate, java.sql.Date endDate) {
		// List<Guest> getGuest = repository.findAll(Sort.by(Sort.Direction.DESC,
		// field));
		List<Guest> guest = repository.findAllByBuildingIdAndCheckInDateBetween(buildingId,
				Sort.by(Sort.Direction.DESC, field), startDate, endDate);

		List<GuestDto> guestDto = new ArrayList<>();

		guest.forEach(h -> {
			GuestDto dto = new GuestDto();
			dto.setAmountPaid(h.getAmountPaid());
			dto.setId(h.getId());
			dto.setGuestName(h.getFirstName());
			dto.setCheckInDate(h.getCheckInDate());
			String buildingName = template.getForObject(
					"http://bedService/bed/getBuildingNameByBuildingId/" + h.getBuildingId(), String.class);
			dto.setBuildingName(buildingName);
			dto.setBedId(h.getBedId());
			dto.setPersonalNumber(h.getPersonalNumber());
			dto.setDueAmount(h.getDueAmount());
			dto.setGuestStatus(h.getGuestStatus());
			dto.setOccupancyType(h.getOccupancyType());
			guestDto.add(dto);
		});
		return guestDto;
	}

	@Override
	public InitiateCheckoutByGuestId newInitiateCheckout(InitiateCheckoutByGuestId data, String id) {
		String mailUri = "http://emailService/mail/sentInitiatingCheckOutRemainder/";
		Guest guest = repository.findById(id).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));
		Defaults def = sceurityR.findByOccupancyType(guest.getOccupancyType());
		double dueAmount = 0;
		double refundAmount = 0;
		double differeneceDays;
		try {
			guest.setId(id);
			log.info("guest id is: {}", guest.getId());
			guest.setNoticeDate(data.getNoticeDate());
			guest.setPreviousDues(guest.getDueAmount());
			ZonedDateTime myNotice = DateFormates.sqlToZoned(data.getNoticeDate());
			log.info("myNotice date converted {}", myNotice);
			Long lastDayOfMonth = 0L;
			lastDayOfMonth = DateFormates.lastDayOfMonth(myNotice);
			log.info("lastDay converted {}", lastDayOfMonth);
			ZonedDateTime plannedDate = myNotice.plusDays(lastDayOfMonth);
			log.info("converted planned checkout date {}", plannedDate);
			java.util.Date finalplannedDate = DateFormates.ZonedToSql(plannedDate);
			log.info("finalplannedDate {}", finalplannedDate);
			guest.setPlannedCheckOutDate(finalplannedDate);	
			java.util.Date myUtilPlanned = finalplannedDate;
			java.sql.Date mySqlPlannedDate = new java.sql.Date(myUtilPlanned.getTime());
			LocalDate myLocalPlannedDateis = mySqlPlannedDate.toLocalDate();

//			Instant instant = finalplannedDate.toInstant();
//
//			// Convert Instant to ZonedDateTime by considering the default time zone
//			ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
//
//			// Convert ZonedDateTime to LocalDate
//			LocalDate datePlanned = zonedDateTime.toLocalDate();
//			log.info("date planned {}",datePlanned);
			// guest.setPlannedCheckOutDate(data.getPlannedCheckOutDate());
			// guest.setOccupancyType(data.getOccupancyType());
			// dueService.calculateDueForInNotice(id);
			// repository.save(guest);

			log.info("due calculation is starting");
			// java.util.Date plannedCheckedDate = guest.getPlannedCheckOutDate();
			java.util.Date billGeneratedTillDate = guest.getBillGeneratedTill();
			java.sql.Date sqlBillGeneratedTill = new java.sql.Date(billGeneratedTillDate.getTime());
			log.info("sqlBillGeneratedTill sql {}",sqlBillGeneratedTill);
			// Commented on 28 july
			// Assuming finalplannedDate is a java.util.Date
			// Current date
			LocalDate currentDate = LocalDate.now();
//			log.info("local planned date {}", datePlanned);
			// Dont Examine my patience here please .
			//java.sql.Date sqlPlannedCheckOut = new java.sql.Date(datePlanned.getTime());
			// LocalDate currentDate =LocalDate.of(2022, 12, 14); //Local Date to Sql Date.
			Date date = Date.valueOf(currentDate);
			// Magic happens here!
			// converting sql date to local date for calculation
			// LocalDate convertedPaidTill=paidTillDate.toLocalDate();
			LocalDate convertedBillGeneratedTillDate = sqlBillGeneratedTill.toLocalDate();
			
			double differenece = (int) ChronoUnit.DAYS.between(convertedBillGeneratedTillDate, myLocalPlannedDateis);
			log.info("difference is {}", differenece);
			if (differenece < 0) {
				differeneceDays = -1 * differenece;
			} else {
				differeneceDays = differenece;
			}

			if (convertedBillGeneratedTillDate.isBefore(myLocalPlannedDateis)) {

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

			} else if (convertedBillGeneratedTillDate.isAfter(myLocalPlannedDateis)) {
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
			} else if (convertedBillGeneratedTillDate.isEqual(myLocalPlannedDateis)) {
				// Definietly Refund
				double refundCalculation = guest.getSecurityDeposit() - guest.getDueAmount()
						- def.getMaintainanceCharge();
				log.info("refund calculation is here {}", refundCalculation);
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

			InitiatingCheckOut mail = new InitiatingCheckOut();
			mail.setName(guest.getFirstName());
			mail.setNoticeDate(guest.getNoticeDate());
			mail.setPlannedCheckOutDate(guest.getPlannedCheckOutDate());
			String name = template.getForObject(
					"http://bedService/bed/getBuildingNameByBuildingId/" + guest.getBuildingId(), String.class);
			mail.setBuildingName(name);
			mail.setBedId(guest.getBedId());
			mail.setEmail(guest.getEmail());
			InitiatingCheckOut res = template.postForObject(mailUri, mail, InitiatingCheckOut.class);
			return data;
		} catch (Exception e) {
			e.getMessage();
		}
		return data;

	}

	@Override
	public List<GuestDto> findGuestForDining(Integer buildingId) {	
		ArrayList<String> ustatus = new ArrayList<>();
		ustatus.add("Active");
		ustatus.add("InNotice");
		List<Guest> pagePost = repository.findByBuildingIdAndGuestStatusIn(buildingId,ustatus);
		List<GuestDto> data = new ArrayList<>();

		if(!pagePost.isEmpty()) {
		pagePost.forEach(s -> {
			GuestDto d = new GuestDto();
			d.setAadharNumber(s.getAadharNumber());
			d.setBedId(s.getBedId());
			d.setBuildingId(s.getBuildingId());
			d.setGuestName(s.getFirstName());
			d.setAmountPaid(s.getAmountPaid());
			d.setBuildingId(s.getBuildingId());
			d.setEmail(s.getEmail());
			d.setGuestStatus(s.getGuestStatus());
			d.setOccupancyType(s.getOccupancyType());
			String c = path+s.getId();
			d.setImageUrl(c);
			String name = template.getForObject(
					"http://bedService/bed/getBuildingNameByBuildingId/" + s.getBuildingId(), String.class);
			d.setBuildingName(name);
			d.setPersonalNumber(s.getPersonalNumber());
			d.setId(s.getId());
			d.setDefaultRent(s.getDefaultRent());
			data.add(d);		
		});
		}
		return data ;
	}
	@Override
	public GuestDto getGuestForTab(Integer buildingId,String bedId) {
		Guest guest = repository.findGuestsByBedIdAndBuildingIdAndGuestStatus(buildingId,bedId);
		GuestDto dto = new GuestDto();
		try {		
		// TODO Auto-generated method stub
			dto.setGuestName(guest.getFirstName());
			dto.setId(guest.getId());
			dto.setBedId(guest.getBedId());
			dto.setGender(guest.getGender());
			log.info("profile url"+ path+guest.getId());
			dto.setImageUrl(path+guest.getId());
	}catch(Exception e) {
	throw new 	ResourceNotFoundException(false,e.getMessage());
	}
		return dto;

	}

	@Override
	public RatesConfig addingNewRent(RatesConfig rates, Integer buildingId) {
		rates.setBuildingId(buildingId);
		System.out.println("adding Building Rents .........");
		String name = template.getForObject(
				"http://bedService/bed/getBuildingNameByBuildingId/" + buildingId, String.class);
		rates.setBuildingName(name);
		System.out.println("Building Rents added successfully.........");
		rates.setDuplicatePackage(false);
		rconfig.save(rates);

		return rates;
	}

}
