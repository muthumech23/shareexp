package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.model.UserSecure;
import com.mycompany.shareexpense.service.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Muthukumaran Swaminathan
 */
@RestController
@RequestMapping("user")
public class UserController {

	private final Logger	log	= Logger.getLogger(UserController.class);

	@Autowired
	public UserService	userService;

	/*
	 * Authentication API
	 */
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/auth/login")
	public ResponseEntity<User> loginUser(@RequestBody User user) throws Exception {

		log.debug("Enter loginUser --->");
		User userResponse = userService.authenticateLogin(user.getEmail(), user.getPassword());
		
		log.debug("Exit loginUser --->"+userResponse);
		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/auth/logout")
	public ResponseEntity<Void> logout() throws Exception {

		log.debug("Enter logout --->");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/auth/forgot")
	public ResponseEntity<Void> forgotPassword(@RequestBody String emailId) throws Exception {

		log.debug("Enter forgotPassword --->");
		boolean status = userService.forgotPassword(emailId);
		if (!status) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.debug("Exit forgotPassword --->");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/auth/activate")
	public ResponseEntity<User> activate(@RequestBody UserSecure userInput) throws Exception {

		log.debug("Enter activate --->");
		User userResponse = userService.activateAccount(userInput);
		
		log.debug("Exit activate --->");
		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

	/*
	 * USER registration
	 */
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<User> createAccount(@RequestBody User user) throws Exception {

		log.debug("Enter createAccount --->");
		User userResponse = userService.createAccount(user);

		log.debug("Exit createAccount --->");
		return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
	}

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<User> updateAccount(@RequestBody User user) throws Exception {

		log.debug("Enter updateAccount --->");
		User userResponse = userService.updateAccount(user);

		log.debug("Exit updateAccount --->");
		return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{Id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> showAccount(@PathVariable("Id") String Id) throws Exception {

		log.debug("Enter showAccount --->");
		User user = userService.showAccount(Id);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.debug("Exit showAccount --->");
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

}
