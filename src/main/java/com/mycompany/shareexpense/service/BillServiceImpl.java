/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.BillSplit;
import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.repository.BillCustomRepository;
import com.mycompany.shareexpense.repository.BillRepository;
import com.mycompany.shareexpense.repository.UserRepository;
import com.mycompany.shareexpense.util.CommonUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author AH0661755
 */
@Service
public class BillServiceImpl implements BillService {

    private Log log = LogFactory.getLog(BillServiceImpl.class);

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public BillCustomRepository billCustomRepository;

    @Override
    public Bill saveBill(Bill bill) throws Exception {

        return billRepository.save(bill);
    }

    @Override
    public Bill showBill(String Id) throws Exception {
        return billRepository.findOne(Id);

    }

    @Override
    public Bill updateBill(Bill bill) throws Exception {
        return billRepository.save(bill);
    }

    @Override
    public Iterable<Bill> findAll() throws Exception {
        return billRepository.findAll();
    }

    @Override
    public void deleteBill(String Id) throws Exception {
        billRepository.delete(Id);
    }

    @Override
    public List<BillSplit> findAllBills(String userId) throws Exception {

        User user = userRepository.findOne(userId);
        List<User> users = null;
        if (user.getFriends() != null) {
            users = CommonUtil.toList(userRepository.findAll(user.getFriends()));
        } else {
            users = new ArrayList<>();
        }

        List<Bill> bills = billRepository.findByUserPaidOrBillSplitsUserId(userId, userId);

        List<BillSplit> billSpits = new ArrayList<>();

        BillSplit loggedUser = new BillSplit();
        loggedUser.setUserId(user.getId());
        loggedUser.setName(user.getName());
        loggedUser.setEmail(user.getEmail());

        billSpits.add(0, loggedUser);

        BigDecimal loggedUserAmt = BigDecimal.ZERO;

        for (User userBill : users) {
            BillSplit billSplit = new BillSplit();
            String Id = userBill.getId();
            log.info("User ->" + Id);
            BigDecimal bigDecimal = BigDecimal.ZERO;
            for (Bill bill : bills) {
                log.info("Bill ->" + bill.getId());
                for (BillSplit billsplit : bill.getBillSplits()) {
                    log.info("BillSPlit ->" + billsplit.getUserId());
                    if (billsplit.getUserId().equalsIgnoreCase(Id)) {
                        log.info("Bill Split IF ->" + billsplit.getUserId());
                        log.info("User Split IF ->" + Id);
                        log.info("User AMount IF ->" + billsplit.getAmount());
                        if(userId.equalsIgnoreCase(bill.getUserPaid()) || billsplit.getUserId().equalsIgnoreCase(bill.getUserPaid())){
                            bigDecimal = bigDecimal.add(billsplit.getAmount());
                        }
                        
                        log.info(billsplit.getAmount());
                    }
                }
            }

            billSplit.setUserId(Id);
            billSplit.setName(userBill.getName());
            billSplit.setEmail(userBill.getEmail());
            billSplit.setAmount(bigDecimal);
            loggedUserAmt = loggedUserAmt.add(bigDecimal);
            log.info(bigDecimal);
            billSpits.add(billSplit);
        }

        billSpits.get(0).setAmount(loggedUserAmt);
        return billSpits;
    }

    @Override
    public List<Bill> recentTrans(String userId) throws Exception {

        List<Bill> bills = billRepository.findByUserPaidOrBillSplitsUserId(userId, userId);
        
        log.info("Bill Details ---> "+bills.size());
        
        return bills;
        
        
    }

    @Override
    public Bill addBill(String userId) throws Exception {

        List<User> users = null;
        Bill bill = null;
        
        User user = userRepository.findOne(userId);
        
        if (user.getFriends() != null) {
            users = CommonUtil.toList(userRepository.findAll(user.getFriends()));
        } else {
            users = new ArrayList<>();
        }
        users.add(user);
        
        List<BillSplit> billSplits = new ArrayList<>();

        for (User userBill : users) {
            BillSplit billSplit = new BillSplit();
            billSplit.setUserId(userBill.getId());
            billSplit.setName(userBill.getName());
            billSplit.setEmail(userBill.getEmail());
            billSplit.setAmount(BigDecimal.ZERO);
            billSplits.add(billSplit);
        }
        bill = new Bill();
        bill.setBillSplits(billSplits);
        return bill;
    }

}
