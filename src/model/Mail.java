package model;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import freemarker.template.*;
import model.dao.*;
import model.vo.*;

public class Mail extends Base{

	
	public static boolean sendMail(MailVo mailVo){
		
		String user = GlobalSettings.get("mail.user");
		String password = GlobalSettings.get("mail.password");
		
		Properties props = new Properties();
		props.put("mail.smtp.host", GlobalSettings.get("mail.smtp.host"));
		props.put("mail.smtp.port", GlobalSettings.get("mail.smtp.port"));
		props.put("mail.smtp.auth", GlobalSettings.get("mail.smtp.auth"));
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(user, password);
			}
		});
		
		try{
			MimeMessage message = new MimeMessage(session);
			message.setSubject(mailVo.getSubject());
			message.setFrom(new InternetAddress(user));
			
			if(mailVo.getContactform() == 1){  // contactform 메일 발송
				message.addRecipient(Message.RecipientType.TO, new InternetAddress( user ));
				message.setReplyTo(new Address[]{new InternetAddress(mailVo.getEmail())});
				
			}else{ // 일반 메일 발송
				message.addRecipient(Message.RecipientType.TO, new InternetAddress( mailVo.getEmail() ));
			}

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
	
	public boolean contactform(String senderName, String senderEmail, String senderSubject, String senderMessage){
		AccountsDao accountsDao = AccountsDao.getInstance();
		
		if(senderEmail.trim().equals("") || !accountsDao.checkEmail(senderEmail)){
			setErrorMessage(GlobalSettings.get("error.E0023"));
			return false;
		}
		
		if(senderMessage.replaceAll("\\<.*?\\>", "").length() < senderMessage.length()){
			setErrorMessage(GlobalSettings.get("error.E0024"));
			return false;
		}
		
		MailVo mailVo = new MailVo();
		mailVo.setContactform(1);
		mailVo.setEmail(senderEmail);
		mailVo.setUsername(senderName);
		mailVo.setSubject(senderSubject);
		mailVo.setContent(GlobalSettings.get("mail.ftl.contactform"));
		mailVo.setMessage(senderMessage);
		
		if(Mail.sendMail(mailVo)){
			return true;
		} else{
			setErrorMessage("Unable to send email");
			return false;
		}
	}
	
}
