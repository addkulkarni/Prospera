package com.prospera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ProsperaLoanApplicationDefaulterApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(ProsperaLoanApplicationDefaulterApplication.class, args);
		
	}
	@Bean
	RestTemplate restT()
	{
		RestTemplate rs=new RestTemplate();
		return rs;
	}
}
