/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.BillPerson;
import com.mycompany.shareexpense.model.BillSplit;
import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.service.BillService;
import com.mycompany.shareexpense.service.UserService;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomController {

    private final Log log = LogFactory.getLog(CustomController.class);

    @Autowired
    public UserService userService;
    
     @Autowired
    public BillService billService;

    @RequestMapping(value = "friends/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<User> allFriends(@PathVariable("userId") String userId) throws Exception {
        return userService.findByFriend(userId);
    }

    @RequestMapping(value = "bills/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public List<BillPerson> allBills(@RequestBody String userId) throws Exception {
        return billService.findAllBills(userId);
    }
}
