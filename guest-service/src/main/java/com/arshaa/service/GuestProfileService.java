package com.arshaa.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.arshaa.entity.Guest;
import com.arshaa.entity.GuestProfile;
import com.arshaa.entity.PreBooking;
import com.arshaa.exception.ResourceNotFoundException;
import com.arshaa.repository.GuestProfileRepo;
import com.arshaa.repository.GuestRepository;
import com.arshaa.repository.PreBookRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GuestProfileService {

	@Autowired
	GuestProfileRepo gpRepo;

	@Autowired
	private PreBookRepository preBookRepository;

	@Autowired
	private GuestRepository guestRepository;

	public GuestProfile store(MultipartFile file, String guestId) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		GuestProfile getFile = gpRepo.findByGuestId(guestId);
		System.out.println(gpRepo.existsByGuestId(guestId));
		if (!gpRepo.existsByGuestId(guestId)) {
			GuestProfile newFile = new GuestProfile();
			newFile.setData(file.getBytes());
			newFile.setGuestId(guestId);
			newFile.setName(fileName);
			newFile.setType(file.getContentType());
			return gpRepo.save(newFile);
		} else {
			getFile.setData(file.getBytes());
			getFile.setGuestId(guestId);
			getFile.setName(fileName);
			getFile.setType(file.getContentType());
			return gpRepo.save(getFile);
		}
	}

	public GuestProfile getFile(String id) {
		return gpRepo.findById(id).get();
	}

	public GuestProfile getFileByID(String id) {
		return gpRepo.findByGuestId(id);
	}

	public Stream<GuestProfile> getAllFiles() {
		return gpRepo.findAll().stream();
	}

	public ResponseEntity<?> addPreBooked(PreBooking book) throws ResourceNotFoundException {
		try {
			PreBooking pb = preBookRepository.findByBedId(book.getBedId());
			if (pb == null) {
				book.setBookingDate(LocalDate.now());
				book.setPreBookedStatus(true);
				preBookRepository.save(book);
				return ResponseEntity.status(HttpStatus.OK).body(book);
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(List.of("Bed Already PreBooked", pb.getName()));

			}

		} catch (Exception e) {
			throw new ResourceNotFoundException(false, "failed to pre book bed" + book.getBedId());
		}

	}

	public ResponseEntity<?> getPreBookingsByBuilding(Integer buildingId) {
		try {
			List<PreBooking> userList = preBookRepository.findAllByBuildingId(buildingId);

			if (userList.isEmpty()) {
				return  new ResponseEntity<String>("No Pre Booking User Found",HttpStatus.OK);
			}

			return ResponseEntity.status(HttpStatus.OK).body(userList);
		} catch (Exception e) {
			// Handle other exceptions (e.g., database connection issues, etc.)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}

	public ResponseEntity<?> getPreBookDetailsByBed(String bedId) {
		try {
			PreBooking data = preBookRepository.findByBedId(bedId);

			if (data == null) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(List.of("Not PreBooked", "This Bed is Not Pre Boooked"));
			}

			return ResponseEntity.status(HttpStatus.OK).body(data);
		} catch (Exception e) {
			// Handle other exceptions (e.g., database connection issues, etc.)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred");
		}
	}

	// update preBookStatus when joining data = current date
	public ResponseEntity<String> updateStatusForPreBook() {
		LocalDate currentDate = LocalDate.now();
		

		List<PreBooking> updation1 = preBookRepository.findByJoiningDateAndPreBookedStatusTrue(currentDate);
		List<PreBooking> updation = preBookRepository.findByJoiningDateBeforeAndPreBookedStatusTrue(currentDate);

		for (PreBooking preBooking : updation) {
			System.out.println(preBooking);
		}

		for (PreBooking pb : updation) {
			log.info(pb.getJoiningDate() + " joining dates");

//			if (pb.getJoiningDate() == currentDate || pb.getJoiningDate().isBefore(currentDate)) {
			for (PreBooking p : updation) {
				p.setPreBookedStatus(false);
			}
			
			for (PreBooking pt : updation1) {
				pt.setPreBookedStatus(false);
			}

			preBookRepository.saveAll(updation);
			preBookRepository.saveAll(updation1);
		}
//		}
		return ResponseEntity.status(HttpStatus.OK).body("Updated Successfully");
	}
}
