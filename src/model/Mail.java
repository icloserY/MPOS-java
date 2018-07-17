package model;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import freemarker.template.*;
import model.vo.*;

public class Mail {

	
	public static boolean sendMail(MailVo mailVo){
		/*String user = "jptj1217";
		String password = "tj08080416";
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.daum.net");
		props.put("mail.smtp.port", 465);
		props.put("mail.smtp.auth", "true");*/
		String user = "dksk73@naver.com";
		String password = "hhc3698";
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.naver.com");
		props.put("mail.smtp.port", 587);
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
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
			cfg.setDirectoryForTemplateLoading(new File("C:/Users/LG/workspace/TestJSP/WebContent/WEB-INF/view/mail"));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);  
			
			Template template = cfg.getTemplate("locked.ftl");
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
