package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 
DOCUMENT ME!
 *
 * @author AH0661755
 */
@Document(collection = "usersecures")
public class UserSecure extends AbstractModal {
    /**
     * DOCUMENT ME!
     */
    @Indexed(unique = true)
    private String userId;


    /**
     * DOCUMENT ME!
     */
    private String password;


    /**
     * DOCUMENT ME!
     */
    private String status;


    /**
     * DOCUMENT ME!
     */
    private String randomString;

    @Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getRandomString() {
        return randomString;
    }


    /**
     * DOCUMENT ME!
     *
     * @param randomString DOCUMENT ME!
     */
    public void setRandomString(String randomString) {
        this.randomString = randomString;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStatus() {
        return status;
    }


    /**
     * DOCUMENT ME!
     *
     * @param status DOCUMENT ME!
     */
    public void setStatus(String status) {
        this.status = status;
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
    public String getPassword() {
        return password;
    }


    /**
     * DOCUMENT ME!
     *
     * @param password DOCUMENT ME!
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
