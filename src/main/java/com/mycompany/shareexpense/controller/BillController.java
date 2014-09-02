/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.service.BillService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bill")
public class BillController {

    private final Log log = LogFactory.getLog(BillController.class);

    @Autowired
    public BillService billService;

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<Bill> submitBill(@RequestBody Bill bill) throws Exception {

        log.debug("Inside Submit Bill --->");
        log.debug(bill.getUserPaid());
        Bill returnBill =  billService.saveBill(bill);
        
        

        ResponseEntity<Bill> responseEntity = new ResponseEntity<>(returnBill, HttpStatus.CREATED);
        return responseEntity;
    }
}
