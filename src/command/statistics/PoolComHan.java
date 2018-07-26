package command.statistics;

import java.sql.*;
import java.text.*;
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
		
		AccountsDao accountsDao = AccountsDao.getInstance();
		SettingsDao settingsDao = SettingsDao.getInstance();
		StatisticsDao statisticsDao = StatisticsDao.getInstance();
		BlocksDao blocksDao = BlocksDao.getInstance();
		
		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			
			int dDifficulty = 0;
			int dNetworkHashrate = 0;
			int iBlock = 0;
			
			String sBlockHash = null;
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
			
			long dTimeSinceLast = 0; 
			if(aBlockData.getFinder() != null){
				dTimeSinceLast = (System.currentTimeMillis() - aBlockData.getTime());
			}
			
			double reward = GlobalSettings.get("reward_type").equals("fixed") ? Double.parseDouble(GlobalSettings.get("reward")) : blocksDao.getAverageAmount(conn, 10);
			
			double iEstShares = statisticsDao.getEstimatedShares(dDifficulty); 
			
			RoundSharesVo aRoundShares = statisticsDao.getRoundShares(conn);
			
			double dEstPercent = 0;
			if(iEstShares > 0 && aRoundShares != null && aRoundShares.getValid() > 0){
				DecimalFormat formatter = new DecimalFormat("#0.00");
				dEstPercent = Double.parseDouble(formatter.format(Math.round(100 / iEstShares * aRoundShares.getValid())));
			}
			
			double dExpectedTimePerBlock = statisticsDao.getExpectedTimePerBlock(conn, "network", 0);
			double dEstNextDifficulty = statisticsDao.getExpectedNextDifficulty();
			double iBlocksUntilDiffChange = statisticsDao.getBlocksUntilDiffChange();
			
			NetworkVo networkVo = new NetworkVo();
			networkVo.setDifficulty(dDifficulty);
			networkVo.setBlock(iBlock);
			networkVo.setEstNextDifficulty(dEstNextDifficulty);
			networkVo.setEstTimePerBlock(dExpectedTimePerBlock);
			networkVo.setBlocksUntilDiffChange(iBlocksUntilDiffChange);
			
			Map<String, Double> estimatesMap = new HashMap<>();
			estimatesMap.put("shares", iEstShares);
			estimatesMap.put("percent", dEstPercent);
			
			request.setAttribute("ESTTIME", iEstTime);
			request.setAttribute("TIMESINCELAST", dTimeSinceLast);
			request.setAttribute("BLOCKSFOUND", aBlocksFoundData);
			request.setAttribute("BLOCKLIMIT", iLimit);
			request.setAttribute("CONTRIBSHARES", aContributorsShares);
			request.setAttribute("CONTRIBHASHES", aContributorsHashes);
			request.setAttribute("CURRENTBLOCK", iBlock);
			request.setAttribute("CURRENTBLOCKHASH", sBlockHash);
			request.setAttribute("NETWORK", networkVo);
			request.setAttribute("ESTIMATES", estimatesMap);
			
			if(aBlockData.getFinder() != null){
				request.setAttribute("LASTBLOCK", aBlockData.getHeight());
				request.setAttribute("LASTBLOCKHASH", aBlockData.getBlockhash());
			} else {
				request.setAttribute("LASTBLOCK", 0);
			}
			request.setAttribute("DIFFICULTY", dDifficulty);
			request.setAttribute("REWARD", reward);
			request.setAttribute("payout_system", GlobalSettings.get("payout_system"));
			request.setAttribute("confirmations", GlobalSettings.get("confirmations"));
			request.setAttribute("currency", GlobalSettings.get("currency"));
			
			switch(settingsDao.getValue(conn, "acl_pool_statistics")){
			case "0":
				if(accountsDao.isAuthenticated(conn, true, request)){
					Content = "/WEB-INF/view/Content/statistics/pool/Pool.jsp";  
				}
				break;
			case "1":
			case "":
				Content = "/WEB-INF/view/Content/statistics/pool/Pool.jsp";  
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

