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
public class User {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private int uid;
	private String username;
	private String firstname;
	private String lastname;
	private String password;
	private String UserType;
	private String UserEmail;
	@Lob
	@Column(length=999999999)
	private byte[] photo;

}
