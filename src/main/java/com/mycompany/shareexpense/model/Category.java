package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * DOCUMENT ME!
 *
 * @author AH0661755
 */
@Document(collection = "categories")
@CompoundIndexes({
        @CompoundIndex(name = "userCategory", def = "{'userId' : 1, 'category' : 1}", unique = true)
})
public class Category extends AbstractModal {

    private String userId;

    /**
     * DOCUMENT ME!
     */
    private String category;

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
