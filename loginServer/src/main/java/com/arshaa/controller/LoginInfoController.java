package com.arshaa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arshaa.entity.LoginInfo;
import com.arshaa.service.LoginInfoServce;

@RestController
@RequestMapping("/info")
@CrossOrigin("*")
public class LoginInfoController {

	@Autowired
	private LoginInfoServce loginInfoServce;

	@GetMapping
	public List<LoginInfo> getLoginDetails(@RequestParam Integer pageNumber, @RequestParam Integer pageSize,
			@RequestParam String sortBy, @RequestParam String sortDir) {
		try {
			List<LoginInfo> lf = loginInfoServce.getAllPays(pageNumber, pageSize, sortBy, sortDir);
			return lf;
		} catch (Exception e) {
			throw new IllegalArgumentException("bad requested data" + e.getMessage());
		}
	}

	@GetMapping("/{key}")
	public List<LoginInfo> getLoginDetails(@PathVariable String key) {
		try {
			List<LoginInfo> lf = loginInfoServce.getAllDataOfLogs(key);
			return lf;
		} catch (Exception e) {
			throw new IllegalArgumentException("bad requested data" + e.getMessage());
		}
	}

}
