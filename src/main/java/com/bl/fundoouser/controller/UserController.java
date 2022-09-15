package com.bl.fundoouser.controller;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.bl.fundoouser.DTO.UserDto;
import com.bl.fundoouser.model.UserModel;
import com.bl.fundoouser.service.IUserService;
import com.bl.fundoouser.util.Response;

/**
 * Purpose: User Controller to process User Data APIs.
 * @version: 4.15.1.RELEASE
 * @author: Pavan Kumar G V  
 */
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	IUserService userService;
	
	/**
	 * Purpose: Create User
	 * @Param: userDto
	 */
	@PostMapping("/adduser")
	public ResponseEntity<Response> addUser(@Valid@RequestBody UserDto userDto) {
		UserModel userModel = userService.addUser(userDto);
		Response response = new Response("User inserted successfully", 200, userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);	
	}
	
	/**
	 * Purpose: update User
	 * @Param: userDto,id,token
	 */
	@PutMapping("/updateuser/{id}")
	public ResponseEntity<Response> updateUser(@Valid@RequestBody UserDto userDto,@PathVariable Long id,@RequestHeader String token) {
		UserModel userModel = userService.updateUser(userDto,id,token);
		Response response = new Response("User updated successfully", 200, userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Purpose: get all Users
	 * @Param: token
	 */
	@GetMapping("/getallusers")
	public ResponseEntity<Response> getAllUsers(@RequestHeader String token) {
		List<UserModel> userModel = userService.getAllUsers(token);
		Response response = new Response("Fetching all users successfully", 200, userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Purpose: get User by id
	 * @Param: id,token
	 */
	@GetMapping("/getuserbyid/{id}")
	public ResponseEntity<Response> getUserById(@PathVariable Long id,@RequestHeader String token) {
		Optional<UserModel> userModel = userService.getUserById(id,token);
		Response response = new Response("Fectching user by id successfully", 200, userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);	
	}
	
	/**
	 * Purpose:Login method 
	 * @Param: email and password
	 */
	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestParam String emailId,@RequestParam String password) {
		Response userModel = userService.login(emailId,password);
		Response response = new Response("User login successfull", 200, userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Purpose:TO reset the password
	 * @Param: emailid
	 */
	@PostMapping("/resetpassword")
	public ResponseEntity<Response> resetPassword(@RequestParam String emailId) {
		Response userModel = userService.resetPassword(emailId);
		Response response = new Response("Reset password successfull", 200, userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Purpose:To change the password
	 * @Param: token and password
	 */
	@PutMapping("/changepassword/{token}")
	public ResponseEntity<Response> changePassword(@PathVariable String token, @RequestParam String password) {
		UserModel userModel = userService.changePassword(token,password);
		Response response = new Response("Password changed successfully", 200, userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Purpose:Validate user
	 * @Param: token
	 */
	@GetMapping("/validateuser/{token}")
	public Boolean validateUser(@PathVariable String token) {
		return userService.validateUser(token);
	}
	
	/**
	 * Purpose:To delete the user
	 * @Param: token and id
	 */
	@DeleteMapping("deleteuser/{id}")
	public ResponseEntity<Response> deleteUser(@PathVariable Long id,@RequestHeader String token) {
		Response userModel = userService.deleteUser(id,token);
		Response response = new Response("User deleted successfully", 200, userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Purpose: To restore the user
	 * @Param: token and id
	 */
	@GetMapping("restoreuser/{id}")
	public ResponseEntity<Response> restoreUser(@PathVariable Long id,@RequestHeader String token) {
		Response userModel = userService.restoreUser(id,token);
		Response response = new Response("User restored successfully", 200, userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Purpose:To delete the user permanently
	 * @Param: token and id
	 */
	@DeleteMapping("permanentlydeleteuser/{id}")
	public ResponseEntity<Response> permanentDelete(@PathVariable Long id,@RequestHeader String token) {
		Response userModel = userService.permanentDelete(id,token);
		Response response = new Response("User deleted permanently ", 200, userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Purpose:To add profile pic
	 * @Param: id and profilepic
	 */
	@PostMapping("/addprofilepic/{id}")
	public ResponseEntity<Response> addProfilePic(@PathVariable Long id,@RequestParam MultipartFile profilePic) throws IOException {
		Response userModel = userService.addProfilePic(id,profilePic);
		Response response = new Response("Profile pic uploaded sucessfully ", 200, userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
