package command.account;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;
import model.vo.notifications.*;

public class NotificationsComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = null;
		@SuppressWarnings("unchecked")
		List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
				? new ArrayList<Map<String, String>>()
				: (ArrayList<Map<String, String>>) request.getAttribute("Errors");

		Map<String, String> popup = new HashMap<>();

		AccountsDao accountsDao = AccountsDao.getInstance();
		SettingsDao settingsDao = SettingsDao.getInstance();
		
		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			
			if(accountsDao.isAuthenticated(conn, true, request)){
				if(settingsDao.getValue(conn, "disable_notifications").equals("1")){
					popup = Popup.getPopup("Notification system disabled by admin.", "alert alert-warning", null, null);
					popups.add(popup);
				} else {
					if(request.getParameter("do") != null && request.getParameter("do").equals("save")){
						PushSettingsVo pushSettings = new PushSettingsVo();
						pushSettings.setVo_class(request.getParameter("pushnotification-class"));
						
						//if(pushSettings.getVo_class() != null && )
						
						
						
					}
					//$aNotifications = $notification->getNotifications($_SESSION['USERDATA']['id']);
					Content = "/WEB-INF/view/Content/account/Invitations.jsp";
				}
			} else {
				Content = "Login.do";
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			request.setAttribute("Errors", popups);
			CloseUtilities.close(conn);
		}
		return Content;
	}

}
