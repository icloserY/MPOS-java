package command.other;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;

public class ContactformComHan implements ComHanInterFace{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = null;
		

		SettingsDao settingsDao = SettingsDao.getInstance();
		AccountsDao accountsDao = AccountsDao.getInstance();
		
		@SuppressWarnings("unchecked")
		List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
				? new ArrayList<Map<String, String>>() : (ArrayList<Map<String, String>>) request.getAttribute("Errors");

		Map<String, String> popup = new HashMap<>();
		
		Connection conn = null;
		
		try{
			conn = JDBCConnection.getConnection();
			// get 방식으로 온 Contactform.do
			if(request.getMethod().equals("GET")){
				if(settingsDao.getValue(conn, "acl_contactform").equals("2")){
					popup = Popup.getPopup("Contactform is currently disabled. Please try again later.", "alert alert-danger", null, null);
					popups.add(popup);
					Content = null;
				} else if(settingsDao.getValue(conn, "acl_contactform").equals("") && !accountsDao.isAuthenticated(conn, false, request)){
					popup = Popup.getPopup("Contactform is disabled for guests.", "alert alert-danger", null, null);
					popups.add(popup);
					Content = "/WEB-INF/view/Content/contactform/contactform_disabled.jsp";
				} else{
					Content = "/WEB-INF/view/Content/contactform/contactform.jsp";
				}
			}
			
			// post 방식으로 온 Contactform.do
			if(request.getMethod().equals("POST")){
				String senderName = request.getParameter("senderName");
				String senderEmail = request.getParameter("senderEmail");
				String senderSubject = request.getParameter("senderSubject");
				String senderMessage = request.getParameter("senderMessage");
				
				if(settingsDao.getValue(conn, "acl_contactform").equals("2")){
					popup = Popup.getPopup("Contactform is currently disabled. Please try again later.", "alert alert-danger", null, null);
					popups.add(popup);
				} else if(settingsDao.getValue(conn, "acl_contactform").equals("0") && !accountsDao.isAuthenticated(conn, false, request)){
					popup = Popup.getPopup("Contactform is disabled for guests.", "alert alert-danger", null, null);
					popups.add(popup);
				} else{
					Mail mail = new Mail();
					
					if(mail.contactform(senderName, senderEmail, senderSubject, senderMessage)){
						popup = Popup.getPopup("Thanks for sending your message! We will get back to you shortly", "alert alert-info", null, null);
						popups.add(popup); 
					}else{
						popup = Popup.getPopup("There was a problem sending your message. Check following error and please try again: " + mail.getError(), "alert alert-danger", null, null);
						popups.add(popup);
					}
				}
				
				Content = "/WEB-INF/view/Content/contactform/contactform.jsp";
			}
			
		}catch (NamingException | SQLException e) {
			e.printStackTrace();
		}finally{
			request.setAttribute("Errors", popups);
			CloseUtilities.close(conn);
		}
		
		
		return Content;
	}

}
