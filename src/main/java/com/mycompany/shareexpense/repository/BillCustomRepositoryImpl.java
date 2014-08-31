/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.BillPerson;
import com.mycompany.shareexpense.model.User;
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

    @Override
    public List<BillPerson> findAllBills(String userId) {
        
        AggregationOperation match = Aggregation.match(Criteria.where("userId").is(userId).orOperator(Criteria.where("paidId").is(userId)));

        AggregationOperation group = Aggregation.group("userId").sum("amount").as("amount");

        Aggregation aggregation = Aggregation.newAggregation(match, group);
        AggregationResults<BillPerson> result = mongoTemplate.aggregate(aggregation, "billsplits", BillPerson.class);

        Query query = Query.query(Criteria.where("friends").is(userId));

        List<BillPerson> users = mongoTemplate.find(query, BillPerson.class, "users");
        
        for(BillPerson billPerson: users){
            String Id = billPerson.getId();
            
            for(BillPerson billPerson1: result.getMappedResults()){
                if(Id.equalsIgnoreCase(billPerson1.getId())){
                    billPerson.setAmount(billPerson1.getAmount());
                }
            }
        }
        return users;
    }
}
