package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class TrackExpList extends AbstractModal {

    private List<Expense> trackExpenses;

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public List<Expense> getTrackExpenses() {
        return trackExpenses;
    }

    public void setTrackExpenses(List<Expense> trackExpenses) {
        this.trackExpenses = trackExpenses;
    }
}
