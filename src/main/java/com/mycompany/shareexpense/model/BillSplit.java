/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.model;

import java.math.BigDecimal;
import org.springframework.data.annotation.Transient;

/**
 *
 * @author AH0661755
 */
public class BillSplit {

    @Transient
    private String splitText;

    private BigDecimal amount;

    private String name;

    @Transient
    private String email;
    
    private String userId;

    public String getSplitText () {
        return splitText;
    }

    public void setSplitText (String splitText) {
        this.splitText = splitText;
    }

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount () {
        return amount;
    }

    public void setAmount (BigDecimal amount) {
        this.amount = amount;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

}
