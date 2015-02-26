package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.model.*;
import com.mycompany.shareexpense.service.TrackExpService;
import com.mycompany.shareexpense.util.CommonUtil;
import com.mycompany.shareexpense.util.CustomException;
import com.mycompany.shareexpense.util.ErrorConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("dashboard")
public class HomeController extends AbstractController {

    /**
     * The log.
     */
    private final Logger log = Logger.getLogger(HomeController.class);

    /**
     * The bill service.
     */
    @Autowired
    public TrackExpService trackExpService;

    /**
     * Submit bill.
     *
     * @param userId the trackExpList
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value="/charts/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public ResponseEntity<ChartsDto> charts(@PathVariable("userId") String userId) throws CustomException {

        ChartsDto chartsDto = null;
        try {

            List<YearSummaryDto> yearSummaryDtos = trackExpService.getYearSummary(userId);
            chartsDto = new ChartsDto();

            chartsDto.setChartYearSummary(chartYearSummary(yearSummaryDtos));
            chartsDto.setChartBudgetSummary(chartBudgetSummary(yearSummaryDtos));

            chartsDto.setChartYear(chartYear(yearSummaryDtos));
            chartsDto.setChartBudget(chartBudget(yearSummaryDtos));

        } catch (CustomException ce) {
            log.error("/trackexp/charts", ce);
            throw ce;
        } catch (Exception e) {
            log.error("/trackexp/charts", e);
            throw new CustomException(ErrorConstants.ERR_GENERAL_FAILURE, "Transaction requested has been failed. Please try again.");
        }

        return new ResponseEntity<>(chartsDto, HttpStatus.OK);
    }

    private ChartDataDto chartYearSummary(List<YearSummaryDto> yearSummaryDtos){

        ChartDataDto chartDataDto = null;

        chartDataDto = new ChartDataDto();
        List<ColumnDto> columnDtos = new ArrayList<>(4);

        ColumnDto columnDto1 = new ColumnDto();
        columnDto1.setId("month");
        columnDto1.setLabel("Month");
        columnDto1.setType("string");
        columnDtos.add(columnDto1);

        ColumnDto columnDto2 = new ColumnDto();
        columnDto2.setId("income-id");
        columnDto2.setLabel("Income");
        columnDto2.setType("number");
        columnDtos.add(columnDto2);

        ColumnDto columnDto3 = new ColumnDto();
        columnDto3.setId("expense-id");
        columnDto3.setLabel("Expenses");
        columnDto3.setType("number");
        columnDtos.add(columnDto3);

        ColumnDto columnDto4 = new ColumnDto();
        columnDto4.setId("saving-id");
        columnDto4.setLabel("Saving");
        columnDto4.setType("number");
        columnDtos.add(columnDto4);

        chartDataDto.setCols(columnDtos);

        List<RowDto> rowDtos = new ArrayList<>(12);

        for(YearSummaryDto yearSummaryDto: yearSummaryDtos){

            RowDto rowDto = new RowDto();

            List<RowColumnDto> rowColumnDtos = new ArrayList<>(4);

            RowColumnDto rowColumnDto1 = new RowColumnDto();
            rowColumnDto1.setV(CommonUtil.months[Integer.parseInt(yearSummaryDto.getMonth())]);
            rowColumnDtos.add(rowColumnDto1);

            RowColumnDto rowColumnDto2 = new RowColumnDto();
            rowColumnDto2.setV(yearSummaryDto.getIncomeAmount()+"");
            rowColumnDtos.add(rowColumnDto2);

            RowColumnDto rowColumnDto3 = new RowColumnDto();
            rowColumnDto3.setV(yearSummaryDto.getExpenseAmount()+"");
            rowColumnDtos.add(rowColumnDto3);

            RowColumnDto rowColumnDto4 = new RowColumnDto();
            rowColumnDto4.setV(yearSummaryDto.getSavingAmount()+"");
            rowColumnDtos.add(rowColumnDto4);

            rowDto.setC(rowColumnDtos);

            rowDtos.add(rowDto);
        }

        chartDataDto.setRows(rowDtos);

        return chartDataDto;

    }

    private ChartDataDto chartBudgetSummary(List<YearSummaryDto> yearSummaryDtos){

        ChartDataDto chartDataDto = null;

        chartDataDto = new ChartDataDto();
        List<ColumnDto> columnDtos = new ArrayList<>(3);

        ColumnDto columnDto1 = new ColumnDto();
        columnDto1.setId("month");
        columnDto1.setLabel("Month");
        columnDto1.setType("string");
        columnDtos.add(columnDto1);

        ColumnDto columnDto2 = new ColumnDto();
        columnDto2.setId("budget-id");
        columnDto2.setLabel("Budget");
        columnDto2.setType("number");
        columnDtos.add(columnDto2);

        ColumnDto columnDto3 = new ColumnDto();
        columnDto3.setId("expense-id");
        columnDto3.setLabel("Expenses");
        columnDto3.setType("number");
        columnDtos.add(columnDto3);

        chartDataDto.setCols(columnDtos);

        List<RowDto> rowDtos = new ArrayList<>(12);

        for(YearSummaryDto yearSummaryDto: yearSummaryDtos){

            RowDto rowDto = new RowDto();

            List<RowColumnDto> rowColumnDtos = new ArrayList<>(3);

            RowColumnDto rowColumnDto1 = new RowColumnDto();
            rowColumnDto1.setV(CommonUtil.months[Integer.parseInt(yearSummaryDto.getMonth())]);
            rowColumnDtos.add(rowColumnDto1);

            RowColumnDto rowColumnDto2 = new RowColumnDto();
            rowColumnDto2.setV(yearSummaryDto.getBudgetAmount()+"");
            rowColumnDtos.add(rowColumnDto2);

            RowColumnDto rowColumnDto3 = new RowColumnDto();
            rowColumnDto3.setV(yearSummaryDto.getExpenseAmount()+"");
            rowColumnDtos.add(rowColumnDto3);

            rowDto.setC(rowColumnDtos);

            rowDtos.add(rowDto);
        }

        chartDataDto.setRows(rowDtos);

        return chartDataDto;

    }

    private List<ChartDto> chartYear(List<YearSummaryDto> yearSummaryDtos){

        List<ChartDto> chartDtos = null;

        chartDtos = new ArrayList<>();

        for(YearSummaryDto yearSummaryDto: yearSummaryDtos){

            ChartDto chartDto = new ChartDto();

            chartDto.setX(CommonUtil.months[Integer.parseInt(yearSummaryDto.getMonth())]);

            List<Object> objects = new ArrayList<>();
            objects.add(yearSummaryDto.getIncomeAmount());
            objects.add(yearSummaryDto.getExpenseAmount());
            objects.add(yearSummaryDto.getSavingAmount());

            chartDto.setY(objects);

            chartDtos.add(chartDto);
        }

        return chartDtos;

    }

    private List<ChartDto> chartBudget(List<YearSummaryDto> yearSummaryDtos){

        List<ChartDto> chartDtos = null;

        chartDtos = new ArrayList<>();

        for(YearSummaryDto yearSummaryDto: yearSummaryDtos){

            ChartDto chartDto = new ChartDto();

            chartDto.setX(CommonUtil.months[Integer.parseInt(yearSummaryDto.getMonth())]);

            List<Object> objects = new ArrayList<>();
            objects.add(yearSummaryDto.getBudgetAmount());
            objects.add(yearSummaryDto.getExpenseAmount());

            chartDto.setY(objects);

            chartDtos.add(chartDto);
        }

        return chartDtos;

    }

}
