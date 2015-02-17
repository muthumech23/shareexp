package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;


/**
 * DOCUMENT ME!
 *
 * @author AH0661755
 */
public class BillSplit {
    /**
     * DOCUMENT ME!
     */
    @Transient
    private String splitText;


    /**
     * DOCUMENT ME!
     */
    private BigDecimal amount;


    /**
     * DOCUMENT ME!
     */
    private String amountStatus;


    /**
     * DOCUMENT ME!
     */
    private String name;


    /**
     * DOCUMENT ME!
     */
    @Transient
    private String email;


    /**
     * DOCUMENT ME!
     */
    @Transient
    private String currency;


    /**
     * DOCUMENT ME!
     */
    private String userId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCurrency() {
        return currency;
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
    public String getSplitText() {
        return splitText;
    }


    /**
     * DOCUMENT ME!
     *
     * @param splitText DOCUMENT ME!
     */
    public void setSplitText(String splitText) {
        this.splitText = splitText;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUserId() {
        return userId;
    }


    /**
     * DOCUMENT ME!
     *
     * @param userId DOCUMENT ME!
     */
    public void setUserId(String userId) {
        this.userId = userId;
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
    public String getEmail() {
        return email;
    }


    /**
     * DOCUMENT ME!
     *
     * @param email DOCUMENT ME!
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }


    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
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
