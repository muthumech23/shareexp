/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Properties;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 *
 * @author AH0661755
 */
public class CommonUtil {

    private final Logger log = Logger.getLogger (CommonUtil.class);
    
    @Autowired
    private static Environment environment;
    
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
    
    public static boolean authenticate(String attemptedPassword,
                                byte[] encryptedPassword,
                                byte[] salt)
                         throws NoSuchAlgorithmException, InvalidKeySpecException {
        
        // Encrypt the clear-text password using the same salt that was used to
        // encrypt the original password
        byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);

        // Authentication succeeds if encrypted password that the user entered
        // is equal to the stored hash
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }


    public static byte[] getEncryptedPassword(String password,
                                       byte[] salt)
                                throws NoSuchAlgorithmException, InvalidKeySpecException {
        
        // PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
        // specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
        String algorithm = "PBKDF2WithHmacSHA1";

        // SHA-1 generates 160 bit hashes, so that's what makes sense here
        int derivedKeyLength = 160;

        // Pick an iteration count that works for you. The NIST recommends at
        // least 1,000 iterations:
        int iterations = 20000;

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);

        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

        return f.generateSecret(spec).getEncoded();
    }


    public static void sendEmail (String subject,
                                  String toAddress,
                                  String body) {

        // Sender's email ID needs to be mentioned
        String from = environment.getProperty ("mail.from");//change accordingly
        final String username = environment.getProperty ("mail.user");//change accordingly
        final String password = environment.getProperty ("mail.pwd");//change accordingly

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
