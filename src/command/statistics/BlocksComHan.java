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

public class BlocksComHan implements ComHanInterFace{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = null;
		@SuppressWarnings("unchecked")
		List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
				? new ArrayList<Map<String, String>>()
				: (ArrayList<Map<String, String>>) request.getAttribute("Errors");

		Map<String, String> popup = new HashMap<>();
		
		AccountsDao accountsDao = AccountsDao.getInstance();
		BlocksDao blocksDao = BlocksDao.getInstance();
		SettingsDao settingsDao = SettingsDao.getInstance();
		StatisticsDao statisticsDao = StatisticsDao.getInstance();
		RoundStats roundStats = RoundStats.getInstance();
		
		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			
			int iLimit = !settingsDao.getValue(conn, "statistics_block_count").equals("") ? Integer.parseInt(settingsDao.getValue(conn, "statistics_block_count"))
					 																	  : 20;
			String rqLimit = request.getParameter("limit");
			if(rqLimit != null && !rqLimit.trim().equals("") && is_numeric(rqLimit)){
				iLimit = Integer.parseInt(rqLimit);
				if(iLimit > 40){
					iLimit = 40;
				}
			}
			
			
			int iHeight = 0;
			BlockVo iBlock = null;
			String rqHeight = request.getParameter("height");
			if(request.getParameter("next") != null && request.getParameter("next").equals("1") && rqHeight != null && !rqHeight.trim().equals("")){
				iHeight = roundStats.getNextBlock(conn, Integer.parseInt(rqHeight));
				if(iHeight == 0){
					iBlock = blocksDao.getLast(conn);
					iHeight = iBlock != null ? iBlock.getHeight() : 0; 
				}
			} else if(request.getParameter("prev") != null && request.getParameter("prev").equals("1") && rqHeight != null && !rqHeight.trim().equals("")){
				iHeight = roundStats.getPreviousBlock(conn, Integer.parseInt(rqHeight)); 
			} else if(rqHeight == null || rqHeight.equals("")){
				iBlock = blocksDao.getLast(conn);
				iHeight = iBlock != null ? iBlock.getHeight() : 0; 
			} else{
				iHeight = Integer.parseInt(rqHeight);
			}
		
			boolean test = false;
			int count = 0;
			int percent = 30;
			AccountsVo userData = (AccountsVo) request.getSession().getAttribute("USERDATA");
			if(request.getParameter("test") != null && request.getParameter("test").equals("true") && userData != null && accountsDao.isAdmin(conn, userData.getId())){
				test = true;
				count = 10;
				percent = 30;
				if(request.getParameter("count") != null && is_numeric(request.getParameter("count"))){
					count =  Integer.parseInt(request.getParameter("count"));
				}
				if(request.getParameter("percent") != null && is_numeric(request.getParameter("percent"))){
					percent =  Integer.parseInt(request.getParameter("percent"));
				}
			}
			
			
			ArrayList<BlockVo> aBlocksFoundData = statisticsDao.getBlocksFoundHeight(conn, iHeight, iLimit);
			boolean use_average = false;
			if(GlobalSettings.get("payout_system").equals("pplns")){
				/*
				 * foreach($aBlocksFoundData as $key => $aData) {
      				$aBlocksFoundData[$key]['pplns_shares'] = $roundstats->getPPLNSRoundShares($aData['height']);
      				if ($setting->getValue('statistics_show_block_average') && !$test) {
        				$aBlocksFoundData[$key]['block_avg'] = round($block->getAvgBlockShares($aData['height'], $config['pplns']['blockavg']['blockcount']));
        				$use_average = true;
      			   	}
    			   }
				 *  변경 필요
				 * */
			}else if(GlobalSettings.get("payout_system").equals("prop") || GlobalSettings.get("payout_system").equals("pps")){
				if(!settingsDao.getValue(conn, "statistics_show_block_average").equals("") && !test){
					for(BlockVo block : aBlocksFoundData){
						block.setBlock_avg(Math.round(blocksDao.getAvgBlockShares(conn, block.getHeight(),
								Integer.parseInt(GlobalSettings.get("pplns.blockavg.blcokcount")))));
						use_average = true;
					}
				}
			}
			
			if(test){
				use_average = true;
				for(BlockVo block : aBlocksFoundData){
					if(request.getParameter("test") != null && request.getParameter("test").equals("1")){
						block.setBlock_avg(Math.round(blocksDao.getAvgBlockShares(conn, block.getHeight(),count)));
					} else if(request.getParameter("test") != null && request.getParameter("test").equals("2")){
						block.setBlock_avg(Math.round(blocksDao.getAvgBlockShares(conn, block.getHeight(),count) 
														* (100 - percent)/100 + block.getShares() * percent/100));
					}
				}
			}
			
			int iHours = 24;
			PoolStatsVo aPoolStatistics = statisticsDao.getPoolStatsHours(conn, iHours); 
			int iFirstBlockFound = statisticsDao.getFirstBlockFound(conn);
			long iTimeSinceFirstBlockFound = (System.currentTimeMillis() / 1000) - iFirstBlockFound;
			
			request.setAttribute("COINGENTIME", GlobalSettings.get("cointarget"));
			
			BlockTimeVo iFoundBlocksByTime = statisticsDao.getLastBlocksbyTime(conn);
			
			request.setAttribute("FIRSTBLOCKFOUND", iTimeSinceFirstBlockFound);
			request.setAttribute("LASTBLOCKSBYTIME", iFoundBlocksByTime);
			request.setAttribute("BLOCKSFOUND", aBlocksFoundData);
			request.setAttribute("BLOCKLIMIT", iLimit);
			request.setAttribute("USEBLOCKAVERAGE", use_average);
			request.setAttribute("POOLSTATS", aPoolStatistics);
			request.setAttribute("payout_system", GlobalSettings.get("payout_system"));
			request.setAttribute("confirmations", GlobalSettings.get("confirmations"));
			switch(settingsDao.getValue(conn, "acl_block_statistics")){
			case "0":
				if(accountsDao.isAuthenticated(conn, true, request)){
					Content = "/WEB-INF/view/Content/statistics/blocks/Blocks.jsp";
				}
				break;
			case "1":
			case "":
				Content = "/WEB-INF/view/Content/statistics/blocks/Blocks.jsp";
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

	public static boolean is_numeric(String str)
	{
	  return str.matches("^[0-9]*$");  
	}
	
}
