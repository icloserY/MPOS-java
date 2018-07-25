package command.statistics;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;
import model.vo.*;

public class DonorsComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = null;
		@SuppressWarnings("unchecked")
		List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
				? new ArrayList<Map<String, String>>()
				: (ArrayList<Map<String, String>>) request.getAttribute("Errors");

		Map<String, String> popup = new HashMap<>();
		
		
		SettingsDao settingsDao = SettingsDao.getInstance();
		AccountsDao accountsDao = AccountsDao.getInstance();
		TransactionsDao transactionsDao = TransactionsDao.getInstance();
		
		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			ArrayList<DonationVo> donations = new ArrayList<>();
			switch(settingsDao.getValue(conn, "acl_donors_page")){
			case "0":
				if(accountsDao.isAuthenticated(conn, true, request)){
					donations = transactionsDao.getDonations(conn);
					request.setAttribute("DONORS", donations);
					request.setAttribute("currency", GlobalSettings.get("currency"));
					Content = "/WEB-INF/view/Content/statistics/donors/Donors.jsp";
				}
				break;
			case "1":
			case "":
				donations = transactionsDao.getDonations(conn);
				request.setAttribute("DONORS", donations);
				Content = "/WEB-INF/view/Content/statistics/donors/Donors.jsp";
				break;
			case "2":
				popup = Popup.getPopup("Page currently disabled. Please try again later.", "alert alert-warning", null, null);
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
