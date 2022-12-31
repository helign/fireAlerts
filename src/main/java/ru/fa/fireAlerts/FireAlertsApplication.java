package ru.fa.fireAlerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FireAlertsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FireAlertsApplication.class, args);
	}

}
