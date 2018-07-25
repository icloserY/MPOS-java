package model.vo.blockfinder;

public class BlockSolvedAccountVo {
	private int id;
	private int height;
	private String blockhash;
	private int confirmations;
	private double amount;
	private double difficulty;
	private int time;
	private int accounted;
	private int account_id;
	private String worker_name;
	private double shares;
	private int share_id;
	private String finder;
	private int is_anonymous;
	private int solvedblocks;
	private double generatedcoins;
	
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
	public int getConfirmations() {
		return confirmations;
	}
	public void setConfirmations(int confirmations) {
		this.confirmations = confirmations;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getAccounted() {
		return accounted;
	}
	public void setAccounted(int accounted) {
		this.accounted = accounted;
	}
	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public String getWorker_name() {
		return worker_name;
	}
	public void setWorker_name(String worker_name) {
		this.worker_name = worker_name;
	}
	public double getShares() {
		return shares;
	}
	public void setShares(double shares) {
		this.shares = shares;
	}
	public int getShare_id() {
		return share_id;
	}
	public void setShare_id(int share_id) {
		this.share_id = share_id;
	}
	public String getFinder() {
		return finder;
	}
	public void setFinder(String finder) {
		this.finder = finder;
	}
	public int getIs_anonymous() {
		return is_anonymous;
	}
	public void setIs_anonymous(int is_anonymous) {
		this.is_anonymous = is_anonymous;
	}
	public int getSolvedblocks() {
		return solvedblocks;
	}
	public void setSolvedblocks(int solvedblocks) {
		this.solvedblocks = solvedblocks;
	}
	public double getGeneratedcoins() {
		return generatedcoins;
	}
	public void setGeneratedcoins(double generatedcoins) {
		this.generatedcoins = generatedcoins;
	}
	
	
}
