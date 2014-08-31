/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.BillPerson;
import com.mycompany.shareexpense.model.BillSplit;
import java.util.List;
import org.springframework.data.repository.Repository;

/**
 *
 * @author AH0661755
 */
public interface BillCustomRepository extends Repository<BillSplit, String>{
    
    public List<BillPerson> findAllBills(String userId);
}
