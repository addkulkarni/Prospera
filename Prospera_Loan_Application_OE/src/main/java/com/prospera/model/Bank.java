package com.prospera.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
//  @NotEmpty(message="Account number cannot be blank")
  private long accNo;
  private String accType;
  
}
