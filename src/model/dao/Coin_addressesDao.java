package model.dao;

import java.sql.Connection;

public class Coin_addressesDao extends Base{
	public Coin_addressesDao() {
		this.table = "coin_addresses";
	}
	private static Coin_addressesDao settingsDao = new Coin_addressesDao();
	public static Coin_addressesDao getInstance() {
		return settingsDao;
	}
	
	public boolean existCoinAddress(Connection conn, String coinAddress) {
		
		return false;
	}
}
