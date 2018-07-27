package command.dashBoard;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.TosCoindApi;
import model.dao.*;
import model.vo.AccountsVo;
import model.vo.pool.RoundSharesVo;

public class DashBoardComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = "/WEB-INF/view/Content/DashBoard.jsp";
		AccountsDao accountsDao = AccountsDao.getInstance();
		SettingsDao settingsDao = SettingsDao.getInstance();
		StatisticsDao statisticsDao = StatisticsDao.getInstance();

		Connection conn = null;
		if (request.getSession().getAttribute("USERDATA") != null) {
			try {
				conn = JDBCConnection.getConnection();
				if (accountsDao.isAuthenticated(conn, true, request)) {

					String resultInterval = settingsDao.getValue(conn, "statistics_ajax_data_interval");
					int interval = 0;

					if (resultInterval.equals("")) {
						interval = 300;
					} else {
						interval = Integer.parseInt(resultInterval);
					}
					int dNetworkHashrate = 1;
					int dDifficulty = 1;
					int aRoundShares = 1;

					// aRoundShares = statisticsDao.getRoundShares(conn);

					int isBlock = 0;

					/*
					 * 보류
					 * 
					 */

				} else {
					Content = "Login.do";
				}
				Double iCurrentPoolHashrate = 0.0;
				Double dPoolHashrateModifier = 0.0;
				Double dPersonalHashrateModifier = 0.0;
				Map<String, Object> aUserMiningStats = null;
				AccountsVo account = (AccountsVo) request.getSession().getAttribute("USERDATA");
				int workers = 0;
				Double dNetworkHashrate = 0.0;
				Double dNetworkHashrateModifier = 0.0;
				int blockCount = 0;
				Double difficulty = 0.0;
				Double dExpectedTimePerBlock = 0.0;
				Double dEstNextDifficulty = 0.0;
				Double iBlocksUntilDiffChange = 0.0;
				Double iEstShares = 0.0;
				Double dEstPercent = 0.0;
				RoundSharesVo aRoundShares = null;
				try {
					aRoundShares = StatisticsDao.getInstance().getRoundShares(conn);
					String dNetworkHashrateValue = TosCoindApi.getInstance().getnetworkhashps(null);
					dNetworkHashrate = Double.parseDouble(dNetworkHashrateValue.isEmpty() ? "0.0": dNetworkHashrateValue);
					iCurrentPoolHashrate = StatisticsDao.getInstance().getCurrentHashrate(conn, 180);
					if(iCurrentPoolHashrate > dNetworkHashrate / 1000) dNetworkHashrate = iCurrentPoolHashrate * 1000;
					String dPoolHashrateModifierValue = SettingsDao.getInstance().getValue(conn, "statistics_pool_hashrate_modifier");
					dPoolHashrateModifier = Double
							.parseDouble(dPoolHashrateModifierValue.isEmpty() ? "1.0" : dPoolHashrateModifierValue);
					String dNetworkHashrateModifierValue = SettingsDao.getInstance().getValue(conn, "statistics_network_hashrate_modifier");
					dNetworkHashrate = Double.parseDouble(dNetworkHashrateModifierValue.isEmpty() ? "1.0" : dNetworkHashrateModifierValue);
					dNetworkHashrate = dNetworkHashrate / 1000 * dNetworkHashrateModifier;
					String dPersonalHashrateModifierValue = SettingsDao.getInstance().getValue(conn, "statistics_personal_hashrate_modifier");
					dPersonalHashrateModifier = Double.parseDouble(dPersonalHashrateModifierValue.isEmpty() ? "0.0" : dPersonalHashrateModifierValue);
					aUserMiningStats = StatisticsDao.getInstance().getUserMiningStats(conn, account.getUsername(),
							account.getId(), -1);
					workers = Pool_workerDao.getInstance().getCountAllActiveWorkers(conn, -1);
					blockCount = TosCoindApi.getInstance().getBlockCount();
					difficulty = TosCoindApi.getInstance().getDifficulty();
					dExpectedTimePerBlock = StatisticsDao.getInstance().getExpectedTimePerBlock(conn, "pool", iCurrentPoolHashrate);
					dEstNextDifficulty = StatisticsDao.getInstance().getExpectedNextDifficulty();
					iBlocksUntilDiffChange = StatisticsDao.getInstance().getBlocksUntilDiffChange();
					iEstShares = StatisticsDao.getInstance().getEstimatedShares(difficulty);
					if (iEstShares > 0 && aRoundShares != null && aRoundShares.getValid() > 0) {
					    dEstPercent = (double)Math.round(100 / iEstShares * aRoundShares.getValid());
					  } else {
					    dEstPercent = 0.0;
					  }
				} catch (Exception e) {
					e.printStackTrace();
				}
				iCurrentPoolHashrate *= dPoolHashrateModifier;
				request.getSession().getServletContext().setAttribute("hashrate", iCurrentPoolHashrate);
				request.getSession().getServletContext().setAttribute("hashmod", dPersonalHashrateModifier);
				request.setAttribute("hashrate", iCurrentPoolHashrate);
				request.setAttribute("user_rawhashrate", aUserMiningStats.get("hashrate"));
				request.setAttribute("user_hashrate",
						(Double) request.getAttribute("user_rawhashrate") * dPersonalHashrateModifier);
				request.setAttribute("user_sharerate", aUserMiningStats.get("sharerate"));
				request.setAttribute("user_sharedifficulty", aUserMiningStats.get("avgsharediff"));
				request.getSession().getServletContext().setAttribute("workers", workers);
				request.setAttribute("workers", workers);
				request.getSession().setAttribute("nethashrate", dNetworkHashrate);
				Map<String, Object> NETWORK = new HashMap<>();
				NETWORK.put("difficulty", difficulty);
				NETWORK.put("block", blockCount);
				NETWORK.put("EstNextDifficulty", dEstNextDifficulty);
				NETWORK.put("EstTimePerBlock", dExpectedTimePerBlock);
				NETWORK.put("BlocksUntilDiffChange", iBlocksUntilDiffChange);
				request.setAttribute("NETWORK", NETWORK);
				Map<String, Object> ESTIMATES = new HashMap<>();
				ESTIMATES.put("shares", iEstShares);
				ESTIMATES.put("percent", dEstPercent);
				request.setAttribute("ESTIMATES", ESTIMATES);
				//StatisticsDao.getInstance().getUser
				System.out.println(request.getAttribute("user_hashrate"));
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			} finally {
				CloseUtilities.close(conn);
			}

		}

		return Content;
	}

}
