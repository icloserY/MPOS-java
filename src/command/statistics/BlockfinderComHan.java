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
import model.vo.blockfinder.*;

public class BlockfinderComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = null;
		@SuppressWarnings("unchecked")
		List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
				? new ArrayList<Map<String, String>>()
				: (ArrayList<Map<String, String>>) request.getAttribute("Errors");

		Map<String, String> popup = new HashMap<>();
		StatisticsDao statisticsDao = StatisticsDao.getInstance();
		AccountsDao accountsDao = AccountsDao.getInstance();
		SettingsDao settingsDao = SettingsDao.getInstance();
		
		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			
			ArrayList<BlockSolvedAccountVo> getBlocksSolvedbyAccount = statisticsDao.getBlocksSolvedbyAccount(conn, 25); 
			request.setAttribute("BLOCKSSOLVEDBYACCOUNT", getBlocksSolvedbyAccount);
			
			AccountsVo userData = (AccountsVo) request.getSession().getAttribute("USERDATA");
			if(userData != null){
				ArrayList<BlockSolvedWorkerVo> getBlocksSolvedbyWorker = statisticsDao.getBlocksSolvedbyWorker(conn, userData.getId(), 25);
				request.setAttribute("BLOCKSSOLVEDBYWORKER", getBlocksSolvedbyWorker);
			}
			
			switch(settingsDao.getValue(conn, "acl_blockfinder_statistics")){
			case "0":
				if(accountsDao.isAuthenticated(conn, true, request)){
					Content = "/WEB-INF/view/Content/statistics/blockfinder/Blockfinder.jsp"; 
				}
				break;
			case "1":
			case "":
				Content = "/WEB-INF/view/Content/statistics/blockfinder/Blockfinder.jsp"; 
				break;
			case "2":
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
