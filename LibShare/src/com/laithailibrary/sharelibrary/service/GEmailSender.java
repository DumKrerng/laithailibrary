package com.laithailibrary.sharelibrary.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import com.laithailibrary.logger.*;
import exc.*;

/**
 * Created by dumkrerng on 21/8/2559.
 */
public class GEmailSender {

	private static SMTPServer s_smtpserver = SMTPServer.Other;

	private static String s_strSMTPServer = "";
	private static String s_strSMTPPort = "587";

	private static String s_strAdminEmail = "";
	private static String s_strPassword = "";

	private GEmailSender() {}

	public static void setAdminEmail(String p_strSMTPServer, int p_intSMTPPort, String p_strAdminEmail, String p_setPassword) {
		s_smtpserver = SMTPServer.Other;

		s_strSMTPServer = p_strSMTPServer;
		s_strSMTPPort = String.valueOf(p_intSMTPPort);
		s_strAdminEmail = p_strAdminEmail;
		s_strPassword = p_setPassword;
	}

	public static void setAdminEmail(String p_strSMTPServer, String p_strAdminEmail, String p_setPassword) {
		s_smtpserver = SMTPServer.Other;

		s_strSMTPServer = p_strSMTPServer;
		s_strAdminEmail = p_strAdminEmail;
		s_strPassword = p_setPassword;
	}

	public static void setAdminEmail_Gmail(String p_strAdminEmail, String p_setPassword) {
		s_smtpserver = SMTPServer.Gmail;

		s_strSMTPServer = "smtp.gmail.com";
		s_strAdminEmail = p_strAdminEmail;
		s_strPassword = p_setPassword;
	}

	public static void setAdminEmail_Gmail(int p_intSMTPPort, String p_strAdminEmail, String p_setPassword) {
		s_smtpserver = SMTPServer.Gmail;

		s_strSMTPServer = "smtp.gmail.com";
		s_strSMTPPort = String.valueOf(p_intSMTPPort);
		s_strAdminEmail = p_strAdminEmail;
		s_strPassword = p_setPassword;
	}

	public static void setAdminEmail_Yahoo(String p_strAdminEmail, String p_setPassword) {
		s_smtpserver = SMTPServer.Yahoo;

		s_strSMTPServer = "smtp.mail.yahoo.com";
		s_strAdminEmail = p_strAdminEmail;
		s_strPassword = p_setPassword;
	}

	public static void setAdminEmail_Yahoo(int p_intSMTPPort, String p_strAdminEmail, String p_setPassword) {
		s_smtpserver = SMTPServer.Yahoo;

		s_strSMTPServer = "smtp.mail.yahoo.com";
		s_strSMTPPort = String.valueOf(p_intSMTPPort);
		s_strAdminEmail = p_strAdminEmail;
		s_strPassword = p_setPassword;
	}

	public static void send(String p_strSubject, String p_strContentMessage, String p_strEmail_TO, String p_strEmail_CC) throws GException {
		try {
			if(p_strEmail_TO.length() <= 0) {
				throw new GException("Invalid Email(To)!!!");
			}

			if(s_strSMTPServer.length() <= 0
				|| s_strAdminEmail.length() <= 0
				|| s_strPassword.length() <= 0) {

				throw new GException("Invalid Admin Email(From)!!!");
			}

			// Step1
			Properties mailServerProperties = new Properties();
			mailServerProperties.put("mail.smtp.port", s_strSMTPPort);
			mailServerProperties.put("mail.smtp.auth", "true");
			mailServerProperties.put("mail.smtp.starttls.enable", "true");
			mailServerProperties.put("mail.smtp.connectiontimeout", 20000);

			if(s_smtpserver == SMTPServer.Yahoo) {
				mailServerProperties.put("mail.smtp.from", s_strAdminEmail);
				mailServerProperties.put("mail.smtp.localhost", s_strAdminEmail);
				mailServerProperties.put("mail.smtp.user", s_strAdminEmail);
				mailServerProperties.put("mail.smtp.host", s_strSMTPServer);
			}

			// Step2
			Session sessionMail = Session.getDefaultInstance(mailServerProperties, null);
			Message message = new MimeMessage(sessionMail);

			String[] strEmailTOs = p_strEmail_TO.split(" ");

			for(String strEmailTO : strEmailTOs) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(strEmailTO));
			}

			message.addRecipient(Message.RecipientType.CC, new InternetAddress(s_strAdminEmail));

			if(p_strEmail_CC.length() > 0) {
				String[] strEmailCCs = p_strEmail_CC.split(" ");

				for(String strEmail_CC : strEmailCCs) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(strEmail_CC));
				}
			}


//			message.setHeader("Content-Type", "text/html; charset=UTF-8");
			message.setSubject(p_strSubject);
//			message.setContent(p_strContentMessage, "text/html");
			message.setContent(p_strContentMessage, "text/html; charset=\"UTF-8\"");

			// Step3
			Transport transport = sessionMail.getTransport("smtp");

			// Enter your correct UserID and Password
			// if you have 2FA enabled then provide App Specific Password
			// Google SMTP Server = smtp.gmail.com
			// Yahoo SMTP Server = smtp.mail.yahoo.com

			transport.connect(s_strSMTPServer, s_strAdminEmail, s_strPassword);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

			GLog.info("Send email to " + p_strEmail_TO);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
