package com.whatsme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class MessagesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagesServiceApplication.class, args);
	}

}
//https://whatsme-production.up.railway.app