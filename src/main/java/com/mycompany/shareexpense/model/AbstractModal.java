package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author AH0661755
 */
public class AbstractModal {

    @Id
    private String id;

    @Indexed
    private String createDate = null;

    @Indexed
    private String modifiedDate = null;

    private String by = null;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getBy() {

        return by;
    }


    public void setBy(String by) {

        this.by = by;
    }

    public void setCreateDate(String createDate) {

        this.createDate = createDate;
    }

    public void setModifiedDate(String modifiedDate) {

        this.modifiedDate = modifiedDate;
    }

    public String getCreateDate() {

        return createDate;
    }

    public String getModifiedDate() {

        return modifiedDate;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getId() {

        return id;
    }

}
