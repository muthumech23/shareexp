/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.Bill;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author AH0661755
 */
@Repository
public interface BillSortAndPageRepository extends PagingAndSortingRepository<Bill, String>{
    
    public List<Bill> findByUserId(String userId, Pageable pageable);
    public List<Bill> findByUserId(String userId, Sort sort);
    
}
