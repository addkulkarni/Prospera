package com.prospera.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class LocalAddress 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int lid;
	private String areaName;
	private String cityName;
	private String district;
	private int pincode;
	private String state;
	private String country;
}
