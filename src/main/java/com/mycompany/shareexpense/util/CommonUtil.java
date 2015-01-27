
package com.mycompany.shareexpense.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.Properties;
import java.util.TimeZone;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.core.env.Environment;

/**
 * @author AH0661755
 */
public class CommonUtil {

	private static Logger	log	= Logger.getLogger(CommonUtil.class);

	public static boolean checkStringNullBlank(String string) {

		if (string == null || string.isEmpty() || string.trim().equalsIgnoreCase("")) {
			return true;
		}

		return false;
	}

	public static <E> List<E> toList(Iterable<E> iterable) {

		if (iterable instanceof List) {
			return (List<E>) iterable;
		}
		ArrayList<E> list = new ArrayList<E>();
		if (iterable != null) {
			for (E e : iterable) {
				list.add(e);
			}
		}
		return list;
	}
	
	public static String getEncryptedPassword(String inputPassword){
		
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		String encryptedPassword = passwordEncryptor.encryptPassword(inputPassword);
		
		return encryptedPassword;
	}
	
public static boolean checkPassword(String inputPassword, String encryptedPassword){
		
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		return passwordEncryptor.checkPassword(inputPassword, encryptedPassword);
	}

	public static boolean authenticate(	String attemptedPassword,
										byte[] encryptedPassword,
										byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

		// Encrypt the clear-text password using the same salt that was used to
		// encrypt the original password
		byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);

		// Authentication succeeds if encrypted password that the user entered
		// is equal to the stored hash
		return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
	}
	
	public static String getSHA256Hash(String inputPassed)throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(inputPassed.getBytes());
 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	inputPassed = hexString.toString();
    	
    	return inputPassed;
    }

	public static byte[] getEncryptedPassword(	String password,
												byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

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

	public static void sendEmail(	String subject,
									String toAddress,
									String body,
									Environment env) {

		log.debug("emailbody --> " + body);
		log.debug("environment --> " + env);
		// Sender's email ID needs to be mentioned
		String from = env.getProperty("mail.from");// change accordingly
		log.debug("from --> " + from);
		final String username = env.getProperty("mail.user");// change accordingly
		final String password = env.getProperty("mail.pwd");// change accordingly

		// Assuming you are sending email through relay.jangosmtp.net
		String host = "smtp.gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(username, password);
			}

		});

		try {


			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

			// Set Subject: header field
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();
			
			body = env.getProperty("mail.template.top") + body + env.getProperty("mail.template.bottom");

			// Fill the message
			messageBodyPart.setContent(body, "text/html; charset=utf-8");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			/*
			 * messageBodyPart = new MimeBodyPart();
			 * String filename = "file.txt";
			 * DataSource source = new FileDataSource(filename);
			 * messageBodyPart.setDataHandler(new DataHandler(source));
			 * messageBodyPart.setFileName(filename);
			 * multipart.addBodyPart(messageBodyPart);
			 */

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static enum Mode {
		ALPHA, ALPHANUMERIC, NUMERIC
	}

	public static String generateRandomString(	int length,
												Mode mode) throws Exception {

		StringBuffer buffer = new StringBuffer();
		String characters = "";

		switch (mode) {

			case ALPHA:
				characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
				break;

			case ALPHANUMERIC:
				characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
				break;

			case NUMERIC:
				characters = "1234567890";
				break;
		}

		int charactersLength = characters.length();

		for (int i = 0; i < length; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		return buffer.toString();
	}
	
	public static Date cvtToGmt( Date date ){
	    TimeZone tz = TimeZone.getDefault();
	    Date ret = new Date( date.getTime() - tz.getRawOffset() );

	    // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
	    if ( tz.inDaylightTime( ret )){
	        Date dstDate = new Date( ret.getTime() - tz.getDSTSavings() );

	        // check to make sure we have not crossed back into standard time
	        // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
	        if ( tz.inDaylightTime( dstDate )){
	            ret = dstDate;
	        }
	     }
	     return ret;
	}

}
