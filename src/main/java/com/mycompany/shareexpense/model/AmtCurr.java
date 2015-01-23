package com.mycompany.shareexpense.model;

import java.math.BigDecimal;


public class AmtCurr {
	
	private String		currency;

	private BigDecimal		amount;
	
	
	public String getCurrency() {
	
		return currency;
	}

	
	public void setCurrency(String currency) {
	
		this.currency = currency;
	}

	
	public BigDecimal getAmount() {
	
		return amount;
	}

	
	public void setAmount(BigDecimal amount) {
	
		this.amount = amount;
	}

	
	public String getAmountStatus() {
	
		return amountStatus;
	}

	
	public void setAmountStatus(String amountStatus) {
	
		this.amountStatus = amountStatus;
	}

	private String		amountStatus;
	

}
