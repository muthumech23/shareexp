package com.mycompany.shareexpense.model;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * DOCUMENT ME!
 *
 * @author AH0661755
 */
public class UserDto extends AbstractModal {
    /** DOCUMENT ME! */
    private List<AmtCurr> amtCurrs;


    /**
     * DOCUMENT ME!
     */
    private String userId;


    /**
     * DOCUMENT ME!
     */
    private String currency;


    /** DOCUMENT ME! */
    private String loggedUser;


    /** DOCUMENT ME! */
    private BigDecimal amount;


    /**
     * DOCUMENT ME!
     */
    private String amountStatus;

    @Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    	}
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<AmtCurr> getAmtCurrs() {
        return amtCurrs;
    }


    /**
     * DOCUMENT ME!
     *
     * @param amtCurrs DOCUMENT ME!
     */
    public void setAmtCurrs(List<AmtCurr> amtCurrs) {
        this.amtCurrs = amtCurrs;
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
    public String getLoggedUser() {
        return loggedUser;
    }


    /**
     * DOCUMENT ME!
     *
     * @param loggedUser DOCUMENT ME!
     */
    public void setLoggedUser(String loggedUser) {
        this.loggedUser = loggedUser;
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
}
