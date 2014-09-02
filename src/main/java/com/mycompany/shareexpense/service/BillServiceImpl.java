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
public class BillServiceImpl implements BillService{
    
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
        List<User> users = userRepository.findByIdOrFriends(userId, userId);
        List<BillSplit> billPersons = billCustomRepository.findAllBills(userId, users);
        return billPersons;
    }
    
    @Override
    public List<Bill> recentTrans(String userId) throws Exception {
        
        return null;
    }

}
