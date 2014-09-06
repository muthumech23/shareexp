/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author AH0661755
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {

    public List<User> findByIdOrFriends (String userId,
                                         String friendId);

    public List<User> findByFriends (String friendId);

    public User findByEmail (String email);

}

/*
 *  Query query = Query.query(Criteria.where("friends").is(friendId));

        List<User> users = mongoTemplate.find(query, User.class);

 */
