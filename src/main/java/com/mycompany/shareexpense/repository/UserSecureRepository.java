

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
