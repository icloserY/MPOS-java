package model.vo.round;

public class RoundTransactionsVo {
	private int id;
	private int uid;
	private String username;
	private int is_anonymous;
	private String type;
	private double amount;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getIs_anonymous() {
		return is_anonymous;
	}
	public void setIs_anonymous(int is_anonymous) {
		this.is_anonymous = is_anonymous;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
