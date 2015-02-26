package com.mycompany.shareexpense.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author AH0661755
 */
public class ChartsDto {

    private ChartDataDto chartYearSummary;

    private ChartDataDto chartBudgetSummary;

    private List<ChartDto> chartYear;

    private List<ChartDto> chartBudget;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public ChartDataDto getChartYearSummary() {
        return chartYearSummary;
    }

    public void setChartYearSummary(ChartDataDto chartYearSummary) {
        this.chartYearSummary = chartYearSummary;
    }

    public ChartDataDto getChartBudgetSummary() {
        return chartBudgetSummary;
    }

    public void setChartBudgetSummary(ChartDataDto chartBudgetSummary) {
        this.chartBudgetSummary = chartBudgetSummary;
    }

    public List<ChartDto> getChartYear() {
        return chartYear;
    }

    public void setChartYear(List<ChartDto> chartYear) {
        this.chartYear = chartYear;
    }

    public List<ChartDto> getChartBudget() {
        return chartBudget;
    }

    public void setChartBudget(List<ChartDto> chartBudget) {
        this.chartBudget = chartBudget;
    }
}
