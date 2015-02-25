package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "budgets")
public class Budget extends AbstractModal {

    @Indexed(unique = true)
    private String userId;

    private List<KeyValue> categories;

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<KeyValue> getCategories() {
        return categories;
    }

    public void setCategories(List<KeyValue> categories) {
        this.categories = categories;
    }
}
