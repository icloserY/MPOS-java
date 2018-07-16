package model;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import model.vo.*;

public class Mail {

	
	public static boolean sendMail(MailVo mailVo){
		String user = "jptj1217";
		String password = "tj08080416";
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.daum.net");
		props.put("mail.smtp.port", 465);
		props.put("mail.smtp.auth", "true");
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(user, password);
			}
		});
		
		try{
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress( mailVo.getEmail() ));
			
			
			message.setSubject(mailVo.getSubject());
			String content = "/view/mail/" + mailVo.getContent() + ".jsp";
			message.setContent(content, "text/html");
			
			Transport.send(message);
			return true;
		}catch(MessagingException e){
			return false;
		}
		
		
		
	}
}
