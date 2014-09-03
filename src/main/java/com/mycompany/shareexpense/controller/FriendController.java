/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.service.UserService;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("friend")
public class FriendController {

    private final Log log = LogFactory.getLog(FriendController.class);

    @Autowired
    public UserService userService;

    @RequestMapping(value = "/{Id}", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<User> createFriend(@PathVariable("Id") String Id, @RequestBody User user) throws Exception {

        log.debug("Inside SubmitAccount --->");
        log.debug(user.getEmail());
        User userResponse = userService.createFriend(user, Id);

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);

    }

    @RequestMapping(value = "/all/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<User> allFriends(@PathVariable("userId") String userId) throws Exception {
        return userService.findByFriend(userId);
    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    public ResponseEntity<User> updateFriend(@RequestBody User user) throws Exception {

        log.debug("Inside updateFriend --->");
        log.debug(user.getEmail());
        User friendResponse = userService.updateUser(user);

        return new ResponseEntity<>(friendResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{Id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteFriend(@PathVariable("Id") String Id) throws Exception {

        userService.deleteFriend(Id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
