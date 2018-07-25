package model.vo.pool;

public class TopContributorsVo {
	private int count;
	private double hashrate;
	private String account;
	private float donate_percent;
	private int is_anonymous;
	private double shares;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public float getDonate_percent() {
		return donate_percent;
	}
	public void setDonate_percent(float donate_percent) {
		this.donate_percent = donate_percent;
	}
	public int getIs_anonymous() {
		return is_anonymous;
	}
	public void setIs_anonymous(int is_anonymous) {
		this.is_anonymous = is_anonymous;
	}
	public double getShares() {
		return shares;
	}
	public void setShares(double shares) {
		this.shares = shares;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getHashrate() {
		return hashrate;
	}
	public void setHashrate(double hashrate) {
		this.hashrate = hashrate;
	}
	
	
}
