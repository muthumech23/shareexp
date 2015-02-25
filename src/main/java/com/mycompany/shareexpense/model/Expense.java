package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Document(collection = "expenses")
public class Expense extends AbstractModal {

    @Indexed
    private Date expenseDate;

    private String expenseDate1;

    @Indexed
    private String userId;

    private List<KeyValue> categories;

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getExpenseDate1() {
        return expenseDate1;
    }

    public void setExpenseDate1(String expenseDate1) {
        this.expenseDate1 = expenseDate1;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public List<KeyValue> getCategories() {
        return categories;
    }

    public void setCategories(List<KeyValue> categories) {
        this.categories = categories;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
