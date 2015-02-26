package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author AH0661755
 */
public class ChartDto {

    private String x;

    private List<Object> y;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public List<Object> getY() {
        return y;
    }

    public void setY(List<Object> y) {
        this.y = y;
    }
}
