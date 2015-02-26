package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author AH0661755
 */
public class RowDto {

    private List<RowColumnDto> c;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public List<RowColumnDto> getC() {
        return c;
    }

    public void setC(List<RowColumnDto> c) {
        this.c = c;
    }
}
