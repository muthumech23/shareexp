package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author AH0661755
 */
@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {

    public List<Category> findByUserId(String userId);

}

/*
 *  Query query = Query.query(Criteria.where("friends").is(friendId));

        List<User> users = mongoTemplate.find(query, User.class);

 */
