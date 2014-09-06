/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.Bill;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author AH0661755
 */
@Repository
public interface BillRepository extends CrudRepository<Bill, String> {

    public List<Bill> findByUserPaidOrBillSplitsUserId (String Id,
                                                        String userId);

    public List<Bill> findByGroupId (String groupId);

}


/*
 * List<Bill> billsCollection =
 * billRepository.findByUserPaidOrBillSplitsUserId(userId, userId);
 *
 * AggregationOperation match =
 * Aggregation.match(Criteria.where("userPaid").is(userId));
 *
 * AggregationOperation group =
 * Aggregation.group("billSplits.userId").sum("billSplits.amount").as("amount");
 *
 * Aggregation aggregation = Aggregation.newAggregation(match, group);
 * AggregationResults<Bill> result = mongoTemplate.aggregate(aggregation,
 * "bills", Bill.class);
 *
 * List<Bill> bills = result.getMappedResults();
 *
 * for(Bill bill: bills){ log.info(bill.getId());
 * log.info(bill.getBillSplits());
 *
 * for(BillSplit billSplit: bill.getBillSplits()){
 * log.info(billSplit.getAmount()); log.info(billSplit.getUserId());
 * log.info(billSplit.getName()); } }
 *
 * List<BillSplit> billSplits = bills.get(0).getBillSplits(); BigDecimal
 * loggedUserAmount = BigDecimal.ZERO;
 *
 * log.info(billSplits.isEmpty()); if (billSplits.isEmpty()) { billSplits = new
 * ArrayList<>(); for (User user1 : users) { BillSplit billSplit = new
 * BillSplit(); billSplit.setUserId(user1.getId());
 * billSplit.setName(user1.getName()); billSplit.setAmount(BigDecimal.ZERO);
 * billSplit.setEmail(user1.getEmail()); billSplits.add(billSplit); }
 * log.info(billSplits.size()); } else { for (User user1 : users) { boolean
 * userExists = false;
 *
 * for (BillSplit billsplit : billSplits) { log.info(billsplit);
 * log.info(user1); log.info(billsplit==null); log.info(user1==null);
 * log.info(user1.getId()); log.info(billsplit.getUserId()); if
 * (billsplit.getUserId().equalsIgnoreCase(user1.getId())) {
 * loggedUserAmount.add(billsplit.getAmount()); userExists = true; break; }; }
 * if (!userExists) { BillSplit billSplit = new BillSplit();
 * billSplit.setUserId(user1.getId()); billSplit.setName(user1.getName());
 * billSplit.setAmount(BigDecimal.ZERO); billSplit.setEmail(user1.getEmail());
 * billSplits.add(billSplit); } } log.info(billSplits.size()); }
 *
 * /* Logged User Info
 *
 * BillSplit loggedUserBillSplit = new BillSplit();
 * loggedUserBillSplit.setUserId(userId);
 * loggedUserBillSplit.setName(user.getName());
 * loggedUserBillSplit.setEmail(user.getEmail());
 * loggedUserBillSplit.setAmount(loggedUserAmount);
 *
 * billSplits.add(loggedUserBillSplit);
 *
 * return billSplits;
 */
