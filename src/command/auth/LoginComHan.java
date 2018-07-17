package command.auth;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;

public class LoginComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = request.getMethod().equals("GET") ? "/WEB-INF/view/Content/Login.jsp" 
				: request.getMethod().equals("POST") ? "DashBoard.do"
				: null;
		// POST 방식으로 넘어왔을 때
		if (request.getMethod().equals("POST")){
			String username = request.getParameter("username");
			String password = request.getParameter("password");
				
			@SuppressWarnings("unchecked")
			List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
					? new ArrayList<Map<String, String>>() : (ArrayList<Map<String, String>>) request.getAttribute("Errors");

			Map<String, String> popup = new HashMap<>();
			
			if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
				SettingsDao settingsDao = SettingsDao.getInstance();
				AccountsDao accountsDao = AccountsDao.getInstance();
				Connection conn = null;
				try { 
					conn = JDBCConnection.getConnection();
					conn.setAutoCommit(false);
					if (!settingsDao.getValue(conn, "maintenance").equals("") 
							&& !accountsDao.isAdmin(conn, accountsDao.getUserIdByEmail(conn, username))) {
						popup = Popup.getPopup("You are not allowed to login during maintenace.", "alert alert-info", null, null);
						popups.add(popup);
					} else { 
						// check if login is correct
						if (accountsDao.checkLogin(conn, username, password, request.getRemoteAddr(), request)) {
							// 로그인 성공 시 dashBoard.do로 보냄
							conn.commit();
							return Content;
						} else { 
							popup = Popup.getPopup("Unable to login: " + accountsDao.getError(), "alert alert-danger", null, null);
							popups.add(popup);
							conn.commit();
						} 
					}
				} catch (NamingException | SQLException e) {
					// 실패시 return login.do 
					e.printStackTrace();
					try {
						popup = Popup.getPopup("Unable to login: " + accountsDao.getError(), "alert alert-danger", null, null);
						popups.add(popup);
						conn.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} finally{
					CloseUtilities.close(conn);
				}
			}
			
			// 로그인 실패 Login.do로 다시 보냄
			request.setAttribute("Errors", popups);
			request.setAttribute("active", true);
			popup = Popup.getPopup("Unable to login: ", "alert alert-danger", null, null);
			popups.add(popup);
			return "/WEB-INF/view/Content/Login.jsp";
		}
		
		// get 방식일 경우 Login.jsp를 보냄
		return Content;
	}

}
