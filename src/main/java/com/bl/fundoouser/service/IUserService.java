package com.bl.fundoouser.service;
import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.bl.fundoouser.DTO.UserDto;
import com.bl.fundoouser.model.UserModel;
import com.bl.fundoouser.util.Response;

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
	
////	UserModel setprofilepic( File profilefile,Long id,String token);
//	Response uploadProfilePic(MultipartFile multipartFile, String token);

	
}
