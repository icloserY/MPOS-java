package command.statistics;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;
import model.vo.pool.*;

public class PoolComHan implements ComHanInterFace{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = null;
		@SuppressWarnings("unchecked")
		List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
				? new ArrayList<Map<String, String>>()
				: (ArrayList<Map<String, String>>) request.getAttribute("Errors");

		Map<String, String> popup = new HashMap<>();
		
		StatisticsDao statisticsDao = StatisticsDao.getInstance();
		
		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			
			int dDifficulty = 0;
			int dNetworkHashrate = 0;
			int iBlock = 0;
			
			// bitcoin.can_connect() === true 보류
			if(false){
				
			}else{
				dDifficulty = 1;
				dNetworkHashrate = 1;
				iBlock = 0;
				
				popup = Popup.getPopup("Unable to connect to wallet RPC service: "  + " Exception Message",  "alert alert-danger", null, null);
				popups.add(popup);
			}
			
			ArrayList<TopContributorsVo> aContributorsShares = statisticsDao.getTopContributors(conn, "shares", 15);
			 
			ArrayList<TopContributorsVo> aContributorsHashes = statisticsDao.getTopContributors(conn, "hashes", 15);
			
			int iLimit = 10;
			ArrayList<BlocksFoundVo> aBlocksFoundData = statisticsDao.getBlocksFound(conn, iLimit);
			BlocksFoundVo aBlockData = aBlocksFoundData.size() > 0 ? aBlocksFoundData.get(0) : new BlocksFoundVo();
			
			double iCurrentPoolHashrate = statisticsDao.getCurrentHashrate(conn, 180);
			
			double iEstTime = iCurrentPoolHashrate > 0 ? statisticsDao.getExpectedTimePerBlock(conn, "pool", iCurrentPoolHashrate) : 0;
			
			
			
			
			
			Content = "/WEB-INF/view/Content/statistics/pool/Pool.jsp"; 
			
			
			
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			request.setAttribute("Errors", popups);
			CloseUtilities.close(conn);
		}
		return Content;
	}

}
