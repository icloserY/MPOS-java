package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jdbc.CloseUtilities;
import model.Converter;

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
}
