package model.vo.round;

public class DetailsBlockVo {
	private int id;
	private int height;
	private String blockhash;
	private double amount;
	private int confirmations;
	private double difficulty;
	private long time;
	private double shares;
	private String finder;
	private double estshares;
	private double round_time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getBlockhash() {
		return blockhash;
	}
	public void setBlockhash(String blockhash) {
		this.blockhash = blockhash;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getConfirmations() {
		return confirmations;
	}
	public void setConfirmations(int confirmations) {
		this.confirmations = confirmations;
	}
	public double getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public double getShares() {
		return shares;
	}
	public void setShares(double shares) {
		this.shares = shares;
	}
	public String getFinder() {
		return finder;
	}
	public void setFinder(String finder) {
		this.finder = finder;
	}
	public double getEstshares() {
		return estshares;
	}
	public void setEstshares(double estshares) {
		this.estshares = estshares;
	}
	public double getRound_time() {
		return round_time;
	}
	public void setRound_time(double round_time) {
		this.round_time = round_time;
	}
	
	
}
