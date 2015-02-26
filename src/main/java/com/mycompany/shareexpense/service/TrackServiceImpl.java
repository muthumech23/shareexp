package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.*;
import com.mycompany.shareexpense.repository.*;
import com.mycompany.shareexpense.util.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author AH0661755
 */
@Service
public class TrackServiceImpl implements TrackExpService {

    private final Logger log = Logger.getLogger(TrackServiceImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    public CategoryRepository categoryRepository;

    @Autowired
    public BudgetRepository budgetRepository;

    @Autowired
    public ExpenseRepository expenseRepository;

    @Autowired
    public IncomeRepository incomeRepository;

    @Override
    public Expense saveExpense(Expense expense) throws Exception {

        DateFormat df = new SimpleDateFormat("MMMM d, y");
        expense.setExpenseDate(df.parse(expense.getExpenseDate1()));

        return expenseRepository.save(expense);
    }

    @Override
    public Income saveIncome(Income income) throws Exception {

        DateFormat df = new SimpleDateFormat("MMMM d, y");
        income.setIncomeDate(df.parse(income.getIncomeDate1()));

        return incomeRepository.save(income);
    }


    @Override
    public Budget saveBudget(Budget budget) throws Exception {
        return budgetRepository.save(budget);
    }

    @Override
    public Budget getBudget(String userId) throws Exception {

        Budget budget = null;
        try{

            List<Budget> budgetList = budgetRepository.findByUserId(userId);
            if(budgetList != null && !budgetList.isEmpty() && budgetList.size() > 0){
                return budgetList.get(0);
            }else{
                List<Category> categories = categoryRepository.findByUserId(userId);
                budget = new Budget();
                List<KeyValue> keyValues = new ArrayList<>();
                for(Category category: categories){
                    KeyValue keyValue = new KeyValue();
                    keyValue.setKey(category.getCategory());
                    keyValue.setValue2(BigDecimal.ZERO);
                    keyValues.add(keyValue);
                }

                budget.setCategories(keyValues);
                budget.setUserId(userId);
            }


        }catch(Exception e){
            throw e;
        }

        return budget;
    }

    @Override
    public Category saveCategory(Category categories) throws Exception {
        categories.setCreateDate(CommonUtil.getCurrentDateTime());
        return categoryRepository.save(categories);
    }

    @Override
    public List<Category> getCategories(String userId) throws Exception {
        return categoryRepository.findByUserId(userId);
    }

    @Override
    public List<Expense> getTrackExpenses(int year, int month, String userId) throws Exception {

        Date startDate = null;
        Date endDate = null;

        startDate = CommonUtil.getStartOfMonth(year, month);
        endDate = CommonUtil.getEndOfMonth(year, month);

        return expenseRepository.findByExpenseDateBetweenAndUserId(startDate, endDate, userId);
    }

    @Override
    public Expense getTrackExpense(String date, String userId) throws Exception {
        Expense expense = null;
        try {

            DateFormat df = new SimpleDateFormat("MMMM d, y");
            Date expenseDate = df.parse(date);

            expense = expenseRepository.findByUserIdAndExpenseDate(userId, expenseDate);

            if (expense != null) {
                return expense;
            } else {
                List<Category> categories = categoryRepository.findByUserId(userId);
                expense = new Expense();
                List<KeyValue> keyValues = new ArrayList<>();
                for (Category category : categories) {
                    KeyValue keyValue = new KeyValue();
                    keyValue.setKey(category.getCategory());
                    keyValue.setValue2(BigDecimal.ZERO);
                    keyValues.add(keyValue);
                }

                expense.setCategories(keyValues);
                expense.setUserId(userId);
                expense.setExpenseDate(expenseDate);
                expense.setExpenseDate1(date);
            }

        }catch (Exception e){
            throw e;
        }

        return expense;
    }

    @Override
    public List<Income> getAllIncomes(String date, String userId) throws Exception {

        Date startDate = null;
        Date endDate = null;

        startDate = CommonUtil.getStartOfMonth(CommonUtil.getCurrentYear(), CommonUtil.getCurrentMonth());
        endDate = CommonUtil.getEndOfMonth(CommonUtil.getCurrentYear(), CommonUtil.getCurrentMonth());

        return incomeRepository.findByIncomeDateBetweenAndUserId(startDate, endDate, userId);
    }

    @Override
    public Income getIncome(String date, String userId) throws Exception {
        Income income = null;
        try {

            DateFormat df = new SimpleDateFormat("MMMM d, y");
            Date incomeDate = df.parse(date);

            income = incomeRepository.findByUserIdAndIncomeDate(userId, incomeDate);

            if (income != null) {
                return income;
            } else {
                income = new Income();
                income.setUserId(userId);
                income.setIncomeDate(incomeDate);
                income.setIncomeDate1(date);
            }

        }catch (Exception e){
            throw e;
        }

        return income;
    }

    @Override
    public List<YearSummaryDto> getYearSummary(String userId) throws Exception {
        List<YearSummaryDto> yearSummaryDtos = null;
        try {

            int year = CommonUtil.getCurrentYear();
            yearSummaryDtos = new ArrayList<>(12);
            List<Budget> budgetList = budgetRepository.findByUserId(userId);

            BigDecimal budgetAmount = BigDecimal.ZERO;

            for(Budget budget: budgetList){
                for(KeyValue keyValue: budget.getCategories()){
                    budgetAmount = budgetAmount.add(keyValue.getValue2());
                }
            }

            for(int i = 1; i <= 12; i++){

                YearSummaryDto yearSummaryDto = new YearSummaryDto();
                yearSummaryDto.setMonth(i+"");

                BigDecimal expenseAmount = BigDecimal.ZERO;
                List<Expense> expenseList = expenseRepository.findByExpenseDateBetweenAndUserId(CommonUtil.getStartOfMonth(year, i), CommonUtil.getEndOfMonth(year, i), userId);

                for(Expense expense: expenseList){
                    for(KeyValue keyValue: expense.getCategories()){
                        expenseAmount = expenseAmount.add(keyValue.getValue2());
                    }
                }

                yearSummaryDto.setExpenseAmount(expenseAmount);

                BigDecimal incomeAmount = BigDecimal.ZERO;
                List<Income> incomeList = incomeRepository.findByIncomeDateBetweenAndUserId(CommonUtil.getStartOfMonth(year, i), CommonUtil.getEndOfMonth(year, i), userId);

                for(Income income: incomeList){
                    incomeAmount = incomeAmount.add(income.getIncomeAmount());
                }

                yearSummaryDto.setIncomeAmount(incomeAmount);

                yearSummaryDto.setSavingAmount(incomeAmount.subtract(expenseAmount));

                yearSummaryDto.setBudgetAmount(budgetAmount);
                yearSummaryDtos.add(yearSummaryDto);
            }


        }catch (Exception e){
            throw e;
        }

        return yearSummaryDtos;
    }

    @Override
    public List<CategorySummaryDto> getExpensesbyCategory(String userId, int year, int month) throws Exception {
        List<CategorySummaryDto> categorySummaryDtos = null;
        try {

            List<Category> categories = categoryRepository.findByUserId(userId);
            List<Expense> expenseList = expenseRepository.findByExpenseDateBetweenAndUserId(CommonUtil.getStartOfMonth(year, month), CommonUtil.getEndOfMonth(year, month), userId);
            List<Budget> budgetList = budgetRepository.findByUserId(userId);
            if(categories != null){
                categorySummaryDtos = new ArrayList<>(categories.size());
            }


            for(Category category: categories){

                String categoryName = category.getCategory();
                CategorySummaryDto categorySummaryDto = new CategorySummaryDto();
                categorySummaryDto.setCategory(categoryName);

                BigDecimal expenseAmount = BigDecimal.ZERO;

                for(Expense expense: expenseList){
                    for(KeyValue keyValue: expense.getCategories()){
                        if(categoryName.equalsIgnoreCase(keyValue.getKey())){
                            expenseAmount = expenseAmount.add(keyValue.getValue2());
                        }
                    }
                }

                categorySummaryDto.setExpenseAmount(expenseAmount);

                BigDecimal budgetAmount = BigDecimal.ZERO;

                for(Budget budget: budgetList){
                    for(KeyValue keyValue: budget.getCategories()){
                        if(categoryName.equalsIgnoreCase(keyValue.getKey())){
                            budgetAmount = budgetAmount.add(keyValue.getValue2());
                        }
                    }
                }

                categorySummaryDto.setBudgetAmount(budgetAmount);

                categorySummaryDto.setDiffAmount(budgetAmount.subtract(expenseAmount));
                categorySummaryDtos.add(categorySummaryDto);
            }


        }catch (Exception e){
            throw e;
        }

        return categorySummaryDtos;
    }
}
