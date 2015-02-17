package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author AH0661755
 */
@Document(collection = "users")
public class User extends AbstractModal {
    /**
     * DOCUMENT ME!
     */
    private String name;


    /**
     * DOCUMENT ME!
     */
    @Transient
    private String status;


    /**
     * DOCUMENT ME!
     */
    @Indexed(unique = true)
    private String email;


    /**
     * DOCUMENT ME!
     */
    @Transient
    private String password;


    /**
     * DOCUMENT ME!
     */
    @Indexed
    private List<String> friends;

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
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
     * @param password DOCUMENT ME!
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @return DOCUMENT ME!
     */
    public String getEmail() {
        return email;
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
     * @return DOCUMENT ME!
     */
    public List<String> getFriends() {
        return friends;
    }


    /**
     * DOCUMENT ME!
     *
     * @param friends DOCUMENT ME!
     */
    public void setFriends(List<String> friends) {
        this.friends = friends;
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
}
