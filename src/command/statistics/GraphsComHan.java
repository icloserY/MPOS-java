package command.statistics;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;

public class GraphsComHan implements ComHanInterFace {

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
		
		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			/*
			 * 변경 필요
			if ($user->isAuthenticated()) {
			    $aHourlyMiningStats = $statistics->getHourlyMiningStatsByAccount($_SESSION['USERDATA']['id'], 'json', 
			    																 $setting->getValue('statistics_graphing_days', 1));
			  }
			  $smarty->assign('YOURMININGSTATS', @$aHourlyMiningStats);*/
			
			
			
			
			switch(settingsDao.getValue(conn, "acl_graphs_statistics")){
			case "0":
			case "":
				if(accountsDao.isAuthenticated(conn, true, request)){
					Content = "/WEB-INF/view/Content/statistics/graphs/Graphs.jsp";
				}else{
					Content = "Login.do";
				}
				break;
			case "1":
				Content = "/WEB-INF/view/Content/statistics/graphs/Graphs.jsp";
				break;
			case "2":
				popup = Popup.getPopup("Page currently disabled. Please try again later.", "alert alert-danger", null, null);
				popups.add(popup);
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


