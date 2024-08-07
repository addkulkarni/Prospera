package com.prospera.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Cibil 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cibilid;
    private int cibilscore;
    private String cibilStatus;
   
}
