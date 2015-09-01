package com.myoffice.myapp.support;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class OfficeMail {

	public void sendMail(String fromMail, String password, String toMail, String subject, String text) throws AddressException, MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");
		
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage generateMailMessage = new MimeMessage(session);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
		generateMailMessage.setSubject(subject);
		generateMailMessage.setText(text);
		System.out.println("Mail Session has been created successfully..");
		
		Transport transport = session.getTransport("smtp");
		transport.connect("smtp.gmail.com", fromMail, password);
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
}
