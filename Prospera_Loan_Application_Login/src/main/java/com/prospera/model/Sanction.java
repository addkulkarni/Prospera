package com.prospera.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
	@Temporal(TemporalType.DATE)
	private Date date;
	private float loanamount;
	private int tenure;
	private float interestRate;
	private float emiAmount;
	
	@Lob
	@Column(length = 999999999)
	private byte[] sanctionLetter;
}
