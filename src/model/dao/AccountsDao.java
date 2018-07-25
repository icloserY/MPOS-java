package model.dao;

import java.security.*;
import java.sql.*;
import java.util.regex.*;

import javax.servlet.http.*;

import jdbc.*;
import model.*;
import model.vo.*;

public class AccountsDao extends Base {
	public AccountsDao() {
		this.table = "accounts";
	}

	protected AccountsVo accountsVo = new AccountsVo();
	private static AccountsDao accountsDao = new AccountsDao();

	public static AccountsDao getInstance() {
		return accountsDao;
	}

	public String getUserName(Connection conn, int id ) throws SQLException{
		String resultValue = "";
		resultValue = getSingle(conn, String.valueOf(id), "username", "id", 0, 1, false);
		return resultValue;
	}
	
	private String getUserEmail(Connection conn, String username) throws SQLException {
		String resultValue = "";
		resultValue = getSingle(conn, username, "email", "username", 1, 1, true);
		return resultValue;
	}
	
	private int getUserId(Connection conn, String username) throws SQLException {
		String resultValue = "";
		int returnValue = 0;
		resultValue = getSingle(conn, username, "id", "email", 1, 0, false);
		if (!resultValue.equals("")) {
			returnValue = Integer.parseInt(resultValue);
		}
		return returnValue;
	}

	public int getUserIdByEmail(Connection conn, String email) throws SQLException {
		String resultValue = "";
		int returnValue = 0;
		resultValue = getSingle(conn, email, "id", "email", 1, 0, false);

		if (!resultValue.equals("")) {
			returnValue = Integer.parseInt(resultValue);
		}
		return returnValue;
	}

	private String getUserNameByEmail(Connection conn, String email) throws SQLException {
		String resultValue = "";
		resultValue = getSingle(conn, email, "username", "email", 1, 1, false);
		return resultValue;
	}

	private int getLastLogin(Connection conn, int id) throws SQLException {
		String resultValue = "";
		int returnValue = 0;
		resultValue = getSingle(conn, String.valueOf(id), "last_login", "id", 0, 0, false);
		if (!resultValue.equals("")) {
			returnValue = Integer.parseInt(resultValue);
		}
		return returnValue;
	}

	private String getUserIp(Connection conn, int id) throws SQLException {
		String resultValue = "";
		resultValue = getSingle(conn, String.valueOf(id), "loggedIp", "id", 0, 1, false);
		return resultValue;
	}

	private int getUserFailed(Connection conn, int id) throws SQLException {
		String resultValue = "";
		int returnValue = 0;
		resultValue = getSingle(conn, String.valueOf(id), "failed_logins", "id", 0, 0, false);
		if (!resultValue.equals("")) {
			returnValue = Integer.parseInt(resultValue);
		}
		return returnValue;
	}

	/* loggedIp 변경 */
	private void setUserIp(Connection conn, int id, String ip) throws SQLException {
		updateSingle(conn, id, "loggedIp", ip, 1, this.table);
	}

	/* last_login 변경 */
	private void updateLoginTimestamp(Connection conn, int id) throws SQLException {
		long time = System.currentTimeMillis() / 1000;
		updateSingle(conn, id, "last_login", String.valueOf(time), 0, this.table);
	}

	/* failed_logins + 1 */
	private void incUserFailed(Connection conn, int id) throws SQLException {
		updateSingle(conn, id, "failed_logins", String.valueOf(getUserFailed(conn, id) + 1), 0, this.table);
	}

	/* is_locked 변경 */
	public int setLocked(Connection conn, int id, int value) throws SQLException {
		return updateSingle(conn, id, "is_locked", String.valueOf(value), 0, this.table);
	}
 
	/* filed_logins 변경 */
	public int setUserFailed(Connection conn, int id, int value) throws SQLException {
		return updateSingle(conn, id, "failed_logins", String.valueOf(value), 0, this.table);
	}
	
	/* failed_pins 변경 */
	public int setUserPinFailed(Connection conn, int id, int value) throws SQLException {
		return updateSingle(conn, id, "failed_pins", String.valueOf(value), 0, this.table);
	}
	
	// get is_Admin
	public boolean isAdmin(Connection conn, int id) throws SQLException {
		String resultValue = "";
		boolean returnValue = false;
		resultValue = getSingle(conn, String.valueOf(id), "is_admin", "id", 0, 0, false);
		if (!resultValue.equals("")) {
			returnValue = (Integer.parseInt(resultValue) == 0) ? false : true;
		}
		return returnValue;
	}

	// get is_locked
	public boolean isLocked(Connection conn, String name) throws SQLException {
		String resultValue = "";
		boolean returnValue = false;
		resultValue = getSingle(conn, name, "is_locked", "username", 1, 0, false);

		if (!resultValue.equals("")) {
			returnValue = (Integer.parseInt(resultValue) == 0) ? false : true;
		}
		return returnValue;
	}

	public boolean checkLogin(Connection conn, String username, String password, String ip, HttpServletRequest request)
			throws SQLException {
		String name = getUserNameByEmail(conn, username);
		// 공백 확인
		if (username.trim().length() == 0 || password.trim().length() == 0) {
			setErrorMessage("Invalid username or password.");
			return false;
		}
		// 이메일 형식 확인
		if (!checkEmail(username)) {
			setErrorMessage("Please login with your e-mail address");
			return false;
		} else {
			if (name.equals("")) {
				setErrorMessage("Invalid username or password.");
				return false;
			}
		}
		// 계정 잠금 확인
		if (isLocked(conn, name)) {
			setErrorMessage("Account locked. Please Check your Email for instructions to unlock.");
			return false;
		}
		// DB username, password 체크
		if (checkUserPassword(conn, name, password)) {
			// rest of login process
			int uid = accountsVo.getId();
			int lastLoginTime = getLastLogin(conn, uid);
			updateLoginTimestamp(conn, uid);
			String getIpAddress = getUserIp(conn, uid);
			setUserIp(conn, uid, ip);
			createSession(name, getIpAddress, lastLoginTime, request);
			return true;
		}
		setErrorMessage("Invalid username or password");
		int id = getUserId(conn, username);
		if (id != 0) {
			incUserFailed(conn, id);
			// 3번이상 로그인 실패시 해당 계정 is_locked 변경
			if (getUserFailed(conn, id) > 3) {
				setLocked(conn, id, 1);
				TokensDao tokensDao = TokensDao.getInstance();
				String token = tokensDao.createToken(conn, "account_unlock", id);
				if (!token.equals("")) {
					// 메일 발송
					MailVo mailVo = new MailVo();
					mailVo.setEmail(username);
					mailVo.setSubject("Account auto-locked");
					mailVo.setContent(GlobalSettings.get("mail.ftl.locked"));
					mailVo.setUrl(GlobalSettings.get("contextpath"));
					mailVo.setToken(token);
					
					boolean sendCheck = Mail.sendMail(mailVo);
					if (!sendCheck) {
						setErrorMessage("Send Mail Failed");
					}
				}
			}
		}

		return false;
	}

	/* 아이디 패스워드 체크 */
	public boolean checkUserPassword(Connection conn, String name, String password) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String[] aPassword = null;
		String password_Hash;
		try {
			pstmt = conn.prepareStatement(
					"SELECT username, pass, id, timezone, is_admin FROM accounts where LOWER(username) = LOWER(?) LIMIT 1");
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				accountsVo.setUsername(rs.getString(1));
				accountsVo.setPassword(rs.getString(2));
				accountsVo.setId(rs.getInt(3));
				accountsVo.setTimezone(rs.getString(4));
				accountsVo.setIs_admin(rs.getInt(5));
			} else {
				throw new SQLException();
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}

		aPassword = accountsVo.getPassword().split("\\$");

		password_Hash = aPassword.length == 1 ? getHash(password, 0, null)
				: getHash(password, Integer.parseInt(aPassword[1]), aPassword[2]);

		return password_Hash.equals(accountsVo.getPassword())
				&& name.toLowerCase().equals(accountsVo.getUsername().toLowerCase());

	}

	/* 이메일 형식 체크 */
	public boolean checkEmail(String email) {
		String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		boolean isNormal = m.matches();
		return isNormal;
	}

	/* password Hash 값 구하기 */
	private String getHash(String password, int version, String pepper) {
		pepper = pepper == null ? "" : pepper;
		String returnValue = "";

		switch (version) {
		case 0:
			returnValue = hash("SHA-256", password);
			break;
		case 1:
			returnValue = "$" + version + "$" + pepper + "$" + hash("SHA-256", password + pepper);
			break;
		}
		return returnValue;
	}

	private String hash(String string, String password) {
		String SHA = "";
		try {
			MessageDigest sh = MessageDigest.getInstance(string);
			sh.update(password.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			SHA = null;
		}
		return SHA;
	}

	private void createSession(String name, String lastIP, int lastLoginTime, HttpServletRequest request) {
		request.getSession().invalidate();
		HttpSession session = request.getSession();
		if (lastIP != null && lastLoginTime != 0) {
			String[] array = { lastIP, String.valueOf(lastLoginTime) };
			session.setAttribute("last_ip_pop", array);
		}
		session.setAttribute("AUTHENTICATED", 1);
		session.setAttribute("USERDATA", accountsVo);
	}

	public boolean register(Connection conn, SignUpVo signUpVo) {
		// 약관 동의 안할시
		if (signUpVo.getTac() == null || !signUpVo.getTac().equals("1")) {
			this.setErrorMessage("You need to accept our Terms and Conditions");
			return false;
		}
		// username이 40자리 넘어갈 시
		if (signUpVo.getUsername().length() > 40) {
			this.setErrorMessage("Username exceeding character limit");
			return false;
		}
		// coin address가 이미 존재 하는지, 유효한지(RPC)
		if (signUpVo.getCoinaddress() != null) {
			if (!Coin_addressesDao.getInstance().existCoinAddress(conn, signUpVo.getCoinaddress())) {
				this.setErrorMessage("Coin address is already taken");
				return false;
			}
			// JSON RPC로 지갑에 등록된 주소인지 확인
			// Coin address is not valid
		} else {
			this.setErrorMessage("You need to insert your Coin address");
			return false;
		}
		// username이 정규식을 만족하는지
		if (!Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$", signUpVo.getUsername())) {
			this.setErrorMessage("Username may only contain alphanumeric characters");
			return false;
		}
		// email이 중복될 시
		if (existEmail(conn, signUpVo.getEmail1())) {
			this.setErrorMessage("This e-mail address is already taken");
			return false;
		}
		// password가 8자리 이하
		if (signUpVo.getPassword1().length() < 8) {
			this.setErrorMessage("Password is too short, minimum of 8 characters required");
			return false;
		}
		// password1과 password2가 일치 하지 않을시
		if (!signUpVo.getPassword1().equals(signUpVo.getPassword2())) {
			this.setErrorMessage("Password do not match");
			return false;
		}
		// email1이 비었거나 e-mail 형식에 맞지 않을시 아이디는 영문자로 시작하는 6~20자 영문자 또는 숫자이어야 합니다.
		if (signUpVo.getEmail1() == null || !checkEmail(signUpVo.getEmail1())) {
			this.setErrorMessage("invalid e-mail address");
			return false;
		}
		// email1과 email2가 일치 하지 않을시
		if (!signUpVo.getEmail1().equals(signUpVo.getEmail2())) {
			this.setErrorMessage("E-mail do not match");
			return false;
		}
		// pin이 숫자 정규식이 아니거나 길이가 4자리가 아닐시
		if (!Pattern.matches("^[0-9]*$", signUpVo.getPin()) || signUpVo.getPin().length() != 4) {
			this.setErrorMessage("Invalid PIN");
			return false;
		}
		PreparedStatement pstmt = null;
		int is_admin = 0;
		try {
			conn.setAutoCommit(false);
			if (getFirstID(conn) > 0) {
				int is_locked = 1;
				is_locked = !GlobalSettings.get("accounts_confirm_email_disabled").equals("true") ? 1 : 0;
				is_admin = 0;
				pstmt = conn.prepareStatement(""
						+ "INSERT INTO Accounts (username, pass, email, signup_timestamp, pin, api_key, is_locked) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?)");
				pstmt.setInt(7, is_locked);
			} else {
				int is_locked = 0;
				is_admin = 1;
				pstmt = conn.prepareStatement(""
						+ "INSERT INTO Accounts (username, pass, email, signup_timestamp, pin, api_key, is_admin, is_locked)"
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)" + "");
				pstmt.setInt(7, is_admin);
				pstmt.setInt(8, is_locked);
			}
			String password_hash = this.getHash(signUpVo.getPassword1(), 1, Converter.getRandomByte());
			String pin_hash = this.getHash(signUpVo.getPin(), 1, Converter.getRandomByte());
			String apikey_hash = this.getHash(signUpVo.getUsername(), 0, null);
			String username_clean = signUpVo.getUsername();
			int signup_time = (int)(System.currentTimeMillis() / 1000);
			pstmt.setString(1, username_clean);
			pstmt.setString(2, password_hash);
			pstmt.setString(3, signUpVo.getEmail1());
			pstmt.setInt(4, signup_time);
			pstmt.setString(5, pin_hash);
			pstmt.setString(6, apikey_hash);
			int count = pstmt.executeUpdate();
			if (count == 0) {
				this.setErrorMessage("register Failed please try later");
				throw new SQLException("register Failed please try later");
			} else {
				if (signUpVo.getCoinaddress() != null)  {
					int new_account_id = this.getLastUserId(conn, signUpVo.getUsername());
					if(new_account_id == 0) {
						this.setErrorMessage("Coin address register Failed please try later");
						throw new SQLException("select lastestuser failed");
					}
					boolean success = Coin_addressesDao.getInstance().add(conn, new_account_id, signUpVo.getCoinaddress(), null);
					if(!success) {
						this.setErrorMessage("register Failed please try later");
						throw new SQLException("coin_address failed insert");
					}
				}
				// accounts_confirm_email_disabled == true 일경우 토큰, 이메일 발송 하지 않음
				if (GlobalSettings.get("accounts_confirm_email_disabled").equals("true")) {
					
				} else if(GlobalSettings.get("accounts_confirm_email_disabled").equals("false") && is_admin != 1){
					// accounts_confirm_email_disabled == false
					TokensDao tokensDao = TokensDao.getInstance();
					String token = null;
					int id = 0;
					if ((id = getUserId(conn, signUpVo.getEmail1())) != 0) {
						token = tokensDao.createToken(conn, "confirm_email", id);
					} else {
						this.setErrorMessage("do not find id by email");
						throw new SQLException("do not find id by email");
					}
					if (!token.equals("")) {
						// 메일 발송
						MailVo mailVo = new MailVo();
						mailVo.setSubject("Confirm Your Registration");
						mailVo.setContent(GlobalSettings.get("mail.ftl.confirm.email"));
						mailVo.setEmail(signUpVo.getEmail1());
						mailVo.setUrl(GlobalSettings.get("contextpath"));
						mailVo.setToken(token);

						boolean sendCheck = Mail.sendMail(mailVo);
						if (!sendCheck) {
							this.setErrorMessage("register Failed please try later");
							throw new SQLException("email send failed");
						}
					} else {
						this.setErrorMessage("Unable to create confirm_email token");
						throw new SQLException("Unable to create confirm_email token");
					}
				}
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally {
			CloseUtilities.close(pstmt);
		}
		return true;
	}

	private boolean existEmail(Connection conn, String email) {
		String value = "";
		try {
			value = this.getSingle(conn, email, "email", "email", 1, 1, false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(value.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private int getFirstID(Connection conn) {
		return 1;
	}
	
	private int getLastUserId(Connection conn, String username) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int id = 0;
		try {
			pstmt = conn.prepareStatement(""
					+ "Select id from accounts order by id desc limit 1");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} finally {
			CloseUtilities.close(pstmt);
			CloseUtilities.close(rs);
		}
		return id;
	}
	 
	// 비밀번호 초기화 메일 전송
	public boolean initResetPassword(Connection conn, String username) throws SQLException {
		String name = getUserNameByEmail(conn, username);
		if (username.trim().equals("")){
			setErrorMessage("Username must not be empty");
			return false;
		}
		if(checkEmail(username)){
			if(name.equals("")){
				setErrorMessage("Invalid username or password.");
				return false;
			}else{
				username = name;
			}
		}
		
		String email = getUserEmail(conn, username); 
		if(email.equals("")){
			setErrorMessage("Please check your mail account to finish your password reset");
			return false;
		}
		
		TokensDao tokensDao = TokensDao.getInstance();
		String token = tokensDao.createToken(conn, "password_reset", getUserId(conn, email));
		if(token.equals("")){
			setErrorMessage("Unable to setup token for password reset");
			return false;
		}
		
		MailVo mailVo = new MailVo();
		mailVo.setUrl(GlobalSettings.get("contextpath"));
		mailVo.setEmail(email);
		mailVo.setToken(token);
		mailVo.setSubject("Password Reset Request");
		mailVo.setContent(GlobalSettings.get("mail.ftl.reset"));
		mailVo.setUsername(name);
		boolean sendCheck = Mail.sendMail(mailVo);
		if (!sendCheck) {
			setErrorMessage("Unable to send mail to your address");
			return false;
		}
		return true;
	}

	
	// Password Reset 
	public boolean resetPassword(Connection conn, String token, String newPassword, String newPassword2) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		TokensDao tokensDao = TokensDao.getInstance();
		TokensVo tokensVo = tokensDao.getToken(conn, token, "password_reset");
		if(tokensVo != null){
			if(!newPassword.equals(newPassword2)){
				setErrorMessage("New passwords do not match");
				return false;
			}
			if(newPassword.length() < 8){
				setErrorMessage("New password is too short, please use more than 8 chars");
				return false;
			}
			String new_hash = getHash(newPassword, 1, Converter.getRandomByte());
			try{
				pstmt = conn.prepareStatement("UPDATE "+this.table+" set pass = ? where id = ? LIMIT 1");
				pstmt.setString(1, new_hash);
				pstmt.setInt(2, tokensVo.getAccounts_id());
				
				int updateRow = pstmt.executeUpdate();
				if(updateRow == 1){
					int deleteRow = tokensDao.deleteToken(conn, tokensVo.getToken());
					if(deleteRow == 1){
						return true;
					}else{
						setErrorMessage("Unable to invalidate used token");
					}
				}else{
					setErrorMessage("Unable to set new password or you chose the same password. Please use a different one.");
				}
			}finally{
				CloseUtilities.close(rs);
				CloseUtilities.close(pstmt);
			}
		}else{
			setErrorMessage("Invalid token: " + tokensDao.getError());
		}
		
	    return false;
	}

	// 로그아웃, 세션 죽이기
	public void logoutUser(HttpServletRequest request) {
		
		request.getSession().invalidate();
		 
	}

	
	/* 사용자 인증 및 로그인 가능여부 체크 
	 * session 확인
	 * 계정이 잠긴 경우 session 파괴 */
	public boolean isAuthenticated(Connection conn, boolean logout, HttpServletRequest request) throws SQLException {
		HttpSession session = request.getSession();
		int authenticated = session.getAttribute("AUTHENTICATED") != null ? (int) session.getAttribute("AUTHENTICATED") : 0;
		AccountsVo userData = session.getAttribute("USERDATA") != null ? (AccountsVo) session.getAttribute("USERDATA") : null;
		if (authenticated == 1 && !isLocked(conn, userData.getUsername())
				&& getUserIp(conn, userData.getId()).equals(request.getRemoteAddr())) {
			return true;
		}

		if (logout) {
			logoutUser(request);
		}
		return false;
	}
}
