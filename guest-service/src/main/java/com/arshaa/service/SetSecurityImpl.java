package com.arshaa.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.arshaa.dtos.GuestData;
import com.arshaa.dtos.GuestDto;
import com.arshaa.entity.Guest;
import com.arshaa.entity.SecurityDeposit;
import com.arshaa.repository.GuestRepository;
import com.arshaa.repository.SetSecurityDepositRepo;

@Service
public class SetSecurityImpl implements SetSecurityInterface {

	@Value("${project.imageUrl}")
	private String path;

	@Autowired
	private SetSecurityDepositRepo setRepos;
	@Autowired(required = true)
	private GuestRepository repository;
	@Autowired
	@Lazy
	private RestTemplate template;

	@Override
	public SecurityDeposit saveSecurityDeposit(SecurityDeposit security) {
		// TODO Auto-generated method stub
		try {
			return setRepos.save(security);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return security;
	}

	@Override
	public List<SecurityDeposit> getallSecuritydeposit() {
		// TODO Auto-generated method stub
		try {
			return setRepos.findAll();
		} catch (Exception e) {
			System.out.println("cant give Result " + e.getLocalizedMessage());
		}
		return null;

	}

	@Override
	public SecurityDeposit updateSecurityDeposit(Integer id, SecurityDeposit sd) {
		Optional<SecurityDeposit> s = setRepos.findById(id);

		if (s.isPresent()) {
			SecurityDeposit securities = setRepos.getById(id);

			securities.setSecuritydeposit(sd.getSecuritydeposit());

			return setRepos.save(securities);
		} else
			return null;
	}

	@Override
	public List<GuestDto> findExceededGuestByBuildingId(Integer buildingId, Date plannedCheckOutDate) {
		ArrayList<String> ustatus = new ArrayList<>();
		ustatus.add("Active");
		ustatus.add("InNotice");
		List<Guest> pagePost = repository.findByBuildingIdAndGuestStatusInAndPlannedCheckOutDateBefore(buildingId,
				ustatus, plannedCheckOutDate);
//		List<Guest> allPosts = pagePost.getContent();
		List<GuestDto> data = new ArrayList<>();

		if (!pagePost.isEmpty()) {
			pagePost.forEach(s -> {

				LocalDate plannedDate = s.getPlannedCheckOutDate().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDate();

				LocalDate twoDaysBefore = LocalDate.now().minusDays(2);
				LocalDate alertPlannedCheckOut = plannedDate.minusDays(2);
				System.out.println("alertPlannedCheckOut: " + alertPlannedCheckOut);

				if (plannedDate.isBefore(twoDaysBefore) && s.getOccupancyType().equalsIgnoreCase("OneMonth")) {
					GuestDto d = new GuestDto();
					d.setAadharNumber(s.getAadharNumber());
					d.setAlertPlannedCheckOutDate(alertPlannedCheckOut);
					d.setBedId(s.getBedId());
					d.setEmail(s.getEmail());
					d.setGuestStatus(s.getGuestStatus());
					d.setOccupancyType(s.getOccupancyType());
					d.setBuildingId(s.getBuildingId());
					d.setGuestName(s.getFirstName());
					d.setAmountPaid(s.getAmountPaid());
					d.setBuildingId(s.getBuildingId());
					d.setPlannedCheckOutDate(s.getPlannedCheckOutDate());
					String name = template.getForObject(
							"http://bedService/bed/getBuildingNameByBuildingId/" + s.getBuildingId(), String.class);
					d.setBuildingName(name);
					String c = path + s.getId();
					d.setImageUrl(c);
					d.setPersonalNumber(s.getPersonalNumber());
					d.setCheckInDate(s.getCheckInDate());
					d.setCheckOutDate(s.getCheckOutDate());
					d.setAddressLine1(s.getAddressLine1());
					d.setAddressLine2(s.getAddressLine2());
					d.setId(s.getId());
					d.setDefaultRent(s.getDefaultRent());
					data.add(d);
				}
			});
		}
		return data;
	}

	@Override
	public List<GuestDto> findTodaysCheckOutByBuildingId(Integer buildingId, Date plannedCheckOutDate) {
		ArrayList<String> ustatus = new ArrayList<>();
		ustatus.add("Active");
		ustatus.add("InNotice");
		List<Guest> pagePost = repository.findByBuildingIdAndGuestStatusInAndPlannedCheckOutDateEquals(buildingId,
				ustatus, plannedCheckOutDate);
//		List<Guest> allPosts = pagePost.getContent();
		List<GuestDto> data = new ArrayList<>();

		if (!pagePost.isEmpty()) {
			pagePost.forEach(s -> {
				if (s.getPlannedCheckOutDate().before(new Date())
						&& s.getOccupancyType().equalsIgnoreCase("OneMonth")) {
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
					String c = path + s.getId();
					d.setImageUrl(c);
					d.setPlannedCheckOutDate(s.getPlannedCheckOutDate());
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
				}
			});
		}
		return data;

	}
}
