package model.vo;

import java.sql.*;

public class InvitationsVo {
	private int id;
	private int account_id;
	private String email;
	private int token_id;
	private int is_activated;
	private Timestamp time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getToken_id() {
		return token_id;
	}
	public void setToken_id(int token_id) {
		this.token_id = token_id;
	}
	public int getIs_activated() {
		return is_activated;
	}
	public void setIs_activated(int is_activated) {
		this.is_activated = is_activated;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
}
