/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.ShareGroup;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author AH0661755
 */
@Repository
public interface GroupRepository extends CrudRepository<ShareGroup, String> {
    
    public List<ShareGroup> findByUserIdOrUserIds(String Id, String userId);
    
}
