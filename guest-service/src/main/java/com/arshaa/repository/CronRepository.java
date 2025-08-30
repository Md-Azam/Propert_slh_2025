package com.arshaa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arshaa.entity.CronNotification;

@Repository
public interface CronRepository extends JpaRepository<CronNotification, Integer> {

}
