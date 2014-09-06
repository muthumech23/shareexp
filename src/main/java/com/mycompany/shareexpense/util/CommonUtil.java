/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author AH0661755
 */
public class CommonUtil {

    public static boolean checkStringNullBlank (String string) {

        if (string == null || string.isEmpty () || string.trim ().equalsIgnoreCase ("")) {
            return true;
        }

        return false;
    }

    public static <E> List<E> toList (Iterable<E> iterable) {
        if (iterable instanceof List) {
            return (List<E>) iterable;
        }
        ArrayList<E> list = new ArrayList<E> ();
        if (iterable != null) {
            for (E e : iterable) {
                list.add (e);
            }
        }
        return list;
    }

    public static void sendEmail (String subject,
                                  String toAddress,
                                  String body) {

        // Sender's email ID needs to be mentioned
        String from = "muthumech23@gmail.com";//change accordingly
        final String username = "muthumech23";//change accordingly
        final String password = "project23";//change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties ();
        props.put ("mail.smtp.auth", "true");
        props.put ("mail.smtp.starttls.enable", "true");
        props.put ("mail.smtp.host", host);
        props.put ("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance (props,
                new javax.mail.Authenticator () {
                    protected PasswordAuthentication getPasswordAuthentication () {
                        return new PasswordAuthentication (username, password);
                    }

                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage (session);

            // Set From: header field of the header.
            message.setFrom (new InternetAddress (from));

            // Set To: header field of the header.
            message.setRecipients (Message.RecipientType.TO, InternetAddress.parse (toAddress));

            // Set Subject: header field
            message.setSubject ("Testing Subject");

            // Now set the actual message
            message.setText ("Hello, this is sample for to check send "
                    + "email using JavaMailAPI ");

            // Send message
            Transport.send (message);

            System.out.println ("Sent message successfully....");

        }
        catch (MessagingException e) {
            throw new RuntimeException (e);
        }
    }

}
