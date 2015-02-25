package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.Expense;
import com.mycompany.shareexpense.model.Income;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author AH0661755
 */
@Repository
public interface IncomeRepository extends CrudRepository<Income, String> {

    public List<Income> findByUserId(String userId);

    public Income findByUserIdAndIncomeDate(String userId, Date expenseDate);

    public List<Income> findByIncomeDateBetweenAndUserId(Date startDate, Date endDate, String userId);



}

/*
 *  Query query = Query.query(Criteria.where("friends").is(friendId));

        List<User> users = mongoTemplate.find(query, User.class);

 */
