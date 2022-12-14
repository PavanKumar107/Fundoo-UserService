package com.bl.fundoouser.service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bl.fundoouser.DTO.UserDto;
import com.bl.fundoouser.exception.UserNotFoundException;
import com.bl.fundoouser.model.UserModel;
import com.bl.fundoouser.repository.UserRepository;
import com.bl.fundoouser.util.Response;
import com.bl.fundoouser.util.TokenUtil;


/**
 *  
 * Purpose:Service implementation of the User
 * @author: Pavan Kumar G V 
 * @version: 4.15.1.RELEASE
 * 
 */

@Service
public class UserService implements IUserService  {

	@Autowired
	UserRepository userRepository;

	@Autowired
	MailService mailService;

	@Autowired
	TokenUtil tokenUtil;

	@Autowired
	PasswordEncoder passwordEncoder;

	//Purpose:To add user to the database
	@Override
	public UserModel addUser(UserDto userDto) {
		UserModel model = new UserModel(userDto);
		model.setCreatedAt(LocalDateTime.now());
		userRepository.save(model);
		String body = "User added successfully with userId"+model.getUserId();
		String subject = "User added Successfull";
		mailService.send(model.getEmailId(), subject, body);
		return model;
	}

	//Purpose:To update user details
	@Override
	public UserModel updateUser(UserDto userDto, Long userId,String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			Optional<UserModel>isUserPresent = userRepository.findById(userId);
			if(isUserPresent.isPresent()) {
				isUserPresent.get().setName(userDto.getName());
				isUserPresent.get().setEmailId(userDto.getEmailId());
				isUserPresent.get().setPassword(passwordEncoder.encode(userDto.getPassword()));
				isUserPresent.get().setUpdatedAt(LocalDateTime.now());
				isUserPresent.get().setDob(userDto.getDob());
				isUserPresent.get().setPhoneno(userDto.getPhoneno());
				userRepository.save(isUserPresent.get());
				String body = "User updated successfully with userId"+isUserPresent.get().getUserId();
				String subject = "User updated Successfull";
				mailService.send(isUserPresent.get().getEmailId(), subject, body);
				return isUserPresent.get();
			}
			throw new UserNotFoundException(400,"User not present");
		}
		throw new UserNotFoundException(400,"Token not find");
	}

	//Purpose:To fetch all the user details
	@Override
	public List<UserModel> getAllUsers(String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			List<UserModel> getAllUsers = userRepository.findAll();
			if(getAllUsers.size()>0) {
				return getAllUsers;
			}else {
				throw new UserNotFoundException(400,"User not present");
			}
		}
		throw new UserNotFoundException(400,"Token not find");
	}

	//Purpose:Ability to fetch user details 
	@Override
	public Optional<UserModel> getUserById(Long userId,String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			return userRepository.findById(userId);
		}
		throw new UserNotFoundException(400,"Token not find");
	}

	//Purpose:Login service
	@Override
	public Response login(String emailId, String password) {
		Optional<UserModel> isEmailPresent = userRepository.findByEmailId(emailId);
		if(isEmailPresent.isPresent()){
			if(isEmailPresent.get().getPassword().equals(password)){
				String token = tokenUtil.createToken(isEmailPresent.get().getUserId());
				return new Response("User login succesfull",200,token);
			}
			throw new UserNotFoundException(400,"Invald credentials");
		}
		throw new UserNotFoundException(400,"invalid emailid");
	}

	//Purpose:Service for reset password
	@Override
	public Response resetPassword(String emailId) {
		Optional<UserModel> isMailPresent = userRepository.findByEmailId(emailId);
		if (isMailPresent.isPresent()){
			String token = tokenUtil.createToken(isMailPresent.get().getUserId());
			String url = "http://localhost:8089/admin/resetpassword "+token;
			String subject = "reset password Successfully";
			mailService.send(isMailPresent.get().getEmailId(), subject, url);
			return new Response("Reset password",200,token);
		}
		throw new UserNotFoundException(400, "EmailNOtFound");
	}

	//Purpose:Service for changing password
	@Override
	public UserModel changePassword(String token, String password) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			isTokenPresent.get().setPassword(passwordEncoder.encode(password));
			userRepository.save(isTokenPresent.get());
			String body = "Password changed successfully with userId"+isTokenPresent.get().getUserId();
			String subject = "Password changed Successfully";
			mailService.send(isTokenPresent.get().getEmailId(), subject, body);
			return isTokenPresent.get();
		}
		throw new UserNotFoundException(400,"Token not find");
	}

	//Purpose:Service for validate user
	@Override
	public Boolean validateUser(String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent())
			return true;
		throw new UserNotFoundException(400, "Token not found");
	}

	//Purpose:Service for valid email
	@Override
	public Boolean validateEmail(String emailId) {
		Optional<UserModel> isEmailPresent = userRepository.findByEmailId(emailId);
		if (isEmailPresent.isPresent()) {
			return true;
		}else {
			return false;	
		}
	}

	//Purpose:Service for deleting user
	@Override
	public Response deleteUser(Long userId,String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			Optional<UserModel> isIdPresent = userRepository.findById(userId);
			if(isIdPresent.isPresent()) {
				isIdPresent.get().setActive(false);
				isIdPresent.get().setDeleted(true);
				userRepository.save(isIdPresent.get());
				return new Response("success",200,isIdPresent.get());
			}else {
				throw new UserNotFoundException(400,"User not present");
			}
		}
		throw new UserNotFoundException(400, "Token not found");	
	}

	//Purpose:Service to restore users
	@Override
	public Response restoreUser(Long userId,String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			Optional<UserModel> isIdPresent = userRepository.findById(userId);
			if(isIdPresent.isPresent()) {
				isIdPresent.get().setActive(true);
				isIdPresent.get().setDeleted(false);
				userRepository.save(isIdPresent.get());
				return new Response("success",200,isIdPresent.get());
			}else {
				throw new UserNotFoundException(400,"User not present");
			}
		}
		throw new UserNotFoundException(400, "Token not found");	
	}

	//Purpose:Service to delete user permanently
	@Override
	public Response permanentDelete(Long userId, String token) {
		Long userId1 = tokenUtil.decodeToken(token);
		Optional<UserModel> isUserPresent = userRepository.findById(userId1);
		if(isUserPresent.isPresent()) {
			Optional<UserModel> isIdPresent = userRepository.findById(userId);
			if(isIdPresent.isPresent()) {
				userRepository.delete(isIdPresent.get());
				String body = "User deleted successfully with userId"+isUserPresent.get().getUserId();
				String subject = "User deleted Successfull";
				mailService.send(isUserPresent.get().getEmailId(), subject, body);
				return new Response("Success", 200, isIdPresent.get());
			} else {
				throw new UserNotFoundException(400, "User not found");
			}		
		}
		throw new UserNotFoundException(400, "Invalid token");
	}

	//Purpose:Service to set profile pic
	@Override
	public Response addProfilePic(Long id,MultipartFile profilePic) throws IOException {
		Optional<UserModel> isIdPresent = userRepository.findById(id);
		if(isIdPresent.isPresent()) {
			isIdPresent.get().setProfilePic(String.valueOf(profilePic.getBytes()));
			userRepository.save(isIdPresent.get());
			return new Response("Success", 200, isIdPresent.get());
		}
		throw new UserNotFoundException(400, "User not found");
	}
}