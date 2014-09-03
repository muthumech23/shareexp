/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.BillSplit;
import com.mycompany.shareexpense.model.User;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author AH0661755
 */
@Repository
public class BillCustomRepositoryImpl implements BillCustomRepository {

    private final Log log = LogFactory.getLog(BillCustomRepositoryImpl.class);

    @Autowired
    public MongoTemplate mongoTemplate;
    
    @Autowired
    public BillRepository billRepository;

    @Override
    public List<BillSplit> findAllBills(String userId, User user, List<User> users) {

        List<Bill> billsCollection = billRepository.findByUserPaidOrBillSplitsId(userId, userId);
        
        AggregationOperation match = Aggregation.match(Criteria.where("userPaid").is(userId));

        AggregationOperation group = Aggregation.group("billSplits._id").sum("billSplits.amount").as("amount");

        Aggregation aggregation = Aggregation.newAggregation(match, group);
        AggregationResults<Bill> result = mongoTemplate.aggregate(aggregation, "bills", Bill.class);

        List<Bill> bills = result.getMappedResults();
        
        for(Bill bill: bills){
            log.info(bill.getId());
            log.info(bill.getBillSplits());
            
            for(BillSplit billSplit: bill.getBillSplits()){
                log.info(billSplit.getAmount());
                log.info(billSplit.getId());
                log.info(billSplit.getName());
            }
        }

        List<BillSplit> billSplits = bills.get(0).getBillSplits();
        BigDecimal loggedUserAmount = BigDecimal.ZERO;

        log.info(billSplits.isEmpty());
        if (billSplits.isEmpty()) {
            billSplits = new ArrayList<>();
            for (User user1 : users) {
                BillSplit billSplit = new BillSplit();
                billSplit.setId(user1.getId());
                billSplit.setName(user1.getName());
                billSplit.setAmount(BigDecimal.ZERO);
                billSplit.setEmail(user1.getEmail());
                billSplits.add(billSplit);
            }
            log.info(billSplits.size());
        } else {
            for (User user1 : users) {
                boolean userExists = false;

                for (BillSplit billsplit : billSplits) {
                    log.info(billsplit);
                    log.info(user1);
                    log.info(billsplit==null);
                    log.info(user1==null);
                    log.info(user1.getId());
                    log.info(billsplit.getId());
                    if (billsplit.getId().equalsIgnoreCase(user1.getId())) {
                        loggedUserAmount.add(billsplit.getAmount());
                        userExists = true;
                        break;
                    };
                }
                if (!userExists) {
                    BillSplit billSplit = new BillSplit();
                    billSplit.setId(user1.getId());
                    billSplit.setName(user1.getName());
                    billSplit.setAmount(BigDecimal.ZERO);
                    billSplit.setEmail(user1.getEmail());
                    billSplits.add(billSplit);
                }
            }
            log.info(billSplits.size());
        }

        /* Logged User Info */
        BillSplit loggedUserBillSplit = new BillSplit();
        loggedUserBillSplit.setId(userId);
        loggedUserBillSplit.setName(user.getName());
        loggedUserBillSplit.setEmail(user.getEmail());
        loggedUserBillSplit.setAmount(loggedUserAmount);

        billSplits.add(loggedUserBillSplit);

        return billSplits;
    }

    @Override
    public List<Bill> recentTrans(String userId) {

        Query query = Query.query(Criteria.where("userPaid").is(userId).orOperator(Criteria.where("billSplits._id").is(userId)));
        Query query1 = Query.query(Criteria.where("userPaid").is(userId).orOperator(Criteria.where("billSplits.id").is(userId)));
        Query query2 = Query.query(Criteria.where("userPaid").is(userId));
        
        log.info(query.toString());
        log.info(query1.toString());
        log.info(query2.toString());

        List<Bill> bills = mongoTemplate.find(query, Bill.class);
        return bills;

    }
}
