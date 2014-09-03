/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.User;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author AH0661755
 */
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final Log log = LogFactory.getLog(UserCustomRepositoryImpl.class);

    @Autowired
    public MongoTemplate mongoTemplate;

    @Override
    public List<User> findByFriendCustom(String friendId) {

        Query query = Query.query(Criteria.where("friends").is(friendId));

        List<User> users = mongoTemplate.find(query, User.class);

        return users;
    }

}
