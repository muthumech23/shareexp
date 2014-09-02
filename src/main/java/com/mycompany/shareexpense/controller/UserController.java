/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.service.UserService;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
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

    private final Log log = LogFactory.getLog(UserController.class);

    @Autowired
    public UserService userService;

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<Void> submitAccount(@RequestBody User user) throws Exception {

        log.debug("Inside SubmitAccount --->");
        log.debug(user.getEmail());
        userService.saveUser(user);

        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
        return responseEntity;
    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/auth/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) throws Exception {

        User userResponse = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if(userResponse == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/auth/logout")
    public ResponseEntity<Void> logout() throws Exception {

        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "/{Id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> showAccount(@PathVariable("Id") String Id) throws Exception {

        User user = userService.showUser(Id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Iterable<User> allUsers() throws Exception {
        return userService.findAll();
    }
    
    

}