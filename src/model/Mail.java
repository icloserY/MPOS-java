package model;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import freemarker.template.*;
import model.vo.*;

public class Mail {

	
	public static boolean sendMail(MailVo mailVo){
		
		String user = GlobalSettings.get("mail.user");
		String password = GlobalSettings.get("mail.password");
		
		Properties props = new Properties();
		props.put("mail.smtp.host", GlobalSettings.get("mail.smtp.host"));
		props.put("mail.smtp.port", GlobalSettings.get("mail.smtp.host"));
		props.put("mail.smtp.auth", GlobalSettings.get("mail.smtp.auth"));
		
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
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
			cfg.setDirectoryForTemplateLoading(new File(GlobalSettings.get("mail.contextpath")));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);  
			
			Template template = cfg.getTemplate(mailVo.getContent());
			Writer writer = new StringWriter();
			template.process(mailVo, writer);
			message.setContent(writer.toString(), "text/html; charset=utf-8");
			Transport.send(message);
			
			return true;
		}catch(MessagingException | IOException | TemplateException e){
			e.printStackTrace();
			return false;
		}
		
		
	}
}
