package com.prospera.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Document {
   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int did;
   @Column(length = 9999999)
	private byte[] adhar;
   @Column(length = 9999999)
	private byte[] pan;
   @Column(length = 9999999)
	private byte[] photo;
   @Column(length = 9999999)
	private byte[] sign;
   @Column(length = 9999999)
	private byte[] incomeCertificate;
   @Column(length = 9999999)
	private byte[] salarySlip;
}
