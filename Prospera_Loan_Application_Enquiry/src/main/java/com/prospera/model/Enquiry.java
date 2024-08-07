package com.prospera.model;


import java.util.Date;
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
	@NotBlank(message = "First name cannot be empty")
	@NotNull (message = "Email cannot be null")
	private String email;
	@Min(value=1000000000, message="Mobile number must contain 10 digits")
	private Long mobileNo;
	@NotBlank(message = "First name cannot be empty")
	private String pancardNo;
	@NotBlank(message = "First name cannot be empty")
	
	private String adharcardNo;
	@NotBlank(message = "First name cannot be empty")
	private String gender;
	private	Date timeStamp;
	private String loanStatus;
	private String enquiryStatus;
	@OneToOne(cascade = CascadeType.ALL)
	private Cibil cibil;
}
