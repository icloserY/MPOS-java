package command.account;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;

public class QRcodesComHan implements ComHanInterFace{

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
			switch(settingsDao.getValue(conn, "acl_qrcode")){
			case "0":
				if(accountsDao.isAuthenticated(conn, true, request)){
					Content = "/WEB-INF/view/Content/account/Myworkers.jsp";
				}
				break;
			case "1":
				popup = Popup.getPopup("Page currently disabled. Please try again later.", "alert alert-danger", null, null);
				popups.add(popup);
				break;
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
