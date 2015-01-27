package com.mycompany.shareexpense.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 
DOCUMENT ME!
 *
 * @author AH0661755
 */
public class UsersBalance {
    /**
     * DOCUMENT ME!
     */
    private List<AmtCurr> amtCurrs;


    /**
     * DOCUMENT ME!
     */
    private String name;


    /**
     * DOCUMENT ME!
     */
    private String email;


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
    public String getUserId() {
        return userId;
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
}
