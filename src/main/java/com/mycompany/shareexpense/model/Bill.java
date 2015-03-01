package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author AH0661755
 */
@Document(collection = "bills")
public class Bill extends AbstractModal {
    /**
     * DOCUMENT ME!
     */
    @Indexed
    private BigDecimal amount;


    /**
     * DOCUMENT ME!
     */
    private String description;


    /**
     * DOCUMENT ME!
     */
    private String amountStatus;


    /**
     * DOCUMENT ME!
     */
    @Indexed
    private String date;

    /**
     * DOCUMENT ME!
     */
    @Indexed
    private Date billDate;

    /**
     * DOCUMENT ME!
     */
    private String category;


    /**
     * DOCUMENT ME!
     */
    private String currency;


    /**
     * DOCUMENT ME!
     */
    @Indexed
    private String userPaid;


    /**
     * DOCUMENT ME!
     */
    @Indexed
    private String groupId;


    /**
     * DOCUMENT ME!
     */
    private String splitType;

    /**
     * DOCUMENT ME!
     */
    @Transient
    private boolean emailRequired;

    /**
     * DOCUMENT ME!
     */
    private List<BillSplit> billSplits;

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCurrency() {
        return currency;
    }


    public boolean isEmailRequired() {

        return emailRequired;
    }


    public void setEmailRequired(boolean emailRequired) {

        this.emailRequired = emailRequired;
    }


    /**
     * DOCUMENT ME!
     *
     * @param currency DOCUMENT ME!
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSplitType() {
        return splitType;
    }


    /**
     * DOCUMENT ME!
     *
     * @param splitType DOCUMENT ME!
     */
    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<BillSplit> getBillSplits() {
        return billSplits;
    }


    /**
     * DOCUMENT ME!
     *
     * @param billSplits DOCUMENT ME!
     */
    public void setBillSplits(List<BillSplit> billSplits) {
        this.billSplits = billSplits;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BigDecimal getAmount() {
        return amount;
    }


    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getGroupId() {
        return groupId;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUserPaid() {
        return userPaid;
    }


    /**
     * DOCUMENT ME!
     *
     * @param userPaid DOCUMENT ME!
     */
    public void setUserPaid(String userPaid) {
        this.userPaid = userPaid;
    }


    /**
     * DOCUMENT ME!
     *
     * @param groupId DOCUMENT ME!
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }


    /**
     * DOCUMENT ME!
     *
     * @param description DOCUMENT ME!
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDate() {
        return date;
    }


    /**
     * DOCUMENT ME!
     *
     * @param date DOCUMENT ME!
     */
    public void setDate(String date) {
        this.date = date;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCategory() {
        return category;
    }


    /**
     * DOCUMENT ME!
     *
     * @param category DOCUMENT ME!
     */
    public void setCategory(String category) {
        this.category = category;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAmountStatus() {
        return amountStatus;
    }


    /**
     * DOCUMENT ME!
     *
     * @param amountStatus DOCUMENT ME!
     */
    public void setAmountStatus(String amountStatus) {
        this.amountStatus = amountStatus;
    }
}
