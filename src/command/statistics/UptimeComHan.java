package command.statistics;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;

public class UptimeComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = null;
		@SuppressWarnings("unchecked")
		List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
				? new ArrayList<Map<String, String>>()
				: (ArrayList<Map<String, String>>) request.getAttribute("Errors");

		Map<String, String> popup = new HashMap<>();
		
		
		SettingsDao settingsDao = SettingsDao.getInstance();
		
		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			
			if(!settingsDao.getValue(conn, "monitoring_uptimerobot_api_keys").equals("")){
				/* 변경 필요
				 * aStatus = $monitoring->getUptimeRobotStatus()) {
				    $smarty->assign("STATUS", $aStatus);
				    $smarty->assign("UPDATED", $setting->getValue('monitoring_uptimerobot_lastcheck'));
				    $smarty->assign("CODES", array(
				      0 => 'Paused',
				      1 => 'Unchecked',
				      2 => 'Up',
				      8 => 'Down',
				      9 => 'Down'
				    ));*/
				Content = "/WEB-INF/view/Content/statistics/uptime/Uptime.jsp";
			}else{
				popup = Popup.getPopup("UptimeRobot API Key not configured or no data available.", "alert alert-warning", null, null);
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
