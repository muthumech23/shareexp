package com.mycompany.shareexpense.model;

import java.math.BigDecimal;


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
    private String userId;

    /**
     * DOCUMENT ME!
     */
    private String loggedUser;

    /**
     * DOCUMENT ME!
     */
    private BigDecimal amount;

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
