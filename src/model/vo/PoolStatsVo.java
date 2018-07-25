package model.vo;

public class PoolStatsVo {
	private int count;
	private double average;
	private double shares;
	private double rewards;
	private double expected;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	public double getShares() {
		return shares;
	}
	public void setShares(double shares) {
		this.shares = shares;
	}
	public double getRewards() {
		return rewards;
	}
	public void setRewards(double rewards) {
		this.rewards = rewards;
	}
	public double getExpected() {
		return expected;
	}
	public void setExpected(double expected) {
		this.expected = expected;
	}
}
