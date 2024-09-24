package com.prospera.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
@Entity
public class Disbursement
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int disbusrementId;
	private int disbursementAccountNo;
	@Lob
	@Column(length = 999999999)
	private byte[] disbursementLetter;
}
