package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.UserSecure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author AH0661755
 */
@Repository
public interface UserSecureRepository extends CrudRepository<UserSecure, String> {

    public UserSecure findByUserIdAndPasswordIgnoreCase(String userId, String password);

    public UserSecure findByUserIdIgnoreCase(String userId);

    public List<UserSecure> findAll();

}
