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
public class Contact extends AbstractModal {
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
    private String message;

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
