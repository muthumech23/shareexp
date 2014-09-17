/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.User;
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

    private final Logger log = Logger.getLogger (UserController.class);

    @Autowired
    public UserService userService;

    /*
     * Authentication API
     */
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/auth/login")
    public ResponseEntity<User> loginUser (@RequestBody User user) throws Exception {

        log.info ("Inside loginUser");
        User userResponse = userService.findByEmailAndPassword (user.getEmail (), user.getPassword ());
        if (userResponse == null) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<> (userResponse, HttpStatus.OK);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/auth/logout")
    public ResponseEntity<Void> logout () throws Exception {

        return new ResponseEntity<> (HttpStatus.OK);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/auth/forgot")
    public ResponseEntity<Void> forgotPassword (@RequestBody String emailId)
            throws Exception {

        boolean status = userService.forgotPassword (emailId);
        if (!status) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<> (HttpStatus.OK);
    }


    /*
     * USER registration
     */
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<User> createAccount (@RequestBody User user) throws Exception {

        User userResponse = userService.createAccount (user);

        return new ResponseEntity<> (userResponse, HttpStatus.CREATED);
    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    public ResponseEntity<User> updateAccount (@RequestBody User user) throws Exception {

        User userResponse = userService.updateAccount (user);

        return new ResponseEntity<> (userResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{Id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> showAccount (@PathVariable("Id") String Id)
            throws Exception {

        User user = userService.showAccount (Id);
        if (user == null) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<> (user, HttpStatus.OK);
    }

}
