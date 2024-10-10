package com.prospera.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class Customer
{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private long mobileNo;
    private String gender;
    private String pancardNo;
	private String adharcardNo;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date dob;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Enquiry enquiry;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Bank bank;
	
	@OneToOne(cascade = CascadeType.ALL)
	private PerAddress padr;
	
	@OneToOne(cascade = CascadeType.ALL)
	private LocalAddress ladr;
		
	@OneToOne(cascade = CascadeType.ALL)
	private Employment emp;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Document doc;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Sanction sanction;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Disbursement disbursement;
	

}
