package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.*;
import com.mycompany.shareexpense.service.TrackExpService;
import com.mycompany.shareexpense.util.CustomException;
import com.mycompany.shareexpense.util.ErrorConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("trackexp")
public class TrackExpController extends AbstractController {

    /**
     * The log.
     */
    private final Logger log = Logger.getLogger(TrackExpController.class);

    /**
     * The bill service.
     */
    @Autowired
    public TrackExpService trackExpService;

    /**
     * Submit bill.
     *
     * @param expense the trackExpList
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/saveexpense",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<Expense> saveExpense(@RequestBody Expense expense) throws CustomException {

        Expense returnTrackExp = null;
        try {
            returnTrackExp = trackExpService.saveExpense(expense);
        } catch (CustomException ce) {
            log.error("/trackexp/saveexpense", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/saveexpense", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(returnTrackExp, HttpStatus.OK);
    }

    /**
     * Submit bill.
     *
     * @param budget the trackExpList
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/savebudget",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<Budget> saveBudget(@RequestBody Budget budget) throws CustomException {

        Budget returnBudget = null;
        try {
            returnBudget = trackExpService.saveBudget(budget);
        } catch (CustomException ce) {
            log.error("/trackexp/savebudget", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/savebudget", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(returnBudget, HttpStatus.OK);
    }

    /**
     * Submit bill.
     *
     * @param income the trackExpList
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/saveincome",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<Income> saveBudget(@RequestBody Income income) throws CustomException {

        Income returnIncome = null;
        try {
            returnIncome = trackExpService.saveIncome(income);
        } catch (CustomException ce) {
            log.error("/trackexp/saveincome", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/saveincome", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(returnIncome, HttpStatus.OK);
    }

    /**
     * Submit bill.
     *
     * @param category the trackExpList
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/savecategory",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<Category> categorySubmission(@RequestBody Category category) throws CustomException {
        Category categoryReturn = null;
        try {
            categoryReturn = trackExpService.saveCategory(category);
        } catch (CustomException ce) {
            log.error("/trackexp/savecategories", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/savecategories", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(categoryReturn, HttpStatus.OK);
    }

    /**
     * Submit bill.
     *
     * @param date the trackExpList
     * @param userId the trackExpList
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/getexpense/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<Expense> getExpense(@RequestBody String date,  @PathVariable("userId") String userId) throws CustomException {

        Expense returnExpense = null;
        try {
            returnExpense = trackExpService.getTrackExpense(date, userId);
        } catch (CustomException ce) {
            log.error("/trackexp/getexpense", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/getexpense", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(returnExpense, HttpStatus.OK);
    }

    /**
     * Submit bill.
     *
     * @param date the trackExpList
     * @param userId the trackExpList
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/getincome/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<Income> getIncome(@RequestBody String date,  @PathVariable("userId") String userId) throws CustomException {

        Income returnIncome = null;
        try {
            returnIncome = trackExpService.getIncome(date, userId);
        } catch (CustomException ce) {
            log.error("/trackexp/getincome", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/getincome", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(returnIncome, HttpStatus.OK);
    }

    /**
     * Submit bill.
     *
     * @param date the trackExpList
     * @param userId the trackExpList
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/getallincomes/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<List<Income>> getAllIncomes(@RequestBody String date,  @PathVariable("userId") String userId) throws CustomException {

        List<Income> returnIncomes = null;
        try {
            returnIncomes = trackExpService.getAllIncomes(date, userId);
        } catch (CustomException ce) {
            log.error("/trackexp/getallincomes", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/getallincomes", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(returnIncomes, HttpStatus.OK);
    }


    /**
     * Submit bill.
     *
     * @param year the trackExpList
     * @param month the trackExpList
     * @param userId the trackExpList
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/getexpenses/{year}/{month}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<List<Expense>> getExpenses(@RequestBody String userId, @PathVariable("year") int year, @PathVariable("month") int month) throws CustomException {

        List<Expense> returnExpenses = null;
        try {
            returnExpenses = trackExpService.getTrackExpenses(year, month, userId);
        } catch (CustomException ce) {
            log.error("/trackexp/getexpenses", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/getexpenses", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(returnExpenses, HttpStatus.OK);
    }

    /**
     * Submit bill.
     *
     * @param year the trackExpList
     * @param month the trackExpList
     * @param userId the trackExpList
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/getexpensesbycategory/{year}/{month}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<List<CategorySummaryDto>> getExpensesbyCategory(@RequestBody String userId, @PathVariable("year") int year, @PathVariable("month") int month) throws CustomException {

        List<CategorySummaryDto> returnExpenses = null;
        try {
            returnExpenses = trackExpService.getExpensesbyCategory(userId, year, month);
        } catch (CustomException ce) {
            log.error("/trackexp/getexpensesbycategory", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/getexpensesbycategory", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(returnExpenses, HttpStatus.OK);
    }

    /**
     * Submit bill.
     *
     * @param userId the trackExpList
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/getbudget/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public ResponseEntity<Budget> getBudget(@PathVariable("userId") String userId) throws CustomException {

        Budget budget = null;
        try {
            budget = trackExpService.getBudget(userId);
        } catch (CustomException ce) {
            log.error("/trackexp/getbudget", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/getbudget", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    /**
     * Submit bill.
     *
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/getcategories/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getCategories(@PathVariable("userId") String userId) throws CustomException {

        List<Category> categories = null;
        try {
            categories = trackExpService.getCategories(userId);
        } catch (CustomException ce) {
            log.error("/trackexp/getcategories", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/getcategories", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * Submit bill.
     *
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/getyearsummary/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public ResponseEntity<List<YearSummaryDto>> getYearSummary(@PathVariable("userId") String userId) throws CustomException {

        List<YearSummaryDto> yearSummaryDtos = null;
        try {
            yearSummaryDtos = trackExpService.getYearSummary(userId);
        } catch (CustomException ce) {
            log.error("/trackexp/getyearsummary", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/getyearsummary", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(yearSummaryDtos, HttpStatus.OK);
    }

}
