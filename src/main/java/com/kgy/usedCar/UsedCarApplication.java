package com.kgy.usedCar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class UsedCarApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsedCarApplication.class, args);
	}

}
