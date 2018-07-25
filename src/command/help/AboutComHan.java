package command.help;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;

public class AboutComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {

		String Content = null;
		String action = request.getParameter("action");

		SettingsDao settingsDao = SettingsDao.getInstance();
		AccountsDao accountsDao = AccountsDao.getInstance();
		
		@SuppressWarnings("unchecked")
		List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
				? new ArrayList<Map<String, String>>() : (ArrayList<Map<String, String>>) request.getAttribute("Errors");

		Map<String, String> popup = new HashMap<>();
		
		Connection conn = null;
		
		
		switch (action) {
		
		case "pool":
			try{
				conn = JDBCConnection.getConnection();
				switch(settingsDao.getValue(conn, "acl_about_page")){
				case "0":
					if(accountsDao.isAuthenticated(conn, true, request)){
						Content = "/WEB-INF/view/Content/about/Pool.jsp";
					}
					break;
				case "1":
				case "":
					request.setAttribute("payout_system", GlobalSettings.get("payout_system"));
					Content = "/WEB-INF/view/Content/about/Pool.jsp";
					break;
				case "2":
					popup = Popup.getPopup("Page currently disabled. Please try again later.", "alert alert-danger", null, null);
					popups.add(popup);
					break;
				}
				
				
				
			}catch (NamingException | SQLException e) {
				e.printStackTrace();
			}finally{
				request.setAttribute("Errors", popups);
				CloseUtilities.close(conn);
			}
			break;
			
		case "api":
			break;
		case "chat":
			break;
		case "moot":
			break;
		case "pplns":
			break;

		}

		
		return Content;
	}

}
