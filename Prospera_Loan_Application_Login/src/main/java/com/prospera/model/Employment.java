package com.prospera.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Employment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int eid;
	@NotBlank(message="Organisation cannot be blank")
	private String organization;
	private String type;
	private String status;
}
