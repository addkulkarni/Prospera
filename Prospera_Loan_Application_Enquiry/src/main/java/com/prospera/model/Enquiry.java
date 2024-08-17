package com.prospera.model;


import java.util.Date;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.regex.RegExp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class Enquiry
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int enquiryID;
	@NotBlank(message = "First name cannot be empty")
	private String firstName;
	@NotBlank(message = "Last name cannot be empty")
	private String lastName;
	@Min (value=18, message="Age must be more than 18")
	@Max (value=125, message="Please enter a valid age")
	private int age;
	@NotBlank(message = "Email cannot be empty")
	@NotNull (message = "Email cannot be null")
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Please enter a valid email id")
	private String email;
	@Min(value=7000000000l)
	@Max(value=9999999999l)
	private Long mobileNo;
	@NotBlank(message = "Pancard Number cannot be empty")
	@Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Please input a valid PAN card number")
	private String pancardNo;
	@NotBlank(message = "AdharCard Number cannot be empty")
	@Pattern(regexp = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$", message = "AdharCard number must have exactly 12 digits")
	private String adharcardNo;
	@NotBlank(message = "Gender cannot be empty")
	@Pattern(regexp = "Male|Female", message = "Allowed values are Male and Female")
	private String gender;
	private	Date timeStamp;
	private String loanStatus;
	private double loanamount;
	private int tenure;
	private String enquiryStatus;
	@OneToOne(cascade = CascadeType.ALL)
	private Cibil cibil;
}
