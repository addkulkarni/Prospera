package com.prospera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient
@SpringBootApplication
public class ProsperaLoanApplicationAhApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProsperaLoanApplicationAhApplication.class, args);
	}

}
