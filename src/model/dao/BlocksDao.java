package model.dao;

import java.sql.*;

import jdbc.*;
import model.vo.*;

public class BlocksDao extends Base {
	public BlocksDao() {
		this.table = "blocks";
	}

	private static BlocksDao blocksDao = new BlocksDao();

	public static BlocksDao getInstance() {
		return blocksDao;
	}
	
	public BlockVo getLast(Connection conn) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BlockVo blockVo = null;
		try {
			pstmt = conn.prepareStatement("select * from "+this.table+" order by height desc limit 1");
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				blockVo = new BlockVo();
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
				
			}
			
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return blockVo;
	} 

	public double getAvgBlockShares(Connection conn, int height, int limit) throws SQLException {
		double returnValue = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("select AVG(x.shares) AS average FROM (SELECT shares FROM "+this.table+" WHERE height <= ? ORDER BY height DESC LIMIT ?) AS x");
			pstmt.setInt(1, height);
			pstmt.setInt(2, limit);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				returnValue = rs.getDouble(1);
			}
			
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return returnValue;
	}

}
