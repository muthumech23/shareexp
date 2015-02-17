package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author AH0661755
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {

    public List<User> findByIdOrFriends(String userId,
                                        String friendId);

    public List<User> findByFriends(String friendId);

    public User findByEmailIgnoreCase(String email);

    public List<User> findAll();

}

/*
 *  Query query = Query.query(Criteria.where("friends").is(friendId));

        List<User> users = mongoTemplate.find(query, User.class);

 */
