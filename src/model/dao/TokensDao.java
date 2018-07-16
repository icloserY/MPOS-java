package model.dao;

import java.sql.*;
import java.util.*;

import model.*;

public class TokensDao extends Base {
	protected String table = "tokens";
	private static TokensDao tokensDao = new TokensDao();
	Token_typesDao token_typesDao = Token_typesDao.getInstance();
	
	public static TokensDao getInstance() {
		return tokensDao;
	}
	
	public String createToken(Connection conn, String strType, int account_id) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String resultValue = "";
		int tokenId = token_typesDao.getTypeId(conn, strType);
		if(tokenId == 0){ 
			return resultValue;
		}
		
		// 랜덤 바이트 생성
		Random randomGenerator = new Random();
		byte[] randomBytes = new byte[32];
		randomGenerator.nextBytes(randomBytes);
		
		String strToken = Converter.bin2hex(randomBytes);
		
		pstmt = conn.prepareStatement("INSERT INTO ? (token, type, account_id) VALUES (?, ?, ?)");
		pstmt.setString(1, this.table);
		pstmt.setString(2, strToken);
		pstmt.setInt(3, tokenId);
		pstmt.setInt(4, account_id);
		
		int insertCount = pstmt.executeUpdate();
		if(insertCount == 0) {
			throw new SQLException();
		}
		
		return resultValue;
	}
	
	
}
 