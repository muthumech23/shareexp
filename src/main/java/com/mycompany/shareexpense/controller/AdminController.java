package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.ShareGroup;
import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.model.UserSecure;
import com.mycompany.shareexpense.service.BillService;
import com.mycompany.shareexpense.service.GroupService;
import com.mycompany.shareexpense.service.UserService;
import com.mycompany.shareexpense.util.CustomException;
import com.mycompany.shareexpense.util.ErrorConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController extends AbstractController {

    /**
     * The log.
     */
    private final Logger log = Logger.getLogger(AdminController.class);

    /**
     * The bill service.
     */
    @Autowired
    public BillService billService;

    @Autowired
    public GroupService groupService;

    @Autowired
    public UserService userService;

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
            log.error("/admin/showbill", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/admin/showbill", e);
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
    @RequestMapping(value = "/bills/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public ResponseEntity<List<Bill>> findAllBills(@PathVariable("userId") String userId) throws CustomException {

        List<Bill> bills = null;
        try {
            bills = billService.findAllBills();
        } catch (CustomException ce) {
            log.error("/admin/bills", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/admin/bills", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<List<Bill>>(bills, HttpStatus.OK);
    }


    /**
     * Users bill details.
     *
     * @param userId the user id
     * @return the list
     * @throws Exception the exception
     */
    @RequestMapping(value = "/users/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public ResponseEntity<List<User>> findAllUsers(@PathVariable("userId") String userId) throws CustomException {

        List<User> users = null;
        try {
            users = userService.findAllUsers();
        } catch (CustomException ce) {
            log.error("/admin/users", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/admin/users", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    /**
     * Users bill details.
     *
     * @param userId the user id
     * @return the list
     * @throws Exception the exception
     */
    @RequestMapping(value = "/usersecures/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public ResponseEntity<List<UserSecure>> findAllUserSecures(@PathVariable("userId") String userId) throws CustomException {

        List<UserSecure> userSecures = null;
        try {
            userSecures = userService.findAllUserSecures();
        } catch (CustomException ce) {
            log.error("/admin/usersecures", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/admin/usersecures", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<List<UserSecure>>(userSecures, HttpStatus.OK);
    }

    /**
     * Users bill details.
     *
     * @param userId the user id
     * @return the list
     * @throws Exception the exception
     */
    @RequestMapping(value = "/groups/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public ResponseEntity<List<ShareGroup>> findAllGroups(@PathVariable("userId") String userId) throws CustomException {

        List<ShareGroup> shareGroups = null;
        try {
            shareGroups = groupService.findAllGroups();
        } catch (CustomException ce) {
            log.error("/admin/groups", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/admin/groups", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<List<ShareGroup>>(shareGroups, HttpStatus.OK);
    }


}
