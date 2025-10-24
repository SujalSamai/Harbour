package com.harbour.equity_tracker_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class EquityTrackerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EquityTrackerServiceApplication.class, args);
	}

}
