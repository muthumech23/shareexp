package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.service.UserService;
import com.mycompany.shareexpense.util.CustomException;
import com.mycompany.shareexpense.util.ErrorConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("friend")
public class FriendController extends AbstractController {

    private final Logger log = Logger.getLogger(FriendController.class);

    @Autowired
    public UserService userService;

    @RequestMapping(value = "/{Id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<User> createFriend(@PathVariable("Id") String Id,
                                             @RequestBody User user) throws CustomException {

        User userResponse = null;
        try {
            userResponse = userService.createFriend(user, Id);
        } catch (CustomException ce) {
            log.error("/friend/create", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/friend/create", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);

    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT)
    public ResponseEntity<User> updateFriend(@RequestBody User user) throws CustomException {

        User friendResponse = null;
        try {
            friendResponse = userService.updateFriend(user);
        } catch (CustomException ce) {
            log.error("/friend/update", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/friend/update", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return new ResponseEntity<>(friendResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/all/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public List<User> allFriends(@PathVariable("userId") String userId) throws Exception {

        List<User> friendList = null;

        try {
            friendList = userService.findByFriend(userId);

        } catch (CustomException ce) {
            log.error("/friend/list", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/friend/list", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return friendList;
    }

    @RequestMapping(value = "/remove/{Id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<Void> deleteFriend(@PathVariable("Id") String Id,
                                             @RequestBody String userId) throws CustomException {

        try {
            userService.deleteFriend(userId, Id);
        } catch (CustomException ce) {
            log.error("/friend/delete", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/friend/delete", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
