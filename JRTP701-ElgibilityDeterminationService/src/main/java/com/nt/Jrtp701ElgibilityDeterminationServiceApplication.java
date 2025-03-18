package com.nt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Jrtp701ElgibilityDeterminationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Jrtp701ElgibilityDeterminationServiceApplication.class, args);
	}

}
