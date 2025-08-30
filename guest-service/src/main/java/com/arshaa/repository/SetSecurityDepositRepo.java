package com.arshaa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arshaa.entity.SecurityDeposit;
@Repository
public interface SetSecurityDepositRepo extends JpaRepository<SecurityDeposit, Integer> {

}
