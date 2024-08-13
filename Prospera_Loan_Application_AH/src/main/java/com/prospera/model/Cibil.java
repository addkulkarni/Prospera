package com.prospera.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Cibil 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cibilid;
    private int cibilscore;
    private Date timeStamp;
    private String cibilStatus;
    
}
