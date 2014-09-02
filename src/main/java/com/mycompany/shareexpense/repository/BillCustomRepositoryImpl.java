/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.BillSplit;
import com.mycompany.shareexpense.model.User;
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

    @Override
    public List<BillSplit> findAllBills(String userId, List<User> users) {

        AggregationOperation match = Aggregation.match(Criteria.where("billSplits.userId").is(userId));

        AggregationOperation group = Aggregation.group("billSplits.userId").sum("billSplits.amount").as("amount");

        Aggregation aggregation = Aggregation.newAggregation(match, group);
        AggregationResults<BillSplit> result = mongoTemplate.aggregate(aggregation, "bills", BillSplit.class);

        List<BillSplit> billSplits = result.getMappedResults();

        log.info(billSplits.isEmpty());
        if (billSplits.isEmpty()) {
            billSplits = new ArrayList<>();
            for (User user : users) {
                BillSplit billSplit = new BillSplit();
                billSplit.setId(user.getId());
                billSplit.setName(user.getName());
                billSplit.setAmount(BigInteger.ZERO);
                billSplit.setEmail(user.getEmail());
                billSplits.add(billSplit);
            }
            log.info(billSplits.size());
        } else {
            for (User user : users) {
                boolean userExists = false;

                for (BillSplit billsplit : billSplits) {
                    if (billsplit.getId().equalsIgnoreCase(user.getId())) {
                        userExists = true;
                        break;
                    };
                }
                if (!userExists) {
                    BillSplit billSplit = new BillSplit();
                    billSplit.setId(user.getId());
                    billSplit.setName(user.getName());
                    billSplit.setAmount(BigInteger.ZERO);
                    billSplit.setEmail(user.getEmail());
                    billSplits.add(billSplit);
                }
            }
            log.info(billSplits.size());
        }

        return billSplits;
    }

    public List<BillSplit> recentTrans(String userId) {

        return null;

    }
}
