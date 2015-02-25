package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.*;

import java.util.Date;
import java.util.List;

/**
 * @author AH0661755
 */
public interface TrackExpService {

    public Expense saveExpense(Expense expense) throws Exception;

    public Budget saveBudget(Budget trackExpList) throws Exception;

    public Income saveIncome(Income income) throws Exception;

    public Category saveCategory(Category categories) throws Exception;

    public Budget getBudget(String userId) throws Exception;

    public  List<Category> getCategories(String userId) throws Exception;

    public List<Expense> getTrackExpenses(int year, int month, String userId) throws Exception;

    public Expense getTrackExpense(String date, String userId) throws Exception;

    public List<Income> getAllIncomes(String date, String userId) throws Exception;

    public Income getIncome(String date, String userId) throws Exception;

    public List<YearSummaryDto> getYearSummary(String userId) throws Exception;

    public List<CategorySummaryDto> getExpensesbyCategory(String userId, int year, int month) throws Exception;

}
