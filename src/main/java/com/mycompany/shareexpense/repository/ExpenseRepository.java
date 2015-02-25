package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.Budget;
import com.mycompany.shareexpense.model.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author AH0661755
 */
@Repository
public interface ExpenseRepository extends CrudRepository<Expense, String> {

    public List<Expense> findByUserId(String userId);

    public Expense findByUserIdAndExpenseDate(String userId, Date expenseDate);

    public List<Expense> findByExpenseDateBetweenAndUserId(Date startDate, Date endDate, String userId);



}

/*
 *  Query query = Query.query(Criteria.where("friends").is(friendId));

        List<User> users = mongoTemplate.find(query, User.class);

 */
