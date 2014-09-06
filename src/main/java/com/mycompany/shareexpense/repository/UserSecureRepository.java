/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.UserSecure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author AH0661755
 */
@Repository
public interface UserSecureRepository extends CrudRepository<UserSecure, String>{
    
    public UserSecure findByUserIdAndPassword(String userId, String password);
    
    public UserSecure findByUserId(String userId);
    
}
