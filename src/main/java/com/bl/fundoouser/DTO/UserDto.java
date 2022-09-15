package com.bl.fundoouser.DTO;
import java.io.File;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 *  
 * Purpose:DTO for the users data
 * 
 * @author: Pavan Kumar G V 
 * @version: 4.15.1.RELEASE
 * 
 **/ 
@Data
public class UserDto {

	@Pattern(regexp = "^[A-Z]{1,}[a-z\\s]{2,}$", message = "Name is invalid, first letter should be uppercase!")
	private String name;

	@Pattern(regexp = "([a-zA-Z0-9./.-])+.([a-zA-Z0-9./.-])?@([a-z]{2,7})+.([a-z]{2,4})+.([a-z]{2,4})?", message = "Valid format is: abc.xyz@gmail.com")
	private String emailId;

	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",message ="Enter valid password")
	private String password;

	private boolean isActive;

	private boolean isDeleted;

	@NotNull
	private String dob;

	@Pattern(regexp = "^[0-9]{10}$", message = "Enter valid phoneno")
	private String Phoneno;
}
