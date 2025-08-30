package net.arshaa.rat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.arshaa.rat.entity.UsersMaster;

@Repository
public interface UsersMasterRepo extends JpaRepository<UsersMaster, Integer> {
	Boolean existsByEmail(String email);

	//boolean existsByBedId(String bedId);
}