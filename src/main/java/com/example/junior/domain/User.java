package com.example.junior.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, updatable = false)
	private Long id;
	
	private String userId;
	
	private String username;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String profileImageUrl;
	
	private Date lastLoginDate;
	
	private Date lastLoginDateDisplay;
	
	private Date joinDate;
	
	private String [] roles;
	
	private String [] authorities;
	
	private boolean isActive;
	
	private boolean isNotLocked;
	
}
