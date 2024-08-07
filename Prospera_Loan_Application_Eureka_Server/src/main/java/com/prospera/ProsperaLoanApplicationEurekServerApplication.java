package com.prospera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ProsperaLoanApplicationEurekServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProsperaLoanApplicationEurekServerApplication.class, args);
	}

}
