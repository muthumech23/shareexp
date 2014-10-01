
package com.mycompany.shareexpense.controller;


import com.mycompany.shareexpense.model.ShareGroup;
import com.mycompany.shareexpense.service.GroupService;
import com.mycompany.shareexpense.util.CustomException;
import com.mycompany.shareexpense.util.ErrorConstants;

import java.util.List;

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

@RestController
@RequestMapping("group")
public class GroupController extends AbstractController {

	private final Logger	log	= Logger.getLogger(GroupController.class);

	@Autowired
	public GroupService		groupService;

	/*
	 * USER registration
	 */
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.POST)
	public ResponseEntity<ShareGroup> createGroup(@RequestBody ShareGroup shareGroup) throws CustomException {

		ShareGroup shareGroupResponse = null;
		try {
			shareGroupResponse = groupService.createGroup(shareGroup);
		} catch (CustomException ce) {
			log.error("/group/create", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/group/create", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE,
							"Transaction requested has been failed. Please try again.");
		}
		return new ResponseEntity<>(shareGroupResponse, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{Id}",
					method = RequestMethod.PUT,
					produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ShareGroup> updateGroup(@PathVariable("Id") String Id) throws CustomException {

		ShareGroup shareGroupResponse = null;
		try {
			shareGroupResponse = groupService.showGroup(Id);
		} catch (CustomException ce) {
			log.error("/group/update", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/group/update", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE,
							"Transaction requested has been failed. Please try again.");
		}
		return new ResponseEntity<>(shareGroupResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/{Id}",
					method = RequestMethod.GET,
					produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ShareGroup> showGroup(@PathVariable("Id") String Id) throws CustomException {

		ShareGroup shareGroupResponse = null;
		try {
			shareGroupResponse = groupService.showGroup(Id);

			if (shareGroupResponse == null) {
				throw new CustomException(ErrorConstants.ERR_SHOW_GROUP_DETAILS,
								"Transaction to retrieve group details failed. Please try again.");
			}
		} catch (CustomException ce) {
			log.error("/group/show", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/group/show", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE,
							"Transaction requested has been failed. Please try again.");
		}
		return new ResponseEntity<>(shareGroupResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/list/{Id}",
					produces = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.GET)
	public List<ShareGroup> listGroups(@PathVariable("Id") String Id) throws CustomException {

		List<ShareGroup> groupList = null;
		try {
			groupList = groupService.listGroups(Id);
		} catch (CustomException ce) {
			log.error("/group/list", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/group/list", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE,
							"Transaction requested has been failed. Please try again.");
		}
		return groupList;
	}

}
