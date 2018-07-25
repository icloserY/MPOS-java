package model.dao;

import java.sql.*;

public class Token_typesDao extends Base {
	public Token_typesDao() {
		this.table = "token_types";
	}
	private static Token_typesDao token_typesDao = new Token_typesDao();

	public static Token_typesDao getInstance() {
		return token_typesDao;
	}

	public int getTypeId(Connection conn, String strType) throws SQLException {
		String resultValue = "";
		int returnValue = 0;
		resultValue = getSingle(conn, strType, "id", "name", 1, 0, false);
		if(!resultValue.equals("")){
			returnValue = Integer.parseInt(resultValue);
		}
		return returnValue;
	}
}
