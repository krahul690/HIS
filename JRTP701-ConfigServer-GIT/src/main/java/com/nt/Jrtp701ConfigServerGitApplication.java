package com.nt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class Jrtp701ConfigServerGitApplication {

	public static void main(String[] args) {
		SpringApplication.run(Jrtp701ConfigServerGitApplication.class, args);
	}

}
