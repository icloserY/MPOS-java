package command.auth;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.dao.*;

public class LoginComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		// POST 방식으로 넘어왔을 때
		if (request.getMethod().equals("POST")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			@SuppressWarnings("unchecked")
			Map<String, String> popup = (HashMap<String, String>) request.getAttribute("Errors") == null
					? new HashMap<String, String>() : (HashMap<String, String>) request.getAttribute("Errors");

			if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
				SettingsDao settingsDao = SettingsDao.getInstance();
				AccountsDao accountsDao = AccountsDao.getInstance();
				Connection conn = null;
				try { 
					conn = JDBCConnection.getConnection();
					if (!settingsDao.getValue(conn, "maintenance").equals("") 
							&& !accountsDao.isAdmin(conn, accountsDao.getUserIdByEmail(username))) {
						popup.put("You are not allowed to login during maintenace.", "alert alert-info");
						request.setAttribute("Errors", popup);
					} else { 
						// check if login is correct
						/*if (accountsDao.checkLogin(conn, username, password)) {

							// 로그인 성공 시 dashBoard.do로 보냄
							return null;
						} else {
							popup.put("Unable to login: " + accountsDao.getError(), "alert alert-danger");
						}*/
					}
				} catch (NamingException | SQLException e) {
					// 실패시 return login.do  
					
				} finally{
					CloseUtilities.close(conn);
				}
			}
		}

		// 로그인 실패 login.do로 다시 보냄
		return null;
	}

}
