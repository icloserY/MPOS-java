package command.dashBoard;

import java.sql.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.dao.*;

public class DashBoardComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = "/WEB-INF/view/Content/DashBoard.jsp";
		AccountsDao accountsDao = AccountsDao.getInstance();
		SettingsDao settingsDao = SettingsDao.getInstance();
		StatisticsDao statisticsDao = StatisticsDao.getInstance();
		
		Connection conn = null;
		
		try{
			conn = JDBCConnection.getConnection();
			if(accountsDao.isAuthenticated(conn, true, request)){
				
				String resultInterval = settingsDao.getValue(conn, "statistics_ajax_data_interval");
				int interval = 0;
				
				if(resultInterval.equals("")){
					interval = 300;
				}else{
					interval = Integer.parseInt(resultInterval);
				}
				
				int dNetworkHashrate = 1;
				int dDifficulty = 1;
				int aRoundShares = 1;
				
			//	aRoundShares = statisticsDao.getRoundShares(conn);  
				
				int isBlock = 0;
				
				/*
				 *  보류 
				 *  
				 * */
				
				
			}else{
				Content = "Login.do";
			}
			
			
		} catch(NamingException | SQLException e){
			e.printStackTrace();
		} finally{
			CloseUtilities.close(conn);
		}
		 
		
		
		return Content;
	}

}
