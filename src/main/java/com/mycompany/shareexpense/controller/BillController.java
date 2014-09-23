
package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.BillSplit;
import com.mycompany.shareexpense.service.BillService;

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

// TODO: Auto-generated Javadoc
/**
 * The Class BillController.
 */
@RestController
@RequestMapping("bill")
public class BillController {

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
	public ResponseEntity<Bill> submitBill(@RequestBody Bill bill) throws Exception {

		log.debug("Inside Submit Bill --->");
		log.debug(bill.getUserPaid());
		Bill returnBill = billService.saveBill(bill);

		ResponseEntity<Bill> responseEntity = new ResponseEntity<>(returnBill, HttpStatus.CREATED);
		return responseEntity;
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
	public ResponseEntity<Bill> showBill(@PathVariable("Id") String Id) throws Exception {

		log.debug("Inside show Bill --->");
		Bill returnBill = billService.showBill(Id);

		ResponseEntity<Bill> responseEntity = new ResponseEntity<>(returnBill, HttpStatus.CREATED);
		return responseEntity;
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
	public List<BillSplit> usersBillDetails(@PathVariable("userId") String userId) throws Exception {

		return billService.usersBillDetails(userId);
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
	public List<Bill> recentTrans(	@RequestBody String month,
									@PathVariable("userId") String userId) throws Exception {

		System.out.println(month);
		return billService.recentTrans(userId);
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
	public Page<Bill> recentPageTrans(	@RequestBody int page,
										int pagesize,
										String month,
										@PathVariable("userId") String userId) throws Exception {

		System.out.println(month);
		return billService.recentPageTrans(userId, page, pagesize);
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
					consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public List<Bill> recentUserTrans(	@PathVariable("userId") String userId,
										@RequestBody String loggedUser) throws Exception {

		log.debug("Inside recentUserTrans");
		log.debug("Logged User -->" + loggedUser);
		log.debug("userId -->" + userId);
		return billService.recentUserTrans(userId, loggedUser);
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
	public List<Bill> recentGroupTrans(@PathVariable("groupId") String groupId) throws Exception {

		return billService.recentGroupTrans(groupId);
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
	public Bill addBill(@PathVariable("userId") String userId) throws Exception {

		return billService.addBill(userId);
	}

}
