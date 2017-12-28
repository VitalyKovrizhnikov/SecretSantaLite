/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dias.idp;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;


/**
 *
 * @author vkovrizhnikov
 */
public class EMailHandler {
    
	private static final String UTF_8 = "utf-8";

	private String smtpHost;
	private String smtpPort;
	private String smtpAuth;
	private String smtpSSL;
	private String smtpDebug;
	private String smtpLogin;
	private String smtpPassword;
	private String smtpCharset;
	private String smtpFrom;
	

        
        public EMailHandler(String Host, String Port, String Login, String Password) {
		smtpAuth = "true";
                smtpCharset = UTF_8;
                smtpHost = Host;
                smtpPort = Port;
                smtpLogin = Login;
                smtpPassword = Password;
                smtpSSL = "false";
                smtpDebug = "false";
	}
        
    
    	private boolean isEmpty(String s) {
		return (s == null || s.isEmpty());
	}
        
        
	private void setProps(Properties props, String key, String value) {
		if (!isEmpty(value)) {
			props.put(key, value);
		}
	}
        
        public void send(String from, String to, String subject,
			String body, String charset) throws Exception {

        // create some properties and get the default Session
        Properties props = new Properties();
        setProps(props, "mail.smtp.host", smtpHost);
       	setProps(props, "mail.smtp.port", smtpPort);
       	setProps(props, "mail.smtp.auth", smtpAuth);
       	setProps(props, "mail.smtp.ssl.enable", smtpSSL);
       	setProps(props, "mail.debug", smtpDebug);
       	setProps(props, "mail.smtp.connectiontimeout", "10000");
       	setProps(props, "mail.smtp.timeout", "60000");
        Session session = Session.getInstance(props, null);
        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(body, charset);
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
            try {
            	if (Boolean.valueOf(smtpAuth) && !isEmpty(smtpLogin) && !isEmpty(smtpPassword)) {
            		t.connect(smtpHost, smtpLogin, smtpPassword);
            	} else {
            		t.connect();
            	}
                t.sendMessage(msg, msg.getAllRecipients());
            } finally {
                t.close();
            }
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
		
	}
    
}
