package com.prospera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ProsperaLoanApplicationReApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProsperaLoanApplicationReApplication.class, args);
	}

}
