package model.dao;

import java.sql.*;
import java.util.*;

import jdbc.*;
import model.*;
import model.vo.*;

public class TokensDao extends Base {
	public TokensDao() {
		this.table = "tokens";
	}

	private static TokensDao tokensDao = new TokensDao();
	Token_typesDao token_typesDao = Token_typesDao.getInstance();

	public static TokensDao getInstance() {
		return tokensDao;
	}

	public String createToken(Connection conn, String strType, int account_id) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String resultValue = "";
		int tokenId = token_typesDao.getTypeId(conn, strType);
		if (tokenId == 0) {
			return resultValue;
		}

		// 랜덤 바이트 생성
		Random randomGenerator = new Random();
		byte[] randomBytes = new byte[32];
		randomGenerator.nextBytes(randomBytes);

		String strToken = Converter.bin2hex(randomBytes);

		pstmt = conn.prepareStatement("INSERT INTO " + this.table + " (token, type, account_id) VALUES (?, ?, ?)");
		pstmt.setString(1, strToken);
		pstmt.setInt(2, tokenId);
		pstmt.setInt(3, account_id);

		int insertCount = pstmt.executeUpdate();
		if (insertCount == 0) {
			throw new SQLException();
		}

		return strToken;
	}

	public TokensVo getToken(Connection conn, String strToken, String strType) throws SQLException {
		TokensVo tokensVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Token_typesDao token_typesDao = Token_typesDao.getInstance();
		if (strType.trim().equals("") || token_typesDao.getTypeId(conn, strType) == 0) {
			setErrorMessage("Invalid token type: " + strType);
			return null;
		}
		try {
			pstmt = conn.prepareStatement("SELECT * from " + this.table + " where token = ? LIMIT 1");
			pstmt.setString(1, strToken);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				tokensVo = new TokensVo();
				tokensVo.setId(rs.getInt(1));
				tokensVo.setAccounts_id(rs.getInt(2));
				tokensVo.setToken(rs.getString(3));
			}else{
				setErrorMessage(strToken);
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return tokensVo;
	}

	public int deleteToken(Connection conn, String token) throws SQLException {
		PreparedStatement pstmt = null;
		int deleteRow = 0;
		try{
			pstmt = conn.prepareStatement("Delete from "+this.table+" where token = ? LIMIT 1");
			pstmt.setString(1, token);
			
			deleteRow = pstmt.executeUpdate();
			
		}finally{
			CloseUtilities.close(pstmt);
		}
		
		return deleteRow;
	}

}
