/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.BillSplit;
import com.mycompany.shareexpense.service.BillService;
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
@RequestMapping("bill")
public class BillController {

    private final Log log = LogFactory.getLog (BillController.class);

    @Autowired
    public BillService billService;

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<Bill> submitBill (@RequestBody Bill bill) throws Exception {

        log.debug ("Inside Submit Bill --->");
        log.debug (bill.getUserPaid ());
        Bill returnBill = billService.saveBill (bill);

        ResponseEntity<Bill> responseEntity = new ResponseEntity<> (returnBill, HttpStatus.CREATED);
        return responseEntity;
    }

    @RequestMapping(value = "/{Id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<Bill> showBill (@PathVariable("Id") String Id) throws Exception {

        log.debug ("Inside show Bill --->");
        Bill returnBill = billService.showBill (Id);

        ResponseEntity<Bill> responseEntity = new ResponseEntity<> (returnBill, HttpStatus.CREATED);
        return responseEntity;
    }

    @RequestMapping(value = "/total/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<BillSplit> usersBillDetails (
            @PathVariable("userId") String userId) throws Exception {
        return billService.usersBillDetails (userId);
    }

    @RequestMapping(value = "/recent/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<Bill> recentTrans (@PathVariable("userId") String userId) throws Exception {
        return billService.recentTrans (userId);
    }

    @RequestMapping(value = "/group/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<Bill> recentGroupTrans (@PathVariable("groupId") String groupId)
            throws Exception {
        return billService.recentGroupTrans (groupId);
    }

    @RequestMapping(value = "/add/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Bill addBill (@PathVariable("userId") String userId) throws Exception {
        return billService.addBill (userId);
    }

}
