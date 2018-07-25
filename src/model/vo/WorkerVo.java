package model.vo;

public class WorkerVo {
	private int id;
	private double hashrate;
	private double difficulty;
	private String username;
	private String password;
	private String monitor;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) { 
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMonitor() {
		return monitor;
	} 
	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}
	public double getHashrate() {
		return hashrate;
	}
	public void setHashrate(double hashrate) {
		this.hashrate = hashrate;
	}
	public double getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
