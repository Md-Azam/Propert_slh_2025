package com.arshaa.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.arshaa.cronService.CronServices;
import com.arshaa.dtos.UpdateGuestBedId;
import com.arshaa.entity.Guest;
import com.arshaa.entity.Image;
import com.arshaa.entity.PreBooking;
import com.arshaa.entity.RatesConfig;
import com.arshaa.exception.ResourceNotFoundException;
import com.arshaa.model.VacatedGuests;
import com.arshaa.repository.GuestRepository;
import com.arshaa.repository.ImageRepository;
import com.arshaa.repository.RatesConfigRepository;
import com.arshaa.service.GuestInterface;
import com.arshaa.service.GuestProfileService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import com.arshaa.profileservice.*;

@RestController
@RequestMapping("/profiles")
@CrossOrigin("*")
@Slf4j
public class ProfileController {

	@Value("${project.image}")
	private String path;

	@Autowired(required = true)
	private GuestRepository repository;

	@Autowired(required = true)
	private GuestInterface service;
	@Autowired
	private RatesConfigRepository ratesRepository;
	@Autowired
	@Lazy
	private RestTemplate template;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private CronServices cronServices;

	@Autowired
	private GuestProfileService guestProfileRepo;

	@Autowired(required = true)
	private FileServices fileServices;

	// post guest image .
	@PostMapping("/image/{guestId}")
	public ResponseEntity<Image> uploadPostImage(@RequestParam("image") MultipartFile image,
			@PathVariable String guestId) throws IOException {
		Image guestImage = fileServices.uploadImage(path, image, guestId);
		return new ResponseEntity<Image>(guestImage, HttpStatus.OK);

	}

	// method to serve files
	@GetMapping(value = "/{guestId}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String guestId, HttpServletResponse response) throws IOException {
		Image guestImage = imageRepository.findByGuestId(guestId);
		InputStream resource = this.fileServices.getResource(path, guestImage.getImageName(), guestId);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
		// Closing the input stream .
		resource.close();

	}

	// delete the image .
	// Delete post By id ;
	@DeleteMapping("/delete/{guestId}")
	public String deletePost(@PathVariable String guestId) throws IOException {
		Image deleteImage = imageRepository.findByGuestId(guestId);
		System.out.println(deleteImage.getImageName());
		Path paths = Paths.get(path + File.separator + deleteImage.getImageName());
		System.out.println(paths);
		try {
			// Delete file or directory
			Files.deleteIfExists(paths);
			System.out.println("File or directory deleted successfully");
			imageRepository.deleteByGuestId(guestId);
		} catch (NoSuchFileException ex) {
			System.out.printf("No such file or directory: %s\n", path);
		} catch (DirectoryNotEmptyException ex) {
			System.out.printf("Directory %s is not empty\n", path);
		} catch (IOException ex) {
			System.out.println(ex);
		}

		return "Guest profile is successfully deleted";

	}

	@DeleteMapping("/cron")
	public String deleteCronNotifications() {
		cronServices.deleteAll();
		return "Successfully deleted";
	}

	@PutMapping("/updateBedId/{guestId}")
	public ResponseEntity<String> updateGuestBedId(@PathVariable String guestId, @RequestBody UpdateGuestBedId b) {
		Guest guest = repository.findById(guestId)
				.orElseThrow(() -> new ResourceNotFoundException(false, "Guest Id Not Found"));
		List<Guest> l = repository.findAll();
		Map<String, Integer> map = l.stream().collect(Collectors.toMap(Guest::getId, Guest::getPackageId));
		// UpdateGuestBedId b= new UpdateGuestBedId();
		log.info("map {}", map);
		log.info("be id and default rent is updated {}", guestId);
		guest.setBedId(b.getBedId());
		log.info("packge id in guest {}", guest.getPackageId());
		guest.setId(guestId);
		guest.setDefaultRent(b.getDefaultRent());
		repository.save(guest);
		log.info("default rent is save{}");
//		RatesConfig r =ratesRepository.findByPriceAndOccupancyTypeAndBuildingId(guest.getDefaultRent(),guest.getOccupancyType(),guest.getBuildingId());
//		log.info("packge id {}",r.getId());
//		guest.setPackageId(r.getId());

		repository.save(guest);
		return new ResponseEntity<String>("Bed Id for guest Updated", HttpStatus.OK);
	}

	@GetMapping("/currentMonthCheckout")
	public List<VacatedGuests> findCurrentMonthVacatedGuest() {
		Guest guest = new Guest();
//		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		List<Guest> getList = repository.findByVacatedGuest();
		List<Integer> buildingIdList = getList.stream().map(x -> x.getBuildingId()).collect(Collectors.toList());
		Map<String, String> map = template.postForObject("http://bedService/bed/buildingsNames", guest, Map.class);
		List<VacatedGuests> vacatedGuests = new ArrayList<>();

		getList.forEach(g -> {
			VacatedGuests gs = new VacatedGuests();
			log.info("checkout date {}", g.getCheckOutDate());
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

	@GetMapping("/currentMonthCheckIn")
	public List<VacatedGuests> findCurrentMonthCheckIn() {
		Guest guest = new Guest();
//		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		List<Guest> getList = repository.getCurrentMonthAddmitted();
		List<Integer> buildingIdList = getList.stream().map(x -> x.getBuildingId()).collect(Collectors.toList());
		Map<String, String> map = template.postForObject("http://bedService/bed/buildingsNames", guest, Map.class);
		List<VacatedGuests> vacatedGuests = new ArrayList<>();

		getList.forEach(g -> {
			VacatedGuests gs = new VacatedGuests();
			log.info("checkout date {}", g.getCheckInDate());
			gs.setBedId(g.getBedId());
			gs.setBuildingName(map.get(String.valueOf(g.getBuildingId())));
			gs.setCheckOutDate(g.getCheckOutDate());
			gs.setCheckInDate(g.getCheckInDate());
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

	String s = "Azam";

	@GetMapping(value = "/232wfsd32fvxf4jhuy7g")
	public String getApproach() {
		return s;

//		31.187.75.117:7000/profiles/232wfsd32fvxf4jhuy7g
	}

	@PostMapping("/addPreBook")
	public ResponseEntity<?> savePreBook(@RequestBody PreBooking book) throws ResourceNotFoundException {
		try {
			return new ResponseEntity<>(guestProfileRepo.addPreBooked(book), HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			throw new ResourceNotFoundException(false, "booking failed");
		}

	}

	@GetMapping("/PreBookbyBuilding/{buildingId}")
	public ResponseEntity<?> getPreBookByBuilding(@PathVariable Integer buildingId) {
		return ResponseEntity.status(HttpStatus.OK).body(guestProfileRepo.getPreBookingsByBuilding(buildingId));
	}
	@GetMapping("/preBookBedId/{bedId}")
	public ResponseEntity<?> getPreBookByBedId(@PathVariable String bedId) {
		return ResponseEntity.status(HttpStatus.OK).body(guestProfileRepo.getPreBookDetailsByBed(bedId));

	}

	@PostMapping("/updatePreBookStatusForToday")
	public ResponseEntity<String> updateStatusForToday() {
		guestProfileRepo.updateStatusForPreBook();
		return ResponseEntity.status(HttpStatus.OK).body("Updated Successfully");
	}

}
