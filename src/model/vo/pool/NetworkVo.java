package model.vo.pool;

public class NetworkVo {
	private double difficulty;
	private int block;
	private double estNextDifficulty;
	private double estTimePerBlock;
	private double blocksUntilDiffChange;
	public double getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}
	public int getBlock() {
		return block;
	}
	public void setBlock(int block) {
		this.block = block;
	}
	public double getEstNextDifficulty() {
		return estNextDifficulty;
	}
	public void setEstNextDifficulty(double estNextDifficulty) {
		estNextDifficulty = estNextDifficulty;
	}
	public double getEstTimePerBlock() {
		return estTimePerBlock;
	}
	public void setEstTimePerBlock(double estTimePerBlock) {
		estTimePerBlock = estTimePerBlock;
	}
	public double getBlocksUntilDiffChange() {
		return blocksUntilDiffChange;
	}
	public void setBlocksUntilDiffChange(double blocksUntilDiffChange) {
		blocksUntilDiffChange = blocksUntilDiffChange;
	}
}
