package com.mycompany.shareexpense;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TestStandalone {

    public static void main(String[] args) {

        DateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy");
        //get current date time with Date()
        Date date = new Date();
        System.out.println(dateFormat.format(date));

    }

}
