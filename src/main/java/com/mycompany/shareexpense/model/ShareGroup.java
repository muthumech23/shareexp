package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/**
 * 
DOCUMENT ME!
 *
 * @author AH0661755
 */
@Document(collection = "sharegroups")
public class ShareGroup extends AbstractModal {
    /**
     * DOCUMENT ME!
     */
    private String groupName;


    /**
     * DOCUMENT ME!
     */
    private String email;


    /**
     * DOCUMENT ME!
     */
    private List<String> userIds;


    /**
     * DOCUMENT ME!
     */
    @Indexed
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
    public String getGroupName() {
        return groupName;
    }


    /**
     * DOCUMENT ME!
     *
     * @param groupName DOCUMENT ME!
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
    public List<String> getUserIds() {
        return userIds;
    }


    /**
     * DOCUMENT ME!
     *
     * @param userIds DOCUMENT ME!
     */
    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
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
}
