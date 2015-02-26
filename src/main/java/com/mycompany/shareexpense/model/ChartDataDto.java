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
public class ChartDataDto {

    private List<ColumnDto> cols;

    private List<RowDto> rows;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public List<ColumnDto> getCols() {
        return cols;
    }

    public void setCols(List<ColumnDto> cols) {
        this.cols = cols;
    }

    public List<RowDto> getRows() {
        return rows;
    }

    public void setRows(List<RowDto> rows) {
        this.rows = rows;
    }
}
