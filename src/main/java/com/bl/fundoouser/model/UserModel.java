package com.bl.fundoouser.model;
import java.io.File;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.multipart.MultipartFile;

import com.bl.fundoouser.DTO.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  
 * Purpose:Model for the User data Registration
 * 
 * @author: Pavan Kumar G V 
 * @version: 4.15.1.RELEASE
 * 
 **/ 
@Entity
@Table(name = "userdetails")
@Data
@NoArgsConstructor
public class UserModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private String emailId;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isActive;
	private boolean isDeleted;
	private String dob;
	private String Phoneno;
	@Column(length = 1000)
	private String profilePic;
	
	public UserModel(UserDto userDto) {
		this.name = userDto.getName();
		this.emailId = userDto.getEmailId();
		this.password = userDto.getPassword();
		this.isActive = userDto.isActive();
		this.isDeleted = userDto.isDeleted();
		this.dob = userDto.getDob();
		this.Phoneno = userDto.getPhoneno();
	}
}
