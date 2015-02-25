package com.mycompany.shareexpense;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TestStandalone {

    public static void main(String[] args) {

        DateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy");
        //get current date time with Date()
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 02-1, 1);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        //startDate = calendar.getTime();
        System.out.println("Start Date: " + calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        //endDate = calendar.getTime();
        System.out.println("End Date: " + calendar.getTime());


            System.out.println(Calendar.getInstance().get(Calendar.MONTH)+1);


    }

}
