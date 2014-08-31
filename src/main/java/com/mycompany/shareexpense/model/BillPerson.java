/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.shareexpense.model;

import java.math.BigInteger;

/**
 *
 * @author AH0661755
 */
public class BillPerson extends AbstractModal{
    
    private String name; 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }
    private String email;
    private BigInteger amount = BigInteger.ZERO;
    
}
