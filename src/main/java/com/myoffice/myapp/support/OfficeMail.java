package com.myoffice.myapp.support;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;


public class OfficeMail {

	private MailSender mailSender;
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendMail(String[] toMail, String[] ccMail, String[] bccMail, String subject, String body) throws AddressException, MessagingException {
		SimpleMailMessage message = new SimpleMailMessage();
	
		message.setTo(toMail);
		if(ccMail != null) message.setCc(ccMail);
		if(bccMail != null) message.setBcc(bccMail);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}
}
