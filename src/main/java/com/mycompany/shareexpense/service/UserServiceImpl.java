
package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.controller.UserController;
import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.model.UserSecure;
import com.mycompany.shareexpense.repository.UserRepository;
import com.mycompany.shareexpense.repository.UserSecureRepository;
import com.mycompany.shareexpense.util.CommonUtil;
import com.mycompany.shareexpense.util.CustomException;
import com.mycompany.shareexpense.util.ErrorConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private final Logger log = Logger.getLogger(UserController.class);

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	@Autowired
	private Environment env;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public UserSecureRepository userSecureRepository;

	@Override
	public User authenticateLogin(	String email,
									String inputPassword) throws CustomException {

		boolean pwdStatus = false;
		
		User user = null;
		try {
			
			UserSecure userSecure = userSecureRepository.findByUserId(email);

			if (userSecure == null) {
				throw new CustomException(ErrorConstants.ERR_LOGIN_FAILED, "The user is not available or email entered is not correct. Please register to start using ShareExpense.");
			}

			if ("R".equalsIgnoreCase(userSecure.getStatus())) {
				pwdStatus = inputPassword.equalsIgnoreCase(CommonUtil.getSHA256Hash(userSecure.getRandomString()));
			} else if ("I".equalsIgnoreCase(userSecure.getStatus())) {
				pwdStatus = CommonUtil.checkPassword(inputPassword, userSecure.getPassword());
				
			} else {
				pwdStatus =  CommonUtil.checkPassword(inputPassword, userSecure.getPassword());
				
			}

			
			if (pwdStatus) {

				user = userRepository.findByEmail(email);

				if (user == null) {
					throw new CustomException(ErrorConstants.ERR_LOGIN_FAILED, "The user is not available or email entered is not correct. Please register to start using ShareExpense.");
				}

				user.setStatus(userSecure.getStatus());

			} else {
				throw new CustomException(ErrorConstants.ERR_LOGIN_FAILED, "The password provided is incorrect.");
			}

			log.info("Secure ID ---> " + userSecure.getUserId());
		} catch (CustomException ce) {
			log.error("userServiceImpl", ce);
			throw ce;
		} catch (Exception exception) {
			log.error("userServiceImpl", exception);
			throw new CustomException(ErrorConstants.ERR_LOGIN_FAILED, "Transaction requested has been failed. Please try again.");
		}
		return user;
	}

	@Override
	public boolean forgotPassword(String email) throws CustomException {

		boolean status = false;

		Date sysDate = new Date();
		try {
			UserSecure userSecure = userSecureRepository.findByUserId(email);

			if (userSecure == null) {
				throw new CustomException(ErrorConstants.ERR_FORGOT_PWD_FAILED, "The user is not available or email entered is not correct. Please register to start using ShareExpense.");
			}

			userSecure.setRandomString(CommonUtil.generateRandomString(6, CommonUtil.Mode.ALPHANUMERIC));
			userSecure.setStatus("R");

			userSecure.setModifiedDate(dateFormat.format(sysDate));
			userSecureRepository.save(userSecure);
			try{
			String emailbody = env.getProperty("mail.template.forgot.pwd");

			log.debug("emailbody --> " + emailbody);
			emailbody = emailbody.replaceAll("<<passwordreset>>", userSecure.getRandomString() + "");
			emailbody = emailbody.replaceAll("<<siteurl>>", "http://shareexpense-shareexp.rhcloud.com/shareexpense/#/home/login");

					
					CommonUtil.sendEmail("Your password reset request", email, emailbody, env);
				}catch(Exception exception){
					log.error(ErrorConstants.ERR_EMAIL_SENT_FAILED, exception);
				}
			status = true;
		} catch (CustomException ce) {
			log.error("userServiceImpl", ce);
			throw ce;
		} catch (Exception exception) {
			log.error("userServiceImpl", exception);
			throw new CustomException(ErrorConstants.ERR_FORGOT_PWD_FAILED, "Transaction requested has been failed. Please try again.");
		}
		return status;
	}

	@Override
	public boolean regenerateActivation(String email) throws CustomException {

		boolean status = false;

		Date sysDate = new Date();
		try {
			UserSecure userSecure = userSecureRepository.findByUserId(email);

			if (userSecure == null) {
				throw new CustomException(ErrorConstants.ERR_ACTIVATION_FAILED, "The user is not available or email entered is not correct. Please register to start using ShareExpense.");
			}

			userSecure.setRandomString(CommonUtil.generateRandomString(6, CommonUtil.Mode.NUMERIC));

			userSecure.setModifiedDate(dateFormat.format(sysDate));
			userSecureRepository.save(userSecure);
			try{
			String emailbody = env.getProperty("mail.template.activate.pwd");

			log.debug("emailbody --> " + emailbody);
			emailbody = emailbody.replaceAll("<<activationcode>>", userSecure.getRandomString()+"");
			emailbody = emailbody.replaceAll("<<siteUrl>>", "http://shareexpense-shareexp.rhcloud.com/shareexpense/#/home/login");

			CommonUtil.sendEmail("Your activation Code", email, emailbody, env);
		}catch(Exception exception){
			log.error(ErrorConstants.ERR_EMAIL_SENT_FAILED, exception);
		}
			status = true;
		} catch (CustomException ce) {
			log.error("userServiceImpl", ce);
			throw ce;
		} catch (Exception exception) {
			log.error("userServiceImpl", exception);
			throw new CustomException(ErrorConstants.ERR_ACTIVATION_FAILED, "Transaction requested has been failed. Please try again.");
		}
		return status;
	}

	@Override
	public User createAccount(User user) throws CustomException {

		User userResponse = null;
		UserSecure userSecure = null;

		Date sysDate = new Date();
		try {
			log.debug(user.getEmail());
			User userExists = userRepository.findByEmail(user.getEmail());

			if (userExists != null) {

				userSecure = userSecureRepository.findByUserId(user.getEmail());

				if (userSecure == null) {

					userSecure = new UserSecure();
					userSecure.setUserId(user.getEmail());
					userSecure.setPassword(CommonUtil.getEncryptedPassword(user.getPassword()));
					userSecure.setCreateDate(dateFormat.format(sysDate));
					userSecure.setModifiedDate(dateFormat.format(sysDate));
					userSecure.setRandomString(CommonUtil.generateRandomString(6, CommonUtil.Mode.NUMERIC));
					userSecure.setStatus("I");
					userSecureRepository.save(userSecure);
					try{
					String emailbody = env.getProperty("mail.template.activate.body");
					emailbody = emailbody.replaceAll("<<activationcode>>", userSecure.getRandomString()+"");
					emailbody = emailbody.replaceAll("<<siteurl>>", "http://shareexpense-shareexp.rhcloud.com/shareexpense/#/home/activation");

					CommonUtil.sendEmail("Welcome to Share Expense", user.getEmail(), emailbody, env);
				}catch(Exception exception){
					log.error(ErrorConstants.ERR_EMAIL_SENT_FAILED, exception);
				}

				} else {
					throw new CustomException(ErrorConstants.ERR_USER_REGISTERATION, "User is already register, please login to continue.");
				}

			} else {
				userResponse = userRepository.save(user);

				userSecure = new UserSecure();
				userSecure.setUserId(user.getEmail());
				userSecure.setPassword(CommonUtil.getEncryptedPassword(user.getPassword()));
				userSecure.setCreateDate(dateFormat.format(sysDate));
				userSecure.setModifiedDate(dateFormat.format(sysDate));
				userSecure.setRandomString(CommonUtil.generateRandomString(6, CommonUtil.Mode.NUMERIC));
				userSecure.setStatus("I");

				userSecureRepository.save(userSecure);
				try{
				String emailbody = env.getProperty("mail.template.activate.body");

				log.debug("emailbody --> " + emailbody);

				emailbody = emailbody.replaceAll("<<activationcode>>", userSecure.getRandomString()+"");
				emailbody = emailbody.replaceAll("<<siteurl>>", "http://shareexpense-shareexp.rhcloud.com/shareexpense/#/home/activation");

				CommonUtil.sendEmail("Welcome to Share Expense", user.getEmail(), emailbody, env);
			}catch(Exception exception){
				log.error(ErrorConstants.ERR_EMAIL_SENT_FAILED, exception);
			}
			}
		} catch (CustomException ce) {
			log.error("userServiceImpl", ce);
			throw ce;
		} catch (Exception exception) {
			log.error("userServiceImpl", exception);
			throw new CustomException(ErrorConstants.ERR_USER_REGISTERATION, "Transaction requested has been failed. Please try again.");
		}
		return userResponse;
	}

	@Override
	public User activateAccount(UserSecure userInput) throws CustomException {

		User user = null;
		try {
			UserSecure userSecure = userSecureRepository.findByUserId(userInput.getUserId());

			if (userSecure == null) {
				throw new CustomException(ErrorConstants.ERR_ACTIVATION_FAILED, "The user is not available or email entered is not correct. Please register to start using ShareExpense.");
			}

			if ( CommonUtil.checkPassword(userInput.getPassword(), userSecure.getPassword())) {

				if (userSecure.getRandomString().equalsIgnoreCase(userInput.getRandomString())) {

					Date sysDate = new Date();

					userSecure.setStatus("A");
					userSecure.setRandomString(null);
					userSecure.setModifiedDate(dateFormat.format(sysDate));
					userSecureRepository.save(userSecure);
				} else {
					throw new CustomException(ErrorConstants.ERR_ACTIVATION_FAILED, "The activation code provided is incorrect.");
				}

				user = userRepository.findByEmail(userInput.getUserId());


				if (user == null) {
					throw new CustomException(ErrorConstants.ERR_ACTIVATION_FAILED, "The user is not available or email entered is not correct. Please register to start using ShareExpense.");
				}

				user.setStatus(userSecure.getStatus());

			} else {
				throw new CustomException(ErrorConstants.ERR_ACTIVATION_FAILED, "The password provided is incorrect.");
			}


		} catch (CustomException ce) {
			log.error("userServiceImpl", ce);
			throw ce;
		} catch (Exception exception) {
			log.error("userServiceImpl", exception);
			throw new CustomException(ErrorConstants.ERR_ACTIVATION_FAILED, "Transaction requested has been failed. Please try again.");
		}
		return user;

	}

	@Override
	public User updateAccount(User userInput) throws CustomException {

		User user = null;
		try {
			UserSecure userSecure = userSecureRepository.findByUserId(userInput.getEmail());

			if (userSecure == null) {
				throw new CustomException(ErrorConstants.ERR_UPDATE_ACCOUNT_FAILED, "The user is not available or email entered is not correct. Please register to start using ShareExpense.");
			}

			if ("R".equalsIgnoreCase(userSecure.getStatus())) {
				userSecure.setStatus("A");
				userSecure.setRandomString(null);
			}

			Date sysDate = new Date();

			userSecure.setPassword(CommonUtil.getEncryptedPassword(userInput.getPassword()));

			userSecure.setModifiedDate(dateFormat.format(sysDate));
			userSecureRepository.save(userSecure);

			user = userRepository.findByEmail(userInput.getEmail());
		} catch (CustomException ce) {
			log.error("userServiceImpl", ce);
			throw ce;
		} catch (Exception exception) {
			log.error("userServiceImpl", exception);
			throw new CustomException(ErrorConstants.ERR_UPDATE_ACCOUNT_FAILED, "Transaction requested has been failed. Please try again.");
		}
		return user;
	}

	@Override
	public User showAccount(String Id) throws CustomException {

		User user = null;
		try {
			user = userRepository.findOne(Id);
		}

		catch (Exception exception) {
			log.error("userServiceImpl", exception);
			throw new CustomException(ErrorConstants.ERR_SHOW_ACCOUNT_DETAILS, "Transaction requested has been failed. Please try again.");
		}
		return user;
	}

	@Override
	public User createFriend(	User user,
								String Id) throws CustomException {

		Date sysDate = new Date();
		List<String> friends = null;
		User friendExist = null;
		try {
			friendExist = userRepository.findByEmail(user.getEmail());
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
			try{
			String emailbody = env.getProperty("mail.template.friend.add.body");

			log.debug("emailbody --> " + emailbody);

			emailbody = emailbody.replaceAll("<<username>>", loggedUser.getName()+"");
			emailbody = emailbody.replaceAll("<<useremail>>", loggedUser.getEmail()+"");
			emailbody = emailbody.replaceAll("<<siteurl>>", "http://shareexpense-shareexp.rhcloud.com/shareexpense/#/home");

			CommonUtil.sendEmail("You are invited to join Share Expense", friendExist.getEmail(), emailbody, env);
		}catch(Exception exception){
			log.error(ErrorConstants.ERR_EMAIL_SENT_FAILED, exception);
		}
		}

		catch (Exception exception) {
			log.error("userServiceImpl", exception);
			throw new CustomException(ErrorConstants.ERR_FRIEND_REGISTERATION, "Transaction requested has been failed. Please try again.");
		}
		return friendExist;
	}

	@Override
	public User updateFriend(User user) throws CustomException {

		User friendUpdated = null;
		try {
			friendUpdated = userRepository.save(user);
		}

		catch (Exception exception) {
			log.error("userServiceImpl", exception);
			throw new CustomException(ErrorConstants.ERR_FRIEND_UPDATE, "Transaction requested has been failed. Please try again.");
		}
		return friendUpdated;
	}

	@Override
	public List<User> findByFriend(String userId) throws CustomException {

		List<User> friendList = null;
		try {
			User user = userRepository.findOne(userId);

			if (user.getFriends() != null) {
				Iterable<User> friends = userRepository.findAll(user.getFriends());
				friendList = CommonUtil.toList(friends);

			} else {
				friendList = new ArrayList<>();
			}

			friendList.add(user);
		}

		catch (Exception exception) {
			log.error("userServiceImpl", exception);
			throw new CustomException(ErrorConstants.ERR_FRIEND_SHOW_DETAIL, "Transaction requested has been failed. Please try again.");
		}
		return friendList;
	}

	@Override
	public void deleteFriend(	String userId,
								String Id) throws CustomException {

		try {
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

		catch (Exception exception) {
			log.error("userServiceImpl", exception);
			throw new CustomException(ErrorConstants.ERR_FRIEND_DELETION, "Transaction requested has been failed. Please try again.");
		}
	}

}
