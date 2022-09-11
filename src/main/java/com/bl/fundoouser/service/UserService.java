package com.bl.fundoouser.service;
import java.io.File;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.User;
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

	//Ability to add user to the database
	@Override
	public UserModel addUser(UserDto userDto) {
		UserModel model = new UserModel(userDto);
		userRepository.save(model);
		String body = "User added successfully with userId"+model.getId();
		String subject = "User added Successfull";
		mailService.send(model.getEmailId(), subject, body);
		return model;
	}

	//Ability to update user details by id and token
	@Override
	public UserModel updateUser(UserDto userDto, Long id,String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			Optional<UserModel>isUserPresent = userRepository.findById(id);
			if(isUserPresent.isPresent()) {
				isUserPresent.get().setName(userDto.getName());
				isUserPresent.get().setEmailId(userDto.getEmailId());
				isUserPresent.get().setPassword(userDto.getPassword());
				isUserPresent.get().setUpdatedAt(userDto.getUpdatedAt().now());
				isUserPresent.get().setDob(userDto.getDob());
				isUserPresent.get().setPhoneno(userDto.getPhoneno());
				userRepository.save(isUserPresent.get());
				String body = "User updated successfully with userId"+isUserPresent.get().getId();
				String subject = "User updated Successfull";
				mailService.send(isUserPresent.get().getEmailId(), subject, body);
				return isUserPresent.get();
			}
			throw new UserNotFoundException(400,"User not present");
		}
		throw new UserNotFoundException(400,"Token not find");
	}

	//Ability to fetch all the user details by token
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

	//Ability to fetch user details by id
	@Override
	public Optional<UserModel> getUserById(Long id,String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			return userRepository.findById(id);
		}
		throw new UserNotFoundException(400,"Token not find");
	}

	@Override
	public Response login(String emailId, String password) {
		Optional<UserModel> isEmailPresent = userRepository.findByEmailId(emailId);
		if(isEmailPresent.isPresent()){
			if(isEmailPresent.get().getPassword().equals(password)){
				String token = tokenUtil.createToken(isEmailPresent.get().getId());
				return new Response("User login succesfull",200,token);
			}
			throw new UserNotFoundException(400,"Invald credentials");
		}
		throw new UserNotFoundException(400,"invalid emailid");
	}

	@Override
	public Response resetPassword(String emailId) {
		Optional<UserModel> isMailPresent = userRepository.findByEmailId(emailId);
		if (isMailPresent.isPresent()){
			String token = tokenUtil.createToken(isMailPresent.get().getId());
			String url = "http://localhost:8089/admin/resetpassword "+token;
			String subject = "reset password Successfully";
			mailService.send(isMailPresent.get().getEmailId(), subject, url);
			return new Response("Reset password",200,token);
		}
		throw new UserNotFoundException(400, "EmailNOtFound");
	}

	@Override
	public UserModel changePassword(String token, String password) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			isTokenPresent.get().setPassword(passwordEncoder.encode(password));
			userRepository.save(isTokenPresent.get());
			String body = "Password changed successfully with userId"+isTokenPresent.get().getId();
			String subject = "Password changed Successfully";
			mailService.send(isTokenPresent.get().getEmailId(), subject, body);
			return isTokenPresent.get();
		}
		throw new UserNotFoundException(400,"Token not find");
	}

	@Override
	public Boolean validateUser(String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent())
			return true;
		throw new UserNotFoundException(400, "Token not found");
	}

	//Ability to delete the user by id
	@Override
	public Response deleteUser(Long id,String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			Optional<UserModel> isIdPresent = userRepository.findById(id);
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

	@Override
	public Response restoreUser(Long id,String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			Optional<UserModel> isIdPresent = userRepository.findById(id);
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

	@Override
	public Response permanentDelete(Long id, String token) {
		Long userId = tokenUtil.decodeToken(token);
		Optional<UserModel> isUserPresent = userRepository.findById(userId);
		if(isUserPresent.isPresent()) {
			Optional<UserModel> isIdPresent = userRepository.findById(id);
			if(isIdPresent.isPresent()) {
				userRepository.delete(isIdPresent.get());
				return new Response("Success", 200, isIdPresent.get());
			} else {
				throw new UserNotFoundException(400, "User not found");
			}		
		}
		throw new UserNotFoundException(400, "Invalid token");
	}
}