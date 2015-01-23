
package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.BillSplit;
import com.mycompany.shareexpense.model.UserDto;
import com.mycompany.shareexpense.model.UsersBalance;
import com.mycompany.shareexpense.service.BillService;
import com.mycompany.shareexpense.util.CustomException;
import com.mycompany.shareexpense.util.ErrorConstants;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bill")
public class BillController extends AbstractController {

	/** The log. */
	private final Logger	log	= Logger.getLogger(BillController.class);

	/** The bill service. */
	@Autowired
	public BillService		billService;

	/**
	 * Submit bill.
	 *
	 * @param bill the bill
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.POST)
	public ResponseEntity<Bill> submitBill(@RequestBody Bill bill) throws CustomException {

		Bill returnBill = null;
		try {
			returnBill = billService.saveBill(bill);

		} catch (CustomException ce) {
			log.error("/bill/create", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/bill/create", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
		}

		return new ResponseEntity<>(returnBill, HttpStatus.CREATED);
	}

	/**
	 * Show bill.
	 *
	 * @param Id the id
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/{Id}",
					produces = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.GET)
	public ResponseEntity<Bill> showBill(@PathVariable("Id") String Id) throws CustomException {

		Bill returnBill = null;
		try {

			returnBill = billService.showBill(Id);

		} catch (CustomException ce) {
			log.error("/bill/show", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/bill/show", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
		}
		return new ResponseEntity<>(returnBill, HttpStatus.OK);
	}

	/**
	 * Users bill details.
	 *
	 * @param userId the user id
	 * @return the list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/total/{userId}",
					produces = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.GET)
	public ResponseEntity<List<UsersBalance>> usersBillDetails(@PathVariable("userId") String userId) throws CustomException {

		List<UsersBalance> billSplitList = null;
		try {
			billSplitList = billService.usersBillDetails(userId);
		} catch (CustomException ce) {
			log.error("/bill/total", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/bill/total", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
		}

		return new ResponseEntity<List<UsersBalance>>(billSplitList, HttpStatus.OK);
	}
	
	
	/**
	 * Users bill details.
	 *
	 * @param userId the user id
	 * @return the list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/settleup",
					consumes = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.POST)
	public ResponseEntity<Void> userSettlement(@RequestBody UserDto userDto) throws CustomException {

		try {
			billService.userSettlement(userDto);
		} catch (CustomException ce) {
			log.error("/bill/settleup", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/bill/settleup", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	/**
	 * Users bill details.
	 *
	 * @param userId the user id
	 * @return the list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/reminder",
					consumes = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.POST)
	public ResponseEntity<Void> userPayReminder(@RequestBody UserDto userDto) throws CustomException {

		try {
			billService.userPayReminder(userDto);
		} catch (CustomException ce) {
			log.error("/bill/reminder", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/bill/reminder", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Recent trans.
	 *
	 * @param month the month
	 * @param userId the user id
	 * @return the list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/recent/{userId}",
					produces = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.GET)
	public ResponseEntity<List<Bill>> recentTrans(	@RequestBody String month,
													@PathVariable("userId") String userId) throws CustomException {

		List<Bill> recentTransList = null;
		try {

			recentTransList = billService.recentTrans(userId);
		} catch (CustomException ce) {
			log.error("/bill/recent", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/bill/recent", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
		}
		return new ResponseEntity<List<Bill>>(recentTransList, HttpStatus.OK);
	}

	/**
	 * Recent trans.
	 *
	 * @param month the month
	 * @param userId the user id
	 * @return the list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/recent1/{userId}",
					produces = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.GET)
	public ResponseEntity<Page<Bill>> recentPageTrans(	@RequestBody int page,
														int pagesize,
														String month,
														@PathVariable("userId") String userId) throws CustomException {

		Page<Bill> recentPageTransList = null;
		try {
			recentPageTransList = billService.recentPageTrans(userId, page, pagesize);
		} catch (CustomException ce) {
			log.error("/bill/recent1", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/bill/recent1", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
		}
		return new ResponseEntity<Page<Bill>>(recentPageTransList, HttpStatus.OK);
	}

	/**
	 * Recent trans.
	 *
	 * @param month the month
	 * @param userId the user id
	 * @return the list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/recentuser/{userId}",
					produces = MediaType.APPLICATION_JSON_VALUE,
					consumes = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.POST)
	public ResponseEntity<List<Bill>> recentUserTrans(	@PathVariable("userId") String userId,
														@RequestBody String loggedUser) throws CustomException {

		List<Bill> recentUserTrans = null;
		try {
			recentUserTrans = billService.recentUserTrans(userId, loggedUser);
		} catch (CustomException ce) {
			log.error("/bill/recentuser", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/bill/recentuser", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
		}

		return new ResponseEntity<List<Bill>>(recentUserTrans, HttpStatus.OK);
	}

	/**
	 * Recent group trans.
	 *
	 * @param groupId the group id
	 * @return the list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/group/{groupId}",
					produces = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.GET)
	public ResponseEntity<List<Bill>> recentGroupTrans(@PathVariable("groupId") String groupId) throws CustomException {

		List<Bill> recentGrpTrans = null;
		try {
			recentGrpTrans = billService.recentGroupTrans(groupId);
		} catch (CustomException ce) {
			log.error("/bill/group", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/bill/group", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
		}
		return new ResponseEntity<List<Bill>>(recentGrpTrans, HttpStatus.OK);
	}

	/**
	 * Adds the bill.
	 *
	 * @param userId the user id
	 * @return the bill
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/add/{userId}",
					produces = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.GET)
	public ResponseEntity<Bill> addBill(@PathVariable("userId") String userId) throws CustomException {

		Bill addBill = null;
		try {
			addBill = billService.addBill(userId);
		} catch (CustomException ce) {
			log.error("/bill/add", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/bill/add", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
		}
		return new ResponseEntity<Bill>(addBill, HttpStatus.OK);
	}
	
	/**
	 * Adds the bill.
	 *
	 * @param userId the user id
	 * @return the bill
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/addgrp/{groupId}",
					produces = MediaType.APPLICATION_JSON_VALUE,
					method = RequestMethod.GET)
	public ResponseEntity<Bill> addGroupBill(@PathVariable("groupId") String groupId) throws CustomException {

		Bill addBill = null;
		try {
			addBill = billService.addGroupBill(groupId);
		} catch (CustomException ce) {
			log.error("/bill/add", ce);
			throw ce;
		} catch (Exception e) {
			log.error("/bill/add", e);
			throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
		}
		return new ResponseEntity<Bill>(addBill, HttpStatus.OK);
	}

}
