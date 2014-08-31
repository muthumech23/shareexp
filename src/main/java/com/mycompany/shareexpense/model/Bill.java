/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.model;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author AH0661755
*/
@Document(collection = "bills")
public class Bill extends AbstractModal {

    @Indexed
    private BigInteger amount;
    
    private String description;
    @Indexed
    private Date date;
    
    private String Category;

    @Indexed
    private String paidUser;

    @Indexed
    private String groupId;

    @Transient
    public List<BillSplit> getBillSplits() {
        return billSplits;
    }

    public void setBillSplits(List<BillSplit> billSplits) {
        this.billSplits = billSplits;
    }
    
    private List<BillSplit> billSplits;

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public String getPaidUser() {
        return paidUser;
    }

    public void setPaidUser(String paidUser) {
        this.paidUser = paidUser;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

}
 