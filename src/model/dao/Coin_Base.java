package model.dao;

public class Coin_Base extends Base{
	  protected int target_bits = 0;

	  protected int share_difficulty_precision = 0;

	  protected int coin_value_precision = 8;
	  
	  
	  public double calcHashrate(double shares, int interval){
		  return shares * Math.pow(2, this.target_bits) / interval / 1000;
	  }
	  
	  
	  public int getTargetBits(){
		  return this.target_bits;
	  }


	public double calcEstaimtedShares(double dDifficulty) {
		return Math.round( Math.pow( 2, (32 - this.getTargetBits())) * dDifficulty);
		/* return Math.round( Math.pow( 2, (32 - this.getTargetBits())) * dDifficulty, this.share_difficulty_precision);
		 * share_difficulty_precision 값 변경시 수정 필요 
		 * 
		 * */
	}


	public int getShareDifficultyPrecision() {
		return this.share_difficulty_precision;
	}


	public double calcNetworkExpectedTimePerBlock(double dDifficulty, double hashrate) {
		if(hashrate > 0){
			return Math.pow(2, 32) * dDifficulty / hashrate;
		}else {
			return 0;
		}
	}
 
}
