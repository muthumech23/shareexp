package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.model.UserSecure;
import com.mycompany.shareexpense.service.UserService;
import com.mycompany.shareexpense.util.CustomException;
import com.mycompany.shareexpense.util.ErrorConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController extends AbstractController {

    private final Logger log = Logger.getLogger(UserController.class);

    @Autowired
    public UserService userService;

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST,
            value = "/auth/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) throws CustomException {

        User userResponse = null;
        try {
            userResponse = userService.authenticateLogin(user.getEmail(), user.getPassword());

        } catch (CustomException ce) {
            log.error("/auth/login", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/auth/login", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET,
            value = "/auth/logout")
    public ResponseEntity<Void> logout() throws CustomException {

        try {
            log.debug("completed logout --->");
        } catch (Exception e) {
            log.error("/auth/logout", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST,
            value = "/auth/forgot")
    public ResponseEntity<Void> forgotPassword(@RequestBody String emailId) throws CustomException {

        try {
            boolean status = userService.forgotPassword(emailId);
            if (!status) {
                throw new CustomException(ErrorConstants.ERR_FORGOT_PWD_FAILED, "Transaction to reset password failed. Please try again.");
            }
        } catch (CustomException ce) {
            log.error("/auth/forgot", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/auth/forgot", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST,
            value = "/auth/activationcode")
    public ResponseEntity<Void> regenerateActivationCode(@RequestBody String emailId) throws CustomException {

        try {
            boolean status = userService.regenerateActivation(emailId);
            if (!status) {
                throw new CustomException(ErrorConstants.ERR_FORGOT_PWD_FAILED, "Transaction to reset password failed. Please try again.");
            }
        } catch (CustomException ce) {
            log.error("/auth/activationcode", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/auth/activationcode", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST,
            value = "/auth/activate")
    public ResponseEntity<User> activate(@RequestBody UserSecure userInput) throws CustomException {

        User userResponse = null;

        try {
            userResponse = userService.activateAccount(userInput);
        } catch (CustomException ce) {
            log.error("/auth/activate", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/auth/activate", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    /*
     * USER registration
     */
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<User> createAccount(@RequestBody User user) throws CustomException {

        User userResponse = null;
        try {
            userResponse = userService.createAccount(user);

        } catch (CustomException ce) {
            log.error("/user/creation", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/user/creation", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT)
    public ResponseEntity<User> updateAccount(@RequestBody User user) throws CustomException {

        User userResponse = null;
        try {
            userResponse = userService.updateAccount(user);

        } catch (CustomException ce) {
            log.error("/user/update", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/user/update", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{Id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> showAccount(@PathVariable("Id") String Id) throws CustomException {

        User user = null;
        try {
            user = userService.showAccount(Id);
            if (user == null) {
                throw new CustomException(ErrorConstants.ERR_SHOW_ACCOUNT_DETAILS, "Transaction to retrieve user details failed. Please try again.");
            }

            log.debug("Exit showAccount --->");
        } catch (CustomException ce) {
            log.error("/user/show", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/user/show", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
