package model.dao;

import java.sql.*;

import jdbc.*;
import model.*;

public class Coin_addressesDao extends Base {
	public Coin_addressesDao() {
		this.table = "coin_addresses";
	}

	private static Coin_addressesDao settingsDao = new Coin_addressesDao();

	public static Coin_addressesDao getInstance() {
		return settingsDao;
	}

	public boolean existCoinAddress(Connection conn, String coinAddress) {
		String value = "";
		try {
			value = this.getSingle(conn, coinAddress, "coin_address", "coin_address", 1, 1, false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (value.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean add(Connection conn, int new_account_id, String coin_address, String currency) {
		if (currency == null) {
			currency = "LTC";
		}
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(""
					+ "INSERT INTO Coin_addresses (account_id, coin_address, currency)"
					+ "VALUES (?, ?, ?)");
			pstmt.setInt(1, new_account_id);
			pstmt.setString(2, coin_address);
			pstmt.setString(3, currency);
			count = pstmt.executeUpdate();
			if (count == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			CloseUtilities.close(pstmt);
		}
	}

	public String getCoinAddress(Connection conn, int id) {
		String resultValue = "";
		String currency = GlobalSettings.get("currency");
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		try {
			pstmt = conn.prepareStatement("SELECT coin_address from "+this.table+" where account_id = ? AND currency = ?");
			pstmt.setInt(1, id);
			pstmt.setString(2, currency);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				resultValue = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			CloseUtilities.close(pstmt);
		}
		
		
		return resultValue;
	}
}
