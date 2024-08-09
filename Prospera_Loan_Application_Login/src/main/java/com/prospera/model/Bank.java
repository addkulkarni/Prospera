package com.prospera.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Bank 
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int bid;
  @NotBlank(message="Bank name cannot be blank")
  private String bankName;
  private String branch;
  @NotBlank(message="IFSCode cannot be blank")
  private String ifscCode;
  @Min(value = 100000000, message="Account number cannot be less than 9 digits")
  @Max(value = 9999999999l, message="Account number cannot be more than 10 digits")
  private long accNo;
  private String accType;
  
}
