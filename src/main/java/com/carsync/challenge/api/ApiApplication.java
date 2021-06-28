package com.carsync.challenge.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.carsync.challenge.api.utils.DbSeedUtils;

@SpringBootApplication
public class ApiApplication {

	@Autowired
	private DbSeedUtils _dbSeedUtils;

	private DbSeedUtils getDbSeedUtils() {
		return _dbSeedUtils;
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			getDbSeedUtils().addUser();
		};
	}
}
