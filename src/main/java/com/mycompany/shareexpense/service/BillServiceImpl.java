/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.BillPerson;
import com.mycompany.shareexpense.repository.BillCustomRepository;
import com.mycompany.shareexpense.repository.BillRepository;
import com.mycompany.shareexpense.repository.BillSplitRepository;
import com.mycompany.shareexpense.repository.UserCustomRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author AH0661755
 */
@Service
public class BillServiceImpl implements BillService{
    
    @Autowired
    private BillRepository billRepository;
    
    @Autowired
    private BillSplitRepository billSplitRepository;
    
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
    public List<BillPerson> findAllBills(String userId) throws Exception {
        List<BillPerson> billPersons = billCustomRepository.findAllBills(userId);
        return billPersons;
    }

}
