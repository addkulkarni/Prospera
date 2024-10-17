package com.prospera.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Enquiry
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int enquiryID;
	private String firstName;
	private String lastName;
	private int age;
	private String email;
	private Long mobileNo;
	private String pancardNo;
	private String adharcardNo;
	private String gender;
	private	Date timeStamp;
	private String loanStatus;
	private String enquiryStatus;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Cibil cibil;

//	@JsonCreator
//	public Enquiry(@JsonProperty("enquiryID") int enquiryID)
//	{
//		this.enquiryID = enquiryID;
//	}
	
}
