package model.dao;

import java.sql.*;
import java.util.*;

import jdbc.*;
import model.*;
import model.vo.*;
import model.vo.blockfinder.*;
import model.vo.pool.*;

public class StatisticsDao extends Base {
	public StatisticsDao() {
		this.table = "statistics_shares";
		String table_user_stats = "statistics_users";
	}
	private static StatisticsDao statisticsDao = new StatisticsDao();
	public static StatisticsDao getInstance() {
		return statisticsDao;
	}
	public int getRoundShares(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int returnValue = 0;
		try{
			pstmt = conn.prepareStatement("SELECT IFNULL(SUM(IF(our_result='Y', IF(difficulty=0, POW(2, (" + GlobalSettings.get("difficulty") +"  - 16)), difficulty), 0)), 0) AS valid,"
					+ "IFNULL(SUM(IF(our_result='N', IF(difficulty=0, POW(2, (" + GlobalSettings.get("difficulty") + " - 16)), difficulty), 0)), 0) AS invalid"
							+ " FROM shares where UNIX_TIMESTAMP(time) > IFNULL((SELECT MAX(time) FROM blocks ), 0)" );
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				returnValue = rs.getInt(1);
			}
			
		}finally{
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		
		
		return returnValue;
		/*
		    $stmt = $this->mysqli->prepare("
		      SELECT
		        IFNULL(SUM(IF(our_result='Y', IF(difficulty=0, POW(2, (" . $this->config['difficulty'] . " - 16)), difficulty), 0)), 0) AS valid,
		        IFNULL(SUM(IF(our_result='N', IF(difficulty=0, POW(2, (" . $this->config['difficulty'] . " - 16)), difficulty), 0)), 0) AS invalid
		      FROM " . $this->share->getTableName() . "
		      WHERE UNIX_TIMESTAMP(time) > IFNULL((SELECT MAX(time) FROM " . $this->block->getTableName() . "), 0)");
		    if ( $this->checkStmt($stmt) && $stmt->execute() && $result = $stmt->get_result() )
		      return $this->memcache->setCache(STATISTICS_ROUND_SHARES, $result->fetch_assoc());
		    return $this->sqlError();*/
		   
	}
	
	
	public ArrayList<BlockVo> getBlocksFoundHeight(Connection conn, int iHeight, int limit) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BlockVo> blocks = new ArrayList<>();
		
		Coin_Base coin_base = new Coin_Base();
		
		try {
			// 0 자리 -> coin->getTargetBits();
			pstmt = conn.prepareStatement("select  b.*, a.username AS finder, a.is_anonymous AS is_anonymous, ROUND(difficulty * POW(2, 32 - "+ coin_base.getTargetBits() 
					+ " ), 4) AS estshares FROM blocks AS b LEFT JOIN accounts AS a ON b.account_id = a.id WHERE b.height <= ? ORDER BY height DESC LIMIT ?");
			pstmt.setInt(1, iHeight);
			pstmt.setInt(2, limit);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				BlockVo blockVo = new BlockVo();
				blockVo.setId(rs.getInt(1));
				blockVo.setHeight(rs.getInt(2));
				blockVo.setBlockhash(rs.getString(3));
				blockVo.setConfirmations(rs.getInt(4));
				blockVo.setAmount(rs.getDouble(5));
				blockVo.setDifficulty(rs.getDouble(6));
				blockVo.setTime(rs.getInt(7));
				blockVo.setAccounted(rs.getInt(8));
				blockVo.setAccount_id(rs.getInt(9));
				blockVo.setWorker_name(rs.getString(10));
				blockVo.setShares(rs.getDouble(11));
				blockVo.setShare_id(rs.getInt(12));
				
				blockVo.setFinder(rs.getString("finder"));
				blockVo.setIs_anonymous(rs.getInt("is_anonymous"));
				blockVo.setEstshares(rs.getDouble("estshares"));
				blocks.add(blockVo);
			}
			
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return blocks;
	}
	public PoolStatsVo getPoolStatsHours(Connection conn, int iHours) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PoolStatsVo poolStatsVo = null;

		Coin_Base coin_base = new Coin_Base();
		
		try {
			pstmt = conn.prepareStatement("SELECT IFNULL(COUNT(id), 0) as count, IFNULL(AVG(difficulty), 0) as average, IFNULL(SUM(shares), 0) as shares,"
					+ " IFNULL(SUM(amount), 0) as rewards FROM blocks WHERE FROM_UNIXTIME(time) > DATE_SUB(now(), INTERVAL ? HOUR) AND confirmations >= 1");
			pstmt.setInt(1, iHours);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				poolStatsVo = new PoolStatsVo();
				poolStatsVo.setCount(rs.getInt(1));
				poolStatsVo.setAverage(rs.getDouble(2));
				poolStatsVo.setShares(rs.getDouble(3));
				poolStatsVo.setRewards(rs.getDouble(4));
				poolStatsVo.setExpected(coin_base.calcEstaimtedShares(poolStatsVo.getAverage()));
				
			}
			
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return poolStatsVo;
	}
	public int getFirstBlockFound(Connection conn) throws SQLException {
		int returnValue = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("SELECT IFNULL(MIN(time), 0) AS time FROM blocks");
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				returnValue = rs.getInt(1);
				
			}
			
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return returnValue;
	}
	public BlockTimeVo getLastBlocksbyTime(Connection conn) throws SQLException {
		BlockTimeVo blocktimeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Coin_Base coin_base = new Coin_Base();
		
		try {
			pstmt = conn.prepareStatement("SELECT COUNT(id) AS Total, IFNULL(SUM(IF(confirmations > 0, 1, 0)), 0) AS TotalValid,"
					+ " IFNULL(SUM(IF(confirmations = -1, 1, 0)), 0) AS TotalOrphan,"
					+ " IFNULL(SUM(IF(confirmations > 0, difficulty, 0)), 0) AS TotalDifficulty,"
					+ " IFNULL(SUM(IF(confirmations > -1, shares, 0)), 0) AS TotalShares,"
					+ " IFNULL(SUM(IF(confirmations > -1, amount, 0)), 0) AS TotalAmount,"
					+ " IFNULL(SUM(IF(FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 3600 SECOND), 1, 0)), 0) AS 1HourTotal,"
					+ " IFNULL(SUM(IF(confirmations > 0 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 3600 SECOND), 1, 0)), 0) AS 1HourValid,"
					+ " IFNULL(SUM(IF(confirmations = -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 3600 SECOND), 1, 0)), 0) AS 1HourOrphan,"
					+ " IFNULL(SUM(IF(confirmations > 0 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 3600 SECOND), difficulty, 0 )), 0) AS 1HourDifficulty,"
					+ " IFNULL(SUM(IF(confirmations > -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 3600 SECOND), shares, 0)), 0 ) AS 1HourShares,"
					+ " IFNULL(SUM(IF(confirmations > -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 3600 SECOND), amount, 0)), 0 ) AS 1HourAmount,"
					+ " IFNULL(SUM(IF(FROM_UNIXTIME(time)    >= DATE_SUB(now(), INTERVAL 86400 SECOND), 1, 0)), 0) AS 24HourTotal,"
					+ " IFNULL(SUM(IF(confirmations > 0 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 86400 SECOND), 1, 0)), 0) AS 24HourValid,"
					+ " IFNULL(SUM(IF(confirmations = -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 86400 SECOND), 1, 0)), 0) AS 24HourOrphan,"
					+ " IFNULL(SUM(IF(confirmations > 0 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 86400 SECOND), difficulty, 0)), 0) AS 24HourDifficulty,"
					+ " IFNULL(SUM(IF(confirmations > -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 86400 SECOND), shares, 0)), 0) AS 24HourShares,"
					+ " IFNULL(SUM(IF(confirmations > -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 86400 SECOND), amount, 0)), 0) AS 24HourAmount,"
					+ " IFNULL(SUM(IF(FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 604800 SECOND), 1, 0)), 0) AS 7DaysTotal,"
					+ " IFNULL(SUM(IF(confirmations > 0 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 604800 SECOND), 1, 0)), 0) AS 7DaysValid,"
					+ " IFNULL(SUM(IF(confirmations = -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 604800 SECOND), 1, 0)), 0) AS 7DaysOrphan,"
					+ " IFNULL(SUM(IF(confirmations > 0 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 604800 SECOND), difficulty, 0 )), 0) AS 7DaysDifficulty,"
					+ " IFNULL(SUM(IF(confirmations > -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 604800 SECOND), shares, 0 )), 0) AS 7DaysShares,"
					+ " IFNULL(SUM(IF(confirmations > -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 604800 SECOND), amount, 0 )), 0) AS 7DaysAmount,"
					+ " IFNULL(SUM(IF(FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 2419200 SECOND), 1, 0)), 0) AS 4WeeksTotal,"
					+ " IFNULL(SUM(IF(confirmations > 0 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 2419200 SECOND), 1, 0)), 0) AS 4WeeksValid,"
					+ " IFNULL(SUM(IF(confirmations = -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 2419200 SECOND), 1, 0)), 0) AS 4WeeksOrphan,"
					+ " IFNULL(SUM(IF(confirmations > 0 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 2419200 SECOND), difficulty, 0 )), 0) AS 4WeeksDifficulty,"
					+ " IFNULL(SUM(IF(confirmations > -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 2419200 SECOND), shares, 0 )), 0) AS 4WeeksShares,"
					+ " IFNULL(SUM(IF(confirmations > -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 2419200 SECOND), amount, 0 )), 0) AS 4WeeksAmount,"
					+ " IFNULL(SUM(IF(FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 29030400 SECOND), 1, 0)), 0) AS 12MonthTotal,"
					+ " IFNULL(SUM(IF(confirmations > 0 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 29030400 SECOND), 1, 0)), 0) AS 12MonthValid,"
					+ " IFNULL(SUM(IF(confirmations = -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 29030400 SECOND), 1, 0)), 0 ) AS 12MonthOrphan,"
					+ " IFNULL(SUM(IF(confirmations > 0 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 29030400 SECOND), difficulty, 0)), 0) AS 12MonthDifficulty,"
					+ " IFNULL(SUM(IF(confirmations > -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 29030400 SECOND), shares, 0 )), 0) AS 12MonthShares,"
					+ " IFNULL(SUM(IF(confirmations > -1 AND FROM_UNIXTIME(time) >= DATE_SUB(now(), INTERVAL 29030400 SECOND), amount, 0 )), 0) AS 12MonthAmount"
					+ " FROM blocks");
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				blocktimeVo = new BlockTimeVo();
				blocktimeVo.setTotal(rs.getInt(1));
				blocktimeVo.setTotalValid(rs.getInt(2));
				blocktimeVo.setTotalOrphan(rs.getInt(3));
				blocktimeVo.setTotalDifficulty(rs.getDouble(4));
				blocktimeVo.setTotalShares(rs.getDouble(5));
				blocktimeVo.setTotalAmount(rs.getDouble(6));
				
				blocktimeVo.setoHourTotal(rs.getInt(7));
				blocktimeVo.setoHourValid(rs.getInt(8));
				blocktimeVo.setoHourOrphan(rs.getInt(9));
				blocktimeVo.setoHourDifficulty(rs.getDouble(10));
				blocktimeVo.setoHourShares(rs.getDouble(11));
				blocktimeVo.setoHourAmount(rs.getDouble(12));
				
				blocktimeVo.setTfHourTotal(rs.getInt(13));
				blocktimeVo.setTfHourValid(rs.getInt(14));
				blocktimeVo.setTfHourOrphan(rs.getInt(15));
				blocktimeVo.setTfHourDifficulty(rs.getDouble(16));
				blocktimeVo.setTfHourShares(rs.getDouble(17));
				blocktimeVo.setTfHourAmount(rs.getDouble(18));
				
				blocktimeVo.setsDaysTotal(rs.getInt(19));
				blocktimeVo.setsDaysValid(rs.getInt(20));
				blocktimeVo.setsDaysOrphan(rs.getInt(21));
				blocktimeVo.setsDaysDifficulty(rs.getDouble(22));
				blocktimeVo.setsDaysShares(rs.getDouble(23));
				blocktimeVo.setsDaysAmount(rs.getDouble(24));
				
				blocktimeVo.setfWeeksTotal(rs.getInt(25));
				blocktimeVo.setfWeeksValid(rs.getInt(26));
				blocktimeVo.setfWeeksOrphan(rs.getInt(27));
				blocktimeVo.setfWeeksDifficulty(rs.getDouble(28));
				blocktimeVo.setfWeeksShares(rs.getDouble(29));
				blocktimeVo.setfWeeksAmount(rs.getDouble(30));
				
				blocktimeVo.setOtMonthTotal(rs.getInt(31));
				blocktimeVo.setOtMonthValid(rs.getInt(32));
				blocktimeVo.setOtMonthOrphan(rs.getInt(33));
				blocktimeVo.setOtMonthDifficulty(rs.getDouble(34));
				blocktimeVo.setOtMonthShares(rs.getDouble(35));
				blocktimeVo.setOtMonthAmount(rs.getDouble(36));
				
				blocktimeVo.setTotalEstimatedShares(coin_base.calcEstaimtedShares(blocktimeVo.getTotalDifficulty()));
				blocktimeVo.setoHourEstimatedShares(coin_base.calcEstaimtedShares(blocktimeVo.getoHourDifficulty()));
				blocktimeVo.setTfHourEstimatedShares(coin_base.calcEstaimtedShares(blocktimeVo.getTfHourDifficulty()));
				blocktimeVo.setsDaysEstimatedShares(coin_base.calcEstaimtedShares(blocktimeVo.getsDaysDifficulty()));
				blocktimeVo.setfWeeksEstimatedShares(blocktimeVo.getfWeeksDifficulty());
				blocktimeVo.setOtMonthEstimatedShares(blocktimeVo.getOtMonthDifficulty());
				
			}
			
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return blocktimeVo;
	}
	public ArrayList<BlockSolvedAccountVo> getBlocksSolvedbyAccount(Connection conn, int limit) throws SQLException {
		ArrayList<BlockSolvedAccountVo> returnValue = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try {
			pstmt = conn.prepareStatement("SELECT b.*, a.username AS finder, COUNT(b.id) AS solvedblocks, SUM(b.amount) AS generatedcoins "
					+ " FROM blocks AS b LEFT JOIN accounts AS a ON b.account_id = a.id WHERE confirmations > 0 GROUP BY finder"
					+ " ORDER BY solvedblocks DESC LIMIT ?");
			pstmt.setInt(1, limit);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BlockSolvedAccountVo vo = new BlockSolvedAccountVo();
				vo.setId(rs.getInt(1));
				vo.setHeight(rs.getInt(2));
				vo.setBlockhash(rs.getString(3));
				vo.setConfirmations(rs.getInt(4));
				vo.setAmount(rs.getDouble(5));
				vo.setDifficulty(rs.getDouble(6));
				vo.setTime(rs.getInt(7));
				vo.setAccounted(rs.getInt(8));
				vo.setAccount_id(rs.getInt(9));
				vo.setWorker_name(rs.getString(10));
				vo.setShares(rs.getDouble(11));
				vo.setShare_id(rs.getInt(12));
				vo.setFinder(rs.getString(13));
				vo.setIs_anonymous(rs.getInt(14));
				vo.setSolvedblocks(rs.getInt(15));
				vo.setGeneratedcoins(rs.getDouble(16));
				
				returnValue.add(vo);
				
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		
		
		return returnValue;
	}
	public ArrayList<BlockSolvedWorkerVo> getBlocksSolvedbyWorker(Connection conn, int account_id, int limit) throws SQLException {
		ArrayList<BlockSolvedWorkerVo> returnValue = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try {
			pstmt = conn.prepareStatement("SELECT worker_name AS finder, COUNT(id) AS solvedblocks, SUM(amount) AS generatedcoins"
					+ " FROM blocks WHERE account_id = ? AND worker_name != 'unknown' GROUP BY finder ORDER BY solvedblocks DESC LIMIT ? ");
			
			pstmt.setInt(1, account_id);
			pstmt.setInt(2, limit);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BlockSolvedWorkerVo vo = new BlockSolvedWorkerVo();
				vo.setFinder(rs.getString(1));
				vo.setSolvedblocks(rs.getInt(2));
				vo.setGeneratedcoins(rs.getDouble(3));
				
				returnValue.add(vo);
				
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		
		
		return returnValue;
	}
	public ArrayList<TopContributorsVo> getTopContributors(Connection conn, String type, int limit) throws SQLException {
		ArrayList<TopContributorsVo> returnValue = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Coin_Base coin_base = new Coin_Base();
		try {
			switch(type){
			case "shares":
				pstmt = conn.prepareStatement("SELECT a.username AS account, a.donate_percent AS donate_percent, a.is_anonymous AS is_anonymous,"
						+ " IFNULL(SUM(IF(s.difficulty=0, POW(2, ("+GlobalSettings.get("difficulty")+" - 16)), s.difficulty)), 0) AS shares"
						+ " FROM shares AS s LEFT JOIN accounts AS a ON SUBSTRING_INDEX( s.username, '.', 1 ) = a.username WHERE our_result = 'Y'"
						+ " GROUP BY account ORDER BY shares DESC LIMIT ?");
				pstmt.setInt(1, limit);
				
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					TopContributorsVo vo = new TopContributorsVo();
					vo.setAccount(rs.getString(1));
					vo.setDonate_percent(rs.getFloat(2));
					vo.setIs_anonymous(rs.getInt(3));
					vo.setShares(rs.getDouble(4));
					
					returnValue.add(vo);
					
				}
				break;
			case "hashes":
				pstmt = conn.prepareStatement("SELECT a.username AS account, a.donate_percent AS donate_percent, a.is_anonymous AS is_anonymous,"
						+ " IFNULL(SUM(t1.difficulty), 0) AS shares FROM ( SELECT id, IFNULL(IF(difficulty=0, pow(2, ("+GlobalSettings.get("difficulty")
						+ " - 16)), difficulty), 0) AS difficulty, username FROM shares WHERE time > DATE_SUB(now(), INTERVAL 10 MINUTE) AND our_result = 'Y'"
						+ " UNION SELECT share_id, IFNULL(IF(difficulty=0, pow(2, ("+GlobalSettings.get("difficulty")+" - 16)), difficulty), 0) AS difficulty,"
						+ " username FROM shares_archive WHERE time > DATE_SUB(now(), INTERVAL 10 MINUTE) AND our_result = 'Y' ) AS t1 LEFT JOIN accounts AS a"
						+ " ON SUBSTRING_INDEX( t1.username, '.', 1 ) = a.username GROUP BY account ORDER BY shares DESC LIMIT ?");
				
				pstmt.setInt(1, limit);
				
				rs = pstmt.executeQuery();
				int count = 0;
				while (rs.next()) {
					TopContributorsVo vo = new TopContributorsVo();
					vo.setAccount(rs.getString(1));
					vo.setDonate_percent(rs.getFloat(2));
					vo.setIs_anonymous(rs.getInt(3));
					vo.setShares(rs.getDouble(4));
					vo.setCount(count);
					vo.setHashrate(coin_base.calcHashrate(vo.getShares(), 600));
					
					returnValue.add(vo);
					count++;
					
				}
				break;
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		
		
		return returnValue;
	}
	
	public ArrayList<BlocksFoundVo> getBlocksFound(Connection conn, int limit) throws SQLException {
		ArrayList<BlocksFoundVo> returnValue = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Coin_Base coin_base = new Coin_Base();
		
		try {
			pstmt = conn.prepareStatement("SELECT b.*, a.username AS finder, a.is_anonymous AS is_anonymous, ROUND(difficulty * POW(2, 32 - "+coin_base.getTargetBits()
					+ " ), "+coin_base.getShareDifficultyPrecision()+" ) AS estshares FROM blocks AS b LEFT JOIN accounts AS a ON b.account_id = a.id"
					+ " ORDER BY height DESC LIMIT ?");
			
			pstmt.setInt(1, limit);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BlocksFoundVo vo = new BlocksFoundVo();
				vo.setId(rs.getInt(1));
				vo.setHeight(rs.getInt(2));
				vo.setBlockhash(rs.getString(3));
				vo.setConfirmations(rs.getInt(4));
				vo.setAmount(rs.getDouble(5));
				vo.setDifficulty(rs.getDouble(6));
				vo.setTime(rs.getInt(7));
				vo.setAccounted(rs.getInt(8));
				vo.setAccount_id(rs.getInt(9));
				vo.setWorker_name(rs.getString(10));
				vo.setShares(rs.getDouble(11));
				vo.setShare_id(rs.getInt(12));
				vo.setFinder(rs.getString(13));
				vo.setIs_anonymous(rs.getInt(14));
				vo.setEstshares(rs.getDouble(15));
				
				returnValue.add(vo);
				
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		
		
		return returnValue;
	}
	public double getCurrentHashrate(Connection conn, int interval) throws SQLException {
		double returnValue = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Coin_Base coin_base = new Coin_Base();
		
		try {
			pstmt = conn.prepareStatement("SELECT ( ( SELECT IFNULL(SUM(IF(difficulty=0, POW(2, ( "+GlobalSettings.get("difficulty")+" - 16)), difficulty)), 0) AS"
					+ " shares FROM shares WHERE time > DATE_SUB(now(), INTERVAL ? SECOND) AND our_result = 'Y' ) + ( SELECT IFNULL(SUM(IF(difficulty=0,"
					+ " POW(2, ( "+GlobalSettings.get("difficulty")+" - 16)), difficulty)), 0) AS shares FROM shares_archive WHERE time > DATE_SUB(now(), INTERVAL ? SECOND)"
					+ " AND our_result = 'Y' ) ) AS shares FROM DUAL");
			
			pstmt.setInt(1, interval);
			pstmt.setInt(2, interval);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				returnValue = coin_base.calcHashrate(rs.getDouble(1), interval);
				
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		
		
		return returnValue;
	}
	
	public double getExpectedTimePerBlock(Connection conn, String type, double hashrate) {
		double dDifficulty = 0;
		Coin_Base coin_base = new Coin_Base();
		// bitcoin_can_connect() 처리
		if(false){
			/*
			 * 변경 필요
			 * if ($type == 'network') {
		        $hashrate = $this->bitcoin->getnetworkhashps();
		      } else {
		        // We need hashes/second and expect khash as input
		        $hashrate = $hashrate * 1000;
		      }
		      $dDifficulty = $this->bitcoin->getdifficulty();*/
		} else {
			hashrate = 1;
			dDifficulty = 1;
		}
		if(hashrate <= 0){
			hashrate = 1;
		}
		return coin_base.calcNetworkExpectedTimePerBlock(dDifficulty,hashrate);
	}
	
	
	
}
 