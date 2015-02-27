package com.mycompany.shareexpense;

import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

        String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        String signature = "";
        byte dearr[] = Base64.decodeBase64(signature.getBytes());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File("C:\\AndroidApp\\signature_"+ fileName +".png"));
            fos.write(dearr);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
