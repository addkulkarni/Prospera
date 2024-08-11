package com.prospera.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Sanction
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sanctionId;
	private String firstName;
	private String lastName;
	private Date date;
	private double loanamount;
	private int tenure;
	private double interestRate;
	private double emiAmount;
}
