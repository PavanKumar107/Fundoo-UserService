package com.bl.fundoouser.service;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.bl.fundoouser.DTO.UserDto;
import com.bl.fundoouser.model.UserModel;
import com.bl.fundoouser.util.Response;

/**
 *  
 * Purpose:User Service Interface
 * @author: Pavan Kumar G V 
 * @version: 4.15.1.RELEASE
 * 
 **/

//all the method for the service are registered here
public interface IUserService {

	UserModel addUser(UserDto userDto);
	
	UserModel updateUser(UserDto userDto,Long id,String token);
	
	List<UserModel> getAllUsers(String token);
	
	Optional<UserModel> getUserById(Long id,String token);
	
	Response login(String emailId, String password);
	
	Response resetPassword(String emailId);
	
	UserModel changePassword(String token, String password);

	Response deleteUser(Long id, String token);

	Boolean validateUser(String token);

	Response restoreUser(Long id, String token);

	Response permanentDelete(Long id, String token);

	Response addProfilePic(Long id, MultipartFile profilePic) throws IOException;

	Boolean validateEmail(String token);
}
