package command.auth;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;

public class ChangePasswordComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String Content = "/WEB-INF/view/Content/ChangePassword.jsp";
		request.setAttribute("token", token);
		// POST 방식으로 넘어왔을 때
		if (request.getMethod().equals("POST")) {
			String doParam = request.getParameter("do");
			String newPassword = request.getParameter("newPassword");
			String newPassword2 = request.getParameter("newPassword2");
			
			@SuppressWarnings("unchecked")
			List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
					? new ArrayList<Map<String, String>>() : (ArrayList<Map<String, String>>) request.getAttribute("Errors");

			Map<String, String> popup = new HashMap<>();
			
			AccountsDao accountsDao = AccountsDao.getInstance();
			Connection conn = null;
			if(doParam != null && doParam.equals("resetPassword")){
				try{ 
					conn = JDBCConnection.getConnection();  
					conn.setAutoCommit(false);
					if( accountsDao.resetPassword(conn, token, newPassword, newPassword2)){
						popup = Popup.getPopup("Password reset complete! Please login.", "alert alert-success", null, null);
						popups.add(popup);
						conn.commit();
					}else{
						popup = Popup.getPopup(accountsDao.getError(), "alert alert-danger", null, null);
						popups.add(popup);
						conn.rollback();
					}
				} catch(NamingException | SQLException e){
					e.printStackTrace();
					try {
						conn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} finally{
					request.setAttribute("Errors", popups); 
					CloseUtilities.close(conn);
				}
			}
			
		}
		return Content;
	}

}
