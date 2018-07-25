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
import model.vo.round.*;

public class RoundComHan implements ComHanInterFace {

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
		BlocksDao blocksDao = BlocksDao.getInstance();
		RoundStats roundStats = RoundStats.getInstance();
		
		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			String search = request.getParameter("search");
			String rqHeight = request.getParameter("height");
			
			if(search != null){
				rqHeight = String.valueOf(roundStats.searchForBlockHeight(conn, search));
			}
			
			int iHeight = 0;
			BlockVo iBlock = null;
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
			
			//int iPPLNSShares = 0;
			
			DetailsBlockVo aDetailsForBlockHeight = roundStats.getDetailsForBlockHeight(conn, iHeight);
			Map<Integer, RoundAccountsVo> aRoundShareStats = roundStats.getRoundStatsForAccounts(conn, iHeight);
			ArrayList<RoundTransactionsVo> aUserRoundTransactions = roundStats.getAllRoundTransactions(conn, iHeight);
			
			if(GlobalSettings.get("payout_system").equals("pplns")){
				/*
				 * 변경 필요 
				 * $aPPLNSRoundShares = $roundstats->getPPLNSRoundStatsForAccounts($iHeight);
			    foreach($aPPLNSRoundShares as $aData) {
			      $iPPLNSShares += $aData['pplns_valid'];
			    }
			    $block_avg = $block->getAvgBlockShares($iHeight, $config['pplns']['blockavg']['blockcount']);
			    $smarty->assign('PPLNSROUNDSHARES', $aPPLNSRoundShares);
			    $smarty->assign("PPLNSSHARES", $iPPLNSShares);
			    $smarty->assign("BLOCKAVGCOUNT", $config['pplns']['blockavg']['blockcount']);
			    $smarty->assign("BLOCKAVERAGE", $block_avg );*/
			}
			
			request.setAttribute("BLOCKDETAILS", aDetailsForBlockHeight);
			request.setAttribute("ROUNDSHARES", aRoundShareStats);
			request.setAttribute("ROUNDTRANSACTIONS", aUserRoundTransactions);
			request.setAttribute("confirmations", GlobalSettings.get("confirmations"));
			
			
			switch(settingsDao.getValue(conn, "acl_round_statistics")){
			case "0":
				if(accountsDao.isAuthenticated(conn, true, request)){
					Content = "/WEB-INF/view/Content/statistics/round/Round.jsp"; 
				}
				break;
			case "1":
			case "":
				Content = "/WEB-INF/view/Content/statistics/round/Round.jsp"; 
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
