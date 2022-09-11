package com.bl.fundoouser.DTO;
import java.io.File;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserDto {
	private String name;
	private String emailId;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isActive;
	private boolean isDeleted;
	private String dob;
	private String Phoneno;

}
