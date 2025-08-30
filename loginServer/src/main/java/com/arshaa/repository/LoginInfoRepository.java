package com.arshaa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arshaa.entity.LoginInfo;

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Integer> {

}
