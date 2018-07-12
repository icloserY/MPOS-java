package model.dao;

import java.sql.Connection;

import model.vo.SignUpVo;

public class AccountsDao {
	protected String table = "accounts";
	private static AccountsDao instance = new AccountsDao();
	public static AccountsDao getInstance() {
		return instance;
	}
	public boolean register(Connection conn, SignUpVo signUpVo) {
		
		return true;
	}
}
