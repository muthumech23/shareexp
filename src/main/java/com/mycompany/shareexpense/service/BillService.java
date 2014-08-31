/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.BillPerson;
import com.mycompany.shareexpense.model.BillSplit;
import java.util.List;

/**
 *
 * @author AH0661755
 */
public interface BillService {
    
    public Bill saveBill(Bill bill) throws Exception;

    public Bill showBill(String Id) throws Exception;

    public void deleteBill(String Id) throws Exception;
    
    public Bill updateBill(Bill bill) throws Exception;

    public Iterable<Bill> findAll() throws Exception;
    
    public List<BillPerson> findAllBills(String userId) throws Exception;

    
}
