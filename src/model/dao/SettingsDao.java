package model.dao;

import java.sql.*;

public class SettingsDao extends Base {
	public SettingsDao() {
		this.table = "settings";
	}
	private static SettingsDao settingsDao = new SettingsDao();
	public static SettingsDao getInstance() {
		return settingsDao;
	}
	
	/* Settings 테이블에서 name에 따른 value 찾기 */
	public String getValue(Connection conn, String name) throws SQLException{
		String resultValue = "";
		resultValue = this.getSingle(conn, name, "value", "name", 1, 1, false);
		return resultValue;
	}
}
 