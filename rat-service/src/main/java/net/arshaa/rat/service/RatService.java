package net.arshaa.rat.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.aspectj.weaver.bcel.BcelGenericSignatureToTypeXConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.arshaa.util.DateFormates;

import Models.AvailableBeds;
import Models.BedInfoForBedChange;
import Models.BedsCount;
import Models.BedsInfo;
import Models.BuildingInfo;
import Models.FloorsInfo;
import Models.RoomsInfo;
import common.Guest;
import common.GuestProfile;
import lombok.extern.slf4j.Slf4j;
import net.arshaa.rat.entity.Bed;
import net.arshaa.rat.entity.Buildings;
import net.arshaa.rat.entity.Floors;
import net.arshaa.rat.entity.Rooms;
import net.arshaa.rat.exception.ResourceNotFoundException;
import net.arshaa.rat.repository.BedRepository;
import net.arshaa.rat.repository.BedSummaryRepo;
import net.arshaa.rat.repository.BuildingRepository;
import net.arshaa.rat.repository.FloorRepository;
import net.arshaa.rat.repository.RoomRepository;

@Service
@Slf4j
public class RatService implements RatInterfaces {

	@Autowired
	private BedRepository bedrepo;
	@Autowired
	private BuildingRepository buildingRepo;
	@Autowired
	private FloorRepository floorRepo;
	@Autowired
	private RoomRepository roomRepo;
	@SuppressWarnings("unused")
	@Autowired
	private BedSummaryRepo bedsumRepo;
	
	//Image Url for Prod Environment
	String ProdImageUrl="http://31.187.75.117:8989/guest/getImage/";

	//Image Url for dev Environment
	String imageUrl="http://localhost:8989/guest/getImage/";

	@Autowired
	@Lazy
	private RestTemplate template;

	private static final String guestDatByIdURL = "http://guestService/guest/getGuestByGuestId/";
	private static final String SummaryFromGuestForRatURL = "http://guestService/guest/buildingSummaryForRat/";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public List<AvailableBeds> getAllAvailableBed() {
		try {
			List<Bed> bedList = bedrepo.findByBedStatus(true);
			List<AvailableBeds> abs = new ArrayList<>();
			bedList.forEach(b -> {
				AvailableBeds ab = new AvailableBeds();
				ab.setBuildingId(b.getBuildingId());
				ab.setDefaultRent(b.getDefaultRent());
				ab.setFloorId(b.getFloorId());
				ab.setRoomId(b.getRoomId());
				ab.setBedName(b.getBedName());
				ab.setbId(b.getId());
				ab.setBedId(b.getBedId());
				ab.setBedStatus(true);
				Rooms roomName = roomRepo.getRoomNameByRoomId(b.getRoomId());
				ab.setRoomNumber(roomName.getRoomNumber());
				Floors flr = floorRepo.getFloorNumberByFloorId(b.getFloorId());
				ab.setFloorNumber(flr.getFloorNumber());
				Buildings bName = buildingRepo.getBuildingNameByBuildingId(b.getBuildingId());
				ab.setBuildingName(bName.getBuildingName());
				abs.add(ab);
			});
			return abs;
		} catch (Exception e) {
			throw new ResourceNotFoundException(false, "no data found");
		}
	}

	@Override
	public List<BuildingInfo> getBedByBuildingId(Integer id) {
		List<BuildingInfo> buildingInfoList = new ArrayList<>();
		BuildingInfo info = new BuildingInfo();
		Buildings getBuildingByid = buildingRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(false, "id not found"));
		info.setBuildingName(getBuildingByid.getBuildingName());
		List<FloorsInfo> floorListInfo = new ArrayList<>();
		List<Floors> findFloorByBuildingId = floorRepo.findByBuildingId(getBuildingByid.getBuildingId())
				.orElseThrow(() -> new ResourceNotFoundException(false, "id not found"));
		findFloorByBuildingId.forEach(floor -> {
			FloorsInfo newFloor = new FloorsInfo();
			newFloor.setFloorName(floor.getFloorNumber());
			List<RoomsInfo> rooomList = new ArrayList<>();
			List<Rooms> getRooms = roomRepo.findByFloorId(floor.getFloorId())
					.orElseThrow(() -> new ResourceNotFoundException(false, "id not found"));
			getRooms.forEach(room -> {
				RoomsInfo newRoom = new RoomsInfo();
				newRoom.setRoomNumber(room.getRoomNumber());
				List<BedsInfo> bedsList = new ArrayList<>();
				List<Bed> beds = bedrepo.findByroomId(room.getRoomId())
						.orElseThrow(() -> new ResourceNotFoundException(false, "id not found"));
				beds.forEach(getBeds -> {
					BedsInfo bedInfo = new BedsInfo();
					bedInfo.setBedId(getBeds.getBedId());
					bedInfo.setBedName(getBeds.getBedName());
					bedInfo.setBedStatus(getBeds.isBedStatus());
					bedInfo.setBuildingId(getBeds.getBuildingId());
					bedInfo.setRoomId(getBeds.getRoomId());
					bedInfo.setFloorId(getBeds.getFloorId());
					int count = bedrepo.getBedsByRoomId(getBeds.getRoomId()).size();
					bedInfo.setBedName(getBeds.getBedName());
					bedInfo.setBedStatus(getBeds.isBedStatus());
					bedInfo.setSharing(count);
					bedInfo.setGuestId(getBeds.getGuestId());
					bedInfo.setAc(getBeds.isAc());
					bedInfo.setBedNum(getBeds.getId());
					if (getBeds.isBedStatus() == false) {
						System.out.println("Guest" + bedInfo.getGuestId());
						Guest listOfGuests = template.getForObject(guestDatByIdURL + bedInfo.getGuestId(), Guest.class);
						bedInfo.setGuestName(listOfGuests.getFirstName());

						bedInfo.setGuestStatus(listOfGuests.getGuestStatus());
						bedInfo.setDueAmount(listOfGuests.getDueAmount());
					
						
						bedInfo.setImageUrl(imageUrl+listOfGuests.getId());
						bedInfo.setGuestDue(listOfGuests.getDueAmount());
						bedsList.add(bedInfo);
					} else {
						bedsList.add(bedInfo);
					}

				});
				newRoom.setBeds(bedsList);
				rooomList.add(newRoom);
			});
			newFloor.setRooms(rooomList);
			floorListInfo.add(newFloor);

		});
		info.setFloors(floorListInfo);
		buildingInfoList.add(info);

		return buildingInfoList;
	}

	// ------------------------------------------------------------------------
	// GET MAP FOR THE SQUARE ICONS IN RAT COMPONENT
	public ResponseEntity<BuildingInfo> getDataforSquareIconsByBuildingId(Integer id, String type,
			String occupencyType) {
		List<BuildingInfo> infoList = new ArrayList<>();
		Bed b = new Bed();
		Guest guest = new Guest();
		BuildingInfo info = new BuildingInfo();
		Buildings getBuilding = buildingRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(false, "building id not found"));

		info.setBuildingName(getBuilding.getBuildingName());
		List<FloorsInfo> floorsList = new ArrayList<>();
		List<Floors> getFloors = floorRepo.findByBuildingId(getBuilding.getBuildingId())
				.orElseThrow(() -> new ResourceNotFoundException(false, "floor id not found"));
		getFloors.forEach(floor -> {
			FloorsInfo newFloor = new FloorsInfo();
			newFloor.setFloorName(floor.getFloorNumber());
			List<RoomsInfo> roomList = new ArrayList<>();
			List<Rooms> getRooms = roomRepo.findByFloorId(floor.getFloorId())
					.orElseThrow(() -> new ResourceNotFoundException(false, " room id not found"));

			getRooms.forEach(room -> {
				RoomsInfo newRoom = new RoomsInfo();
				newRoom.setRoomNumber(room.getRoomNumber());

				List<BedsInfo> bedsList = getBedsInfo(room.getRoomId(), type, occupencyType);
				newRoom.setBeds(bedsList);
				roomList.add(newRoom);
			});

			newFloor.setRooms(roomList);
			floorsList.add(newFloor);
		});

		info.setFloors(floorsList);

		infoList.add(info);
		return new ResponseEntity(infoList, HttpStatus.OK);
	}

	//
	public List<BedsInfo> getBedsInfo(int id, String type, String occupencyType) {

		List<BedsInfo> bedsList = new ArrayList<>();
		String types = type;
		switch (types) {
		case "AVAILABLE_BEDS": {
			Optional<List<Bed>> getBedsList = bedrepo.findByroomIdAndBedStatus(id, true);
			if (getBedsList.isPresent()) {
				getBedsList.get().forEach(bd -> {

					BedsInfo newBed = this.setBeds(bd, types, true);
					bedsList.add(newBed);
				});
			}
			break;
		}

		case "DUE_GUESTS": {
			Optional<List<Bed>> getBedsList = bedrepo.findByroomIdAndBedStatus(id, false);

			if (getBedsList.isPresent()) {
				getBedsList.get().forEach(bd -> {
					Guest listOfGuests1 = template.getForObject(guestDatByIdURL + bd.getGuestId(), Guest.class);
					if (occupencyType.equalsIgnoreCase("OneMonth") || occupencyType.equalsIgnoreCase("daily")) {
						if (listOfGuests1.getDueAmount() > 0
								&& listOfGuests1.getOccupancyType().equalsIgnoreCase(occupencyType)) {
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						}

					} else if (occupencyType.equalsIgnoreCase("Regular")) {

						if (listOfGuests1.getDueAmount() > 0
								&& listOfGuests1.getOccupancyType().equalsIgnoreCase("Regular")) {
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						}
					} else {
						if (listOfGuests1.getDueAmount() > 0
								&& listOfGuests1.getOccupancyType().equalsIgnoreCase(occupencyType)) {
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						} else if (listOfGuests1.getDueAmount() > 0
								&& listOfGuests1.getOccupancyType().equalsIgnoreCase("Regular")) {
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						}

					}
				});
			}
			break;
		}
		case "IN_NOTICE": {
			Optional<List<Bed>> getBedsList = bedrepo.findByroomIdAndBedStatus(id, false);
			if (getBedsList.isPresent()) {
				getBedsList.get().forEach(bd -> {
					Guest listOfGuests1 = template.getForObject(guestDatByIdURL + bd.getGuestId(), Guest.class);
					if (occupencyType.equalsIgnoreCase("OneMonth") || occupencyType.equalsIgnoreCase("daily")) {
						if (listOfGuests1.getGuestStatus().equalsIgnoreCase("InNotice")
								&& listOfGuests1.getOccupancyType().equalsIgnoreCase(occupencyType)) {
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						}

					} else if (occupencyType.equalsIgnoreCase("Regular")) {
						if (listOfGuests1.getGuestStatus().equalsIgnoreCase("InNotice")) {
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						}

					} else {
						if (listOfGuests1.getGuestStatus().equalsIgnoreCase("InNotice")
								&& listOfGuests1.getOccupancyType().equalsIgnoreCase(occupencyType)) {
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						} else if (listOfGuests1.getGuestStatus().equalsIgnoreCase("InNotice")) {
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						}

					}

				});
			}
			break;
		}
		case "ACTIVE_WITHOUTDUE": {

			Optional<List<Bed>> getBedsList = bedrepo.findByroomIdAndBedStatus(id, false);
			if (getBedsList.isPresent()) {
				getBedsList.get().forEach(bd -> {
					Guest listOfGuests1 = template.getForObject(guestDatByIdURL + bd.getGuestId(), Guest.class);
					if (occupencyType.equalsIgnoreCase("OneMonth") || occupencyType.equalsIgnoreCase("daily")) {
						if (listOfGuests1.getGuestStatus().equalsIgnoreCase("active")
								&& listOfGuests1.getOccupancyType().equalsIgnoreCase(occupencyType)
								&& listOfGuests1.getDueAmount() <= 0) {
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						}

					} else if (occupencyType.equalsIgnoreCase("Regular")) {
						if (listOfGuests1.getGuestStatus().equalsIgnoreCase("active")
								&& listOfGuests1.getOccupancyType().equalsIgnoreCase(occupencyType)
								&& listOfGuests1.getDueAmount() <= 0) {
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);

						}
					} else {
						if (listOfGuests1.getGuestStatus().equalsIgnoreCase("active")
								&& listOfGuests1.getDueAmount() <= 0) {
							
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						} else if (listOfGuests1.getGuestStatus().equalsIgnoreCase("active")
								&& listOfGuests1.getDueAmount() <= 0) {
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);

						}

					}

				});
			}
			break;
		}
		case "TODAY_CHECKOUT": {
			Optional<List<Bed>> getBedsList = bedrepo.findByroomIdAndBedStatus(id, false);
			if (getBedsList.isPresent()) {
				getBedsList.get().forEach(bd -> {
					Guest listOfGuests1 = template.getForObject(guestDatByIdURL + bd.getGuestId(), Guest.class);
					Double guestDue = listOfGuests1.getDueAmount();
					LocalDate date1 = LocalDate.now();
					
					 LocalDate returnvalue= date1.minusDays(1);
					log.info("local date is {}",date1);
					log.info("local date -1 day is {}",returnvalue);
					java.util.Date utilDate = new java.util.Date();
					log.info("current date in util=:{}",utilDate);
					java.util.Date date = Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant());

					System.out.println("get planned check out date"+listOfGuests1.getPlannedCheckOutDate());

					if ((listOfGuests1.getOccupancyType().equalsIgnoreCase("Regular"))
							&& (listOfGuests1.getGuestStatus().equalsIgnoreCase("InNotice"))) {
						log.info("local date {}",new Date());
						log.info("planned date {}",listOfGuests1.getPlannedCheckOutDate());
						if ((isLocalDateEquals(listOfGuests1.getPlannedCheckOutDate(),date))) {
							log.info("local date {}",new Date());
							log.info("planned date {}",listOfGuests1.getPlannedCheckOutDate());
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						}
					} else if ((listOfGuests1.getOccupancyType().equalsIgnoreCase("OneMonth"))
							|| (listOfGuests1.getOccupancyType().equalsIgnoreCase("daily"))) {
						log.info("local date {}",new Date());
						log.info("planned date {}",listOfGuests1.getPlannedCheckOutDate());
						if ((isLocalDateEquals(listOfGuests1.getPlannedCheckOutDate(),date))) {
							log.info("local date {}",new Date());
							log.info("planned date {}",listOfGuests1.getPlannedCheckOutDate());
							BedsInfo newBed = this.setBeds(bd, types, false);
							bedsList.add(newBed);
						}

					}
				});
			}
			break;
		}
		case "CHECKOUT_EXCEED": {
			Optional<List<Bed>> getBedsList = bedrepo.findByroomIdAndBedStatus(id, false);
			if (getBedsList.isPresent()) {
				getBedsList.get().forEach(bd -> {
					Guest listOfGuests1 = template.getForObject(guestDatByIdURL + bd.getGuestId(), Guest.class);
					Double guestDue = listOfGuests1.getDueAmount();
					
					LocalDate date1 = LocalDate.now();
					
					 LocalDate returnvalue= date1.minusDays(1);
					log.info("local date is {}",date1);
					log.info("local date -1 day is {}",returnvalue);
					java.util.Date utilDate = new java.util.Date();
					log.info("current date in util=:{}",utilDate);
					java.util.Date date = Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant());
					log.info("converted util date in util=:{}",date);
					String  mdyFormat =	new SimpleDateFormat("MM-dd-yyyy").format(utilDate);

					// Date date1 = sdf.parse(listOfGuests1.getPlannedCheckOutDate());
					// Date date2 = sdf.parse(new Date());

					if ((listOfGuests1.getOccupancyType().equalsIgnoreCase("OneMonth"))) {
						log.info("local date {}",new Date());
						log.info("planned date {}",listOfGuests1.getPlannedCheckOutDate());
						if ((isLocalDateBeforeDate(listOfGuests1.getPlannedCheckOutDate(),date))) {
							log.info("local date {}",new Date());
							log.info("planned date {}",listOfGuests1.getPlannedCheckOutDate());
							BedsInfo newBed = this.setBeds(bd, types, false);
							newBed.setExceeded(true);
							bedsList.add(newBed);
						}
					}

				});
			}
			break;
		}
		default: {
			Optional<List<Bed>> getBedsList = bedrepo.findByroomId(id);
			if (getBedsList.isPresent()) {
				getBedsList.get().forEach(bd -> {
					BedsInfo newBed = new BedsInfo();
					newBed.setTypeOfFilter("default");
					newBed.setBuildingId(bd.getBuildingId());
					newBed.setRoomId(bd.getRoomId());
					newBed.setBedId(bd.getBedId());
					newBed.setBedName(bd.getBedName());
					newBed.setBedStatus(bd.isBedStatus());
					newBed.setDefaultRent(bd.getDefaultRent());
					newBed.setAc(bd.isAc());
					newBed.setBedNum(bd.getId());
					newBed.setGuestId(bd.getGuestId());

					if (bd.isBedStatus() == false) {
						System.out.println(newBed.getGuestId());
						Guest listOfGuests1 = template.getForObject(guestDatByIdURL + newBed.getGuestId(), Guest.class);
						// newBed.setGuest(listOfGuests);

						newBed.setGuestName(listOfGuests1.getFirstName());
						newBed.setGuestStatus(listOfGuests1.getGuestStatus());
						newBed.setDueAmount(listOfGuests1.getDueAmount());
						GuestProfile getProfile = template.getForObject(
								"http://guestService/guest/files/" + newBed.getGuestId(), GuestProfile.class);
						newBed.setName(getProfile.getName());
						newBed.setType(getProfile.getType());
						newBed.setUrl(getProfile.getData());
						newBed.setGuestDue(listOfGuests1.getDueAmount());
						newBed.setPlannedCheckOutDate(listOfGuests1.getPlannedCheckOutDate());
						newBed.setOccupancyType(listOfGuests1.getOccupancyType());
						bedsList.add(newBed);
					}
					bedsList.add(newBed);
				});
			}
		}
		}
		// });
		// }
		return bedsList;
	}

	public boolean isLocalDateBeforeDate(Date d1, Date d2) {
		LocalDate planned = LocalDate.parse(sdf.format(d1));
		LocalDate isLocalDateBefore = LocalDate.parse(sdf.format(d2));
		return (planned).isBefore(isLocalDateBefore);
	}

	public boolean isLocalDateEquals(Date d1, Date d2) {
		LocalDate plannedCheckOutDate = LocalDate.parse(sdf.format(d1));
		LocalDate isLocalDateEqual = LocalDate.parse(sdf.format(d2));
		return (plannedCheckOutDate).isEqual(isLocalDateEqual);

//		return DateFormates.dateToLocalDate(DateFormates.formatUtil(d1)).isBefore (DateFormates.dateToLocalDate(d2));
	}

	@Override
	public BedInfoForBedChange getBedInformation(String bedId, int buildingId) {
		BedInfoForBedChange bedInfo = new BedInfoForBedChange();
		try {
			Bed bed = bedrepo.findByBedId(bedId);
			int roomSize = bedrepo.getBedsByRoomId(bed.getRoomId()).size();
			String buildingName = buildingRepo.getBuildingNameByBuildingId(buildingId).getBuildingName();
			bedInfo.setBedId(bedId);
			bedInfo.setRoomType(bed.isAc() ? "Ac" : "NonAc");
			bedInfo.setSharing(roomSize);
			bedInfo.setBuildingName(buildingName);
			return bedInfo;
		} catch (Exception e) {
			return bedInfo;
		}
	}

	public BedsInfo setBeds(Bed bd, String types, boolean status) {
		BedsInfo newBed = new BedsInfo();

		if (status) {
			newBed.setTypeOfFilter(types);
			newBed.setBuildingId(bd.getBuildingId());
			newBed.setRoomId(bd.getRoomId());
			newBed.setBedId(bd.getBedId());
			newBed.setBedName(bd.getBedName());
			newBed.setBedStatus(bd.isBedStatus());
			newBed.setDefaultRent(bd.getDefaultRent());
			newBed.setAc(bd.isAc());
			newBed.setBedNum(bd.getId());
			newBed.setGuestId(bd.getGuestId());
			return newBed;
		} else {
			Guest listOfGuests1 = template
					.getForObject("http://guestService/guest/getGuestByGuestId/" + bd.getGuestId(), Guest.class);
			newBed.setTypeOfFilter(types);
			newBed.setBuildingId(bd.getBuildingId());
			newBed.setRoomId(bd.getRoomId());
			newBed.setBedId(bd.getBedId());
			newBed.setBedName(bd.getBedName());
			newBed.setBedStatus(bd.isBedStatus());
			newBed.setDefaultRent(bd.getDefaultRent());
			newBed.setAc(bd.isAc());
			newBed.setBedNum(bd.getId());
			newBed.setGuestId(bd.getGuestId());
			newBed.setGuestName(listOfGuests1.getFirstName());
			newBed.setGuestStatus(listOfGuests1.getGuestStatus());
			newBed.setDueAmount(listOfGuests1.getDueAmount());
			GuestProfile getProfile = template.getForObject("http://guestService/guest/files/" + bd.getGuestId(),
					GuestProfile.class);
			newBed.setName(getProfile.getName());
			newBed.setType(getProfile.getType());
			newBed.setUrl(getProfile.getData());
			newBed.setGuestDue(listOfGuests1.getDueAmount());
			newBed.setPlannedCheckOutDate(listOfGuests1.getPlannedCheckOutDate());
			newBed.setOccupancyType(listOfGuests1.getOccupancyType());
			return newBed;
		}
	}

	@Override
	public BedsCount getAvailableBedsCount(int buildingId) {
		BedsCount bedsCount = new BedsCount();
		bedsCount.setTotalAvailbleBeds(bedrepo.countByBedStatusAndBuildingId(true, buildingId).intValue());
		return bedsCount;
	}

	@Override
	public Map<Integer, String> getAllBuildingsNameByBuildingIds(List<Integer> buildingId) {
		try {
			List<Buildings> l = buildingRepo.findAll();
			Map<Integer, String> map = l.stream()
					.collect(Collectors.toMap(Buildings::getBuildingId, Buildings::getBuildingName));
			return map;
		} catch (Exception e) {
			throw new ResourceNotFoundException(false, "data is empty");
		}
	}
	@Override
	public List<Buildings> getAllBuildings() {
		try {
			List<Buildings> buildings = buildingRepo.findAll();
			return buildings;
		} catch (Exception e) {
			throw new ResourceNotFoundException(false, "no data found");
		}
	}

}