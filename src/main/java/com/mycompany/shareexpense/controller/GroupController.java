/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.ShareGroup;
import com.mycompany.shareexpense.service.GroupService;
import java.util.List;
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
@RequestMapping("group")
public class GroupController {

    private final Log log = LogFactory.getLog(GroupController.class);

    @Autowired
    public GroupService groupService;

    /* USER registration */
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<ShareGroup> createGroup(@RequestBody ShareGroup shareGroup) throws Exception {
        ShareGroup shareGroupResponse = groupService.createGroup(shareGroup);

        return new ResponseEntity<>(shareGroupResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{Id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShareGroup> updateGroup(@PathVariable("Id") String Id) throws Exception {

        ShareGroup shareGroupResponse = groupService.showGroup(Id);

        return new ResponseEntity<>(shareGroupResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/list/{Id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<ShareGroup> listGroups(@PathVariable("Id") String Id) throws Exception {
        return groupService.listGroups(Id);
    }

}
