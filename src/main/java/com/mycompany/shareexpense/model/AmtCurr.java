package com.mycompany.shareexpense.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * DOCUMENT ME!
 *
 * @author Muthukumaran Swaminathan
 * @version $Revision$
  */
public class AmtCurr {
    /**
     * DOCUMENT ME!
     */
    private String currency;


    /**
     * DOCUMENT ME!
     */
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
