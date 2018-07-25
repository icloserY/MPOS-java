package model.dao;

import java.sql.*;
import java.util.*;

import jdbc.*;
import model.vo.round.*;

public class RoundStats extends Base {
	
	private static RoundStats accountsDao = new RoundStats();

	public static RoundStats getInstance() {
		return accountsDao;
	}
	
	public int getNextBlock(Connection conn, int iHeight) throws SQLException {
		int returnValue = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement("select height from blocks where height > ? order by height asc limit 1");
			pstmt.setInt(1, iHeight);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				returnValue = rs.getInt(1);
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return returnValue;
	}

	public int getPreviousBlock(Connection conn, int iHeight) throws SQLException {
		int returnValue = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement("select height from blocks where height < ? order by height desc limit 1");
			pstmt.setInt(1, iHeight);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				returnValue = rs.getInt(1);
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return returnValue;
	}

	public int searchForBlockHeight(Connection conn, String search) throws SQLException {
		int returnValue = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int iHeight = 0;
		try{
			iHeight = Integer.parseInt(search);
		}catch(NumberFormatException e){
			return 0;
		}
		try {
			pstmt = conn.prepareStatement("select height from blocks where height >= ? order by height asc limit 1");
			pstmt.setInt(1, iHeight);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				returnValue = rs.getInt(1);
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return returnValue; 
	}

	public DetailsBlockVo getDetailsForBlockHeight(Connection conn, int iHeight) throws SQLException {
		
		DetailsBlockVo vo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Coin_Base coin_base = new Coin_Base(); 
		
		try {
			pstmt = conn.prepareStatement("SELECT b.id, height, blockhash, amount, confirmations, difficulty, FROM_UNIXTIME(time) as time, shares,"
					+ " IF(a.is_anonymous, 'anonymous', a.username) AS finder, ROUND(difficulty * POW(2, 32 - "+coin_base.getTargetBits()+" ), 0) AS estshares,"
					+ " (time - (SELECT time FROM blocks WHERE height < ? ORDER BY height DESC LIMIT 1)) AS round_time FROM blocks as b LEFT JOIN accounts  AS a ON"
					+ " b.account_id = a.id WHERE b.height = ? LIMIT 1");
			pstmt.setInt(1, iHeight);
			pstmt.setInt(2, iHeight);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				vo = new DetailsBlockVo();
				
				vo.setId(rs.getInt(1));
				vo.setHeight(rs.getInt(2));
				vo.setBlockhash(rs.getString(3));
				vo.setAmount(rs.getDouble(4));
				vo.setConfirmations(rs.getInt(5));
				vo.setDifficulty(rs.getDouble(6));
				vo.setTime(rs.getLong(7));
				vo.setShares(rs.getDouble(8));
				vo.setFinder(rs.getString(9));
				vo.setEstshares(rs.getDouble(10));
				vo.setRound_time(rs.getDouble(11));
				
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		
		
		return vo;
	}

	public Map<Integer, RoundAccountsVo> getRoundStatsForAccounts(Connection conn, int iHeight) throws SQLException {
		Map<Integer, RoundAccountsVo> returnValue = new HashMap<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			
		try {
			pstmt = conn.prepareStatement("SELECT a.id, a.username, a.is_anonymous, s.valid, s.invalid FROM statistics_shares AS s"
					+ " LEFT JOIN blocks AS b ON s.block_id = b.id LEFT JOIN accounts AS a ON a.id = s.account_id WHERE b.height = ? AND s.valid > 0"
					+ " GROUP BY username ASC ORDER BY valid DESC ");
			pstmt.setInt(1, iHeight);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				RoundAccountsVo vo = new RoundAccountsVo();
				vo.setId(rs.getInt(1));
				vo.setUsername(rs.getString(2));
				vo.setIs_anonymous(rs.getInt(3));
				vo.setValid(rs.getFloat(4));
				vo.setInvalid(rs.getFloat(5));
				
				returnValue.put(vo.getId(), vo);
				
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		
		
		return returnValue;
	}

	public ArrayList<RoundTransactionsVo> getAllRoundTransactions(Connection conn, int iHeight) throws SQLException {
		ArrayList<RoundTransactionsVo> returnValue = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try {
			pstmt = conn.prepareStatement("SELECT t.id AS id, a.id AS uid, a.username AS username, a.is_anonymous, t.type AS type, t.amount AS amount"
					+ " FROM transactions AS t LEFT JOIN blocks AS b ON t.block_id = b.id LEFT JOIN accounts AS a ON t.account_id = a.id WHERE b.height = ?"
					+ " AND t.type = 'Credit' ORDER BY amount DESC ");
			
			pstmt.setInt(1, iHeight);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				RoundTransactionsVo vo = new RoundTransactionsVo();
				vo.setId(rs.getInt(1));
				vo.setUid(rs.getInt(2));
				vo.setUsername(rs.getString(3));
				vo.setIs_anonymous(rs.getInt(4));
				vo.setType(rs.getString(5));
				vo.setAmount(rs.getDouble(6));
				
				returnValue.add(vo);
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		
		
		return returnValue;
	}
}
