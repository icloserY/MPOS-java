package command.auth;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;

public class ResetComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = "/WEB-INF/view/Content/Reset.jsp";

		// POST 방식으로 넘어왔을 때
		if (request.getMethod().equals("POST")) {
			String username = request.getParameter("username");
			
			@SuppressWarnings("unchecked")
			List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
					? new ArrayList<Map<String, String>>() : (ArrayList<Map<String, String>>) request.getAttribute("Errors");

			Map<String, String> popup = new HashMap<>();
			
			AccountsDao accountsDao = AccountsDao.getInstance();
			Connection conn = null;
			try{
				conn = JDBCConnection.getConnection();
				if(accountsDao.initResetPassword(conn, username)){
					popup = Popup.getPopup("Please check your mail account to finish your password reset", "alert alert-success", null, null);
					popups.add(popup);
				}else {
					popup = Popup.getPopup(accountsDao.getError(), "alert alert-danger", null, null);
					popups.add(popup);
				}
			} catch(NamingException | SQLException e){
				e.printStackTrace();
			} finally{
				request.setAttribute("Errors", popups); 
				CloseUtilities.close(conn);
			}
		}
		return Content;
	}

}
