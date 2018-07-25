package model.vo.round;

public class RoundAccountsVo {
	private int id;
	private String username;
	private int is_anonymous;
	private float valid;
	private float invalid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public float getValid() {
		return valid;
	}
	public void setValid(float valid) {
		this.valid = valid;
	}
	public float getInvalid() {
		return invalid;
	}
	public void setInvalid(float invalid) {
		this.invalid = invalid;
	}
	
}
