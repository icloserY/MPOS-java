package model.vo;

public class DonationVo {
	private double donation;
	private String username;
	private int is_anonymous;
	private float donate_percent;
	public double getDonation() {
		return donation;
	}
	public void setDonation(double donation) {
		this.donation = donation;
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
	public float getDonate_percent() {
		return donate_percent;
	}
	public void setDonate_percent(float donate_percent) {
		this.donate_percent = donate_percent;
	}
}
