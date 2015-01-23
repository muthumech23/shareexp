package com.mycompany.shareexpense.model;

import java.math.BigDecimal;
import java.util.List;


/**
 * 
DOCUMENT ME!
 *
 * @author AH0661755
 */
public class UserDto extends AbstractModal {
    /**
     * DOCUMENT ME!
     */
	
	private List<AmtCurr> amtCurrs ;
	
    
	public List<AmtCurr> getAmtCurrs() {
	
		return amtCurrs;
	}



	
	public void setAmtCurrs(List<AmtCurr> amtCurrs) {
	
		this.amtCurrs = amtCurrs;
	}


	private String userId;
    
    private String currency;

    
	public String getCurrency() {
	
		return currency;
	}


	
	public void setCurrency(String currency) {
	
		this.currency = currency;
	}


	/**
     * DOCUMENT ME!
     */
    private String loggedUser;

    /**
     * DOCUMENT ME!
     */
    private BigDecimal amount;
    
    private String amountStatus;

    
	public String getAmountStatus() {
	
		return amountStatus;
	}



	
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
