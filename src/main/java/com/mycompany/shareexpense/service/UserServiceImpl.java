
package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.controller.UserController;
import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.model.UserSecure;
import com.mycompany.shareexpense.repository.UserRepository;
import com.mycompany.shareexpense.repository.UserSecureRepository;
import com.mycompany.shareexpense.util.CommonUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private final Logger		log			= Logger.getLogger(UserController.class);

	DateFormat					dateFormat	= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	@Autowired
	private Environment			env;

	@Autowired
	public UserRepository		userRepository;

	@Autowired
	public UserSecureRepository	userSecureRepository;

	@Override
	public User authenticateLogin(	String email,
									String password) throws Exception {

		String passwordSecure = null;
		User user = null;
		UserSecure userSecure = userSecureRepository.findByUserId(email);


		if (userSecure == null) {
			throw new Exception("Login failed. User Id or Password is incorrect.");
		}

		if ("R".equalsIgnoreCase(userSecure.getStatus())) {
			passwordSecure = userSecure.getRandomString();
		} else if ("I".equalsIgnoreCase(userSecure.getStatus())) {
			passwordSecure = userSecure.getPassword();
			password = Arrays.toString(CommonUtil.getEncryptedPassword(password, email.getBytes()));
		} else {
			passwordSecure = userSecure.getPassword();
			password = Arrays.toString(CommonUtil.getEncryptedPassword(password, email.getBytes()));
		}

		if (password.equalsIgnoreCase(passwordSecure)) {

			user = userRepository.findByEmail(email);

			if (user == null) {
				throw new Exception("User is not available");
			}

			user.setStatus(userSecure.getStatus());

		} else {
			throw new Exception("Login failed. User Id or Password is incorrect.");
		}

		log.info("Secure ID ---> " + userSecure.getUserId());
		return user;
	}

	@Override
	public boolean forgotPassword(String email) throws Exception {

		boolean status = false;

		Date sysDate = new Date();

		UserSecure userSecure = userSecureRepository.findByUserId(email);

		if (userSecure == null) {
			throw new Exception("User Id or Password is incorrect.");
		}

		userSecure.setRandomString(CommonUtil.generateRandomString(6, CommonUtil.Mode.ALPHANUMERIC));
		userSecure.setStatus("R");

		userSecure.setModifiedDate(dateFormat.format(sysDate));
		userSecureRepository.save(userSecure);


		String emailbody = env.getProperty("mail.template.forgot.pwd");
		
		log.debug("emailbody --> "+emailbody);
		emailbody = emailbody.replaceAll("<<passwordreset>>", userSecure.getRandomString());
		emailbody = emailbody.replaceAll("<<siteUrl>>", "http://localhost:8580/shareexpense/#/home/login");

		CommonUtil.sendEmail("Reset Password Request", email, emailbody, env);
		status = true;
		return status;
	}

	@Override
	public User createAccount(User user) throws Exception {

		User userResponse = null;
		UserSecure userSecure = null;

		Date sysDate = new Date();

		log.debug(user.getEmail());
		User userExists = userRepository.findByEmail(user.getEmail());

		if (userExists != null) {

			userSecure = userSecureRepository.findByUserId(user.getEmail());

			if (userSecure == null) {

				userSecure = new UserSecure();
				userSecure.setUserId(user.getEmail());
				userSecure.setPassword(Arrays.toString(CommonUtil.getEncryptedPassword(user.getPassword(), userExists
								.getEmail().getBytes())));
				userSecure.setCreateDate(dateFormat.format(sysDate));
				userSecure.setModifiedDate(dateFormat.format(sysDate));
				userSecure.setRandomString(CommonUtil.generateRandomString(6, CommonUtil.Mode.NUMERIC));
				userSecure.setStatus("I");
				userSecureRepository.save(userSecure);

				String emailbody = env.getProperty("mail.template.activate.body");
				emailbody = emailbody.replaceAll("<<activationcode>>", userSecure.getRandomString());
				emailbody = emailbody.replaceAll("<<siteurl>>", "http://localhost:8580/shareexpense/#/home/activation");

				CommonUtil.sendEmail("User Registration", user.getEmail(), emailbody, env);

			} else {
				throw new Exception("User already register, please login to continue");
			}

		} else {
			userResponse = userRepository.save(user);

			userSecure = new UserSecure();
			userSecure.setUserId(user.getEmail());
			userSecure.setPassword(Arrays.toString(CommonUtil.getEncryptedPassword(user.getPassword(), userResponse
							.getEmail().getBytes())));
			userSecure.setCreateDate(dateFormat.format(sysDate));
			userSecure.setModifiedDate(dateFormat.format(sysDate));
			userSecure.setRandomString(CommonUtil.generateRandomString(6, CommonUtil.Mode.NUMERIC));
			userSecure.setStatus("I");

			userSecureRepository.save(userSecure);


			String emailbody = env.getProperty("mail.template.activate.body");
			
			log.debug("emailbody --> "+emailbody);
			
			emailbody = emailbody.replaceAll("<<activationcode>>", userSecure.getRandomString());
			emailbody = emailbody.replaceAll("<<siteurl>>", "http://localhost:8580/shareexpense/#/home/activation");

			CommonUtil.sendEmail("User Registration", user.getEmail(), emailbody, env);

		}

		return userResponse;
	}


	@Override
	public User activateAccount(UserSecure userInput) throws Exception {

		String password =
						Arrays.toString(CommonUtil.getEncryptedPassword(userInput.getPassword(), userInput.getUserId()
										.getBytes()));

		UserSecure userSecure = userSecureRepository.findByUserIdAndPassword(userInput.getUserId(), password);

		if (userSecure == null) {
			throw new Exception("Login failed. User Id or Password is incorrect.");
		}


		if (userSecure.getRandomString().equalsIgnoreCase(userInput.getRandomString())) {

			Date sysDate = new Date();

			userSecure.setStatus("A");
			userSecure.setRandomString(null);
			userSecure.setModifiedDate(dateFormat.format(sysDate));
			userSecureRepository.save(userSecure);
		}else{
			throw new Exception("Invalid activation code");
		}

		User user = userRepository.findByEmail(userInput.getUserId());

		return user;

	}

	@Override
	public User updateAccount(User userInput) throws Exception {


		UserSecure userSecure = userSecureRepository.findByUserId(userInput.getEmail());

		if (userSecure == null) {
			throw new Exception("User Id or Password is incorrect.");
		}

		if ("R".equalsIgnoreCase(userSecure.getStatus())) {
			userSecure.setStatus("A");
			userSecure.setRandomString(null);
		}

		Date sysDate = new Date();

		userSecure.setPassword(Arrays.toString(CommonUtil.getEncryptedPassword(userInput.getPassword(), userInput
						.getEmail().getBytes())));

		userSecure.setModifiedDate(dateFormat.format(sysDate));
		userSecureRepository.save(userSecure);

		User user = userRepository.findByEmail(userInput.getEmail());

		return user;
	}

	@Override
	public User showAccount(String Id) throws Exception {

		return userRepository.findOne(Id);

	}

	@Override
	public User createFriend(	User user,
								String Id) throws Exception {

		Date sysDate = new Date();
		List<String> friends = null;

		User friendExist = userRepository.findByEmail(user.getEmail());
		if (friendExist == null) {
			user.setModifiedDate(dateFormat.format(sysDate));
			user.setCreateDate(dateFormat.format(sysDate));

			friends = user.getFriends() != null ? user.getFriends() : new ArrayList<String>();
			friends.add(Id);

			friendExist = userRepository.save(user);
		}

		User loggedUser = userRepository.findOne(Id);

		if (loggedUser.getFriends() != null) {
			loggedUser.getFriends().add(friendExist.getId());
		} else {
			List<String> loggedFriends = new ArrayList<>();
			loggedFriends.add(friendExist.getId());
			loggedUser.setFriends(loggedFriends);
		}
		loggedUser.setModifiedDate(dateFormat.format(sysDate));
		userRepository.save(loggedUser);

		if (friendExist.getFriends() != null) {
			friendExist.getFriends().add(Id);
		} else {
			List<String> friendsList = new ArrayList<>();
			friendsList.add(Id);
			friendExist.setFriends(friendsList);
		}

		friendExist.setModifiedDate(dateFormat.format(sysDate));
		userRepository.save(friendExist);
		
		String emailbody = env.getProperty("mail.template.friend.add.body");
		
		log.debug("emailbody --> "+emailbody);
		
		emailbody = emailbody.replaceAll("<<username>>", loggedUser.getName());
		emailbody = emailbody.replaceAll("<<useremail>>", loggedUser.getEmail());
		emailbody = emailbody.replaceAll("<<siteurl>>", "http://localhost:8580/shareexpense/#/home");

		CommonUtil.sendEmail("Friend Addition", friendExist.getEmail(), emailbody, env);
		
		return friendExist;
	}

	@Override
	public User updateFriend(User user) throws Exception {

		User friendUpdated = userRepository.save(user);

		return friendUpdated;
	}

	@Override
	public List<User> findByFriend(String userId) throws Exception {

		List<User> friendList = null;
		User user = userRepository.findOne(userId);

		if (user.getFriends() != null) {
			Iterable<User> friends = userRepository.findAll(user.getFriends());
			friendList = CommonUtil.toList(friends);

		} else {
			friendList = new ArrayList<>();
		}

		friendList.add(user);
		return friendList;
	}

	@Override
	public void deleteFriend(	String userId,
								String Id) throws Exception {

		Date sysDate = new Date();

		User user = userRepository.findOne(userId);
		List<String> friends = user.getFriends();

		if (friends != null) {
			friends.remove(Id);

			user.setFriends(friends);

			user.setModifiedDate(dateFormat.format(sysDate));

			userRepository.save(user);
		}
	}

}
