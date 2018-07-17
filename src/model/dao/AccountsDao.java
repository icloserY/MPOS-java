package model.dao;

import java.security.*;
import java.sql.*;
import java.util.regex.*;

import javax.servlet.http.*;

import com.mysql.jdbc.log.Log;

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
	private void setLocked(Connection conn, int id, int value) throws SQLException {
		updateSingle(conn, id, "is_locked", String.valueOf(value), 0, this.table);
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
					String url = request.getRequestURL().substring(0, (request.getRequestURL().length() - request.getServletPath().length()));
					
					MailVo mailVo = new MailVo();
					mailVo.setEmail(username);
					mailVo.setSubject("Account auto-locked");
					mailVo.setContent("notifications/locked");
					mailVo.setUrl(url);
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
	private boolean checkEmail(String email) {
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
			return false;
		}
		// username이 40자리 넘어갈 시
		if (signUpVo.getUsername().length() > 40) {
			return false;
		}
		// coin address가 이미 존재 하는지, 유효한지(RPC)
		if (signUpVo.getCoinaddress() != null) {
			if (!Coin_addressesDao.getInstance().existCoinAddress(conn, signUpVo.getCoinaddress())) {
				return false;
			}
			// JSON RPC로 지갑에 등록된 주소인지 확인
		}
		// username이 정규식을 만족하는지
		if (Pattern.matches("/^[a-z]+[a-z0-9]{5,19}$/g", signUpVo.getUsername())) {
			return false;
		}
		// email이 중복될 시
		if (existEmail(conn, signUpVo.getEmail1())) {
			return false;
		}
		// password가 8자리 이하
		if (signUpVo.getPassword1().length() < 8) {
			return false;
		}
		// password1과 password2가 일치 하지 않을시
		if (!signUpVo.getPassword1().equals(signUpVo.getPassword2())) {
			return false;
		}
		// email1이 비었거나 e-mail 형식에 맞지 않을시 아이디는 영문자로 시작하는 6~20자 영문자 또는 숫자이어야 합니다.
		if (signUpVo.getEmail1() == null || checkEmail(signUpVo.getEmail1())) {
			return false;
		}
		// email1과 email2가 일치 하지 않을시
		if (!signUpVo.getEmail1().equals(signUpVo.getEmail2())) {
			return false;
		}
		// pin이 숫자 정규식이 아니거나 길이가 4자리가 아닐시
		if (Pattern.matches("/([^a-zA-Z0-9-_])/", signUpVo.getPin()) || signUpVo.getPin().length() != 4) {
			return false;
		}
		PreparedStatement pstmt = null;
		try {
			if (getFirstID() > 0) {
				int is_locked = 1;
				/*
				 * ! $this->setting->getValue('accounts_confirm_email_disabled') ? is_locked = 1
				 * : is_locked = 0;
				 */
				int is_admin = 0;
				pstmt = conn.prepareStatement(""
						+ "INSERT INTO $this->table (username, pass, email, signup_timestamp, pin, api_key, is_locked) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?)");
				pstmt.setInt(7, is_locked);
			} else {
				int is_locked = 0;
				int is_admin = 1;
				pstmt = conn.prepareStatement(""
						+ "INSERT INTO $this->table (username, pass, email, signup_timestamp, pin, api_key, is_admin, is_locked)"
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)" + "");
				pstmt.setInt(7, is_admin);
				pstmt.setInt(8, is_locked);
			}
			String password_hash = this.getHash(signUpVo.getPassword1(), 1, Converter.getRandomByte());
			String pin_hash = this.getHash(signUpVo.getPin(), 1, Converter.getRandomByte());
			String apikey_hash = this.getHash(signUpVo.getUsername(), 0, null);
			String username_clean = signUpVo.getUsername();
			int signup_time = (int)System.currentTimeMillis() / 1000;
			pstmt.setString(1, username_clean);
			pstmt.setString(2, password_hash);
			pstmt.setString(3, signUpVo.getEmail1());
			pstmt.setInt(4, signup_time);
			pstmt.setString(5, pin_hash);
			pstmt.setString(6, apikey_hash);
			int count = pstmt.executeUpdate();
			if(count == 0) {
				throw new SQLException();
			} else {
				//accounts_confirm_email_disabled == true
				
				//accounts_confirm_email_disabled == false 일경우 토큰, 이메일 발송 하지 않음
				
			}
			/*
			if ($this->checkStmt($stmt) && $stmt->bind_param('sssissi', $username_clean, $password_hash, $email1, $signup_time, $pin_hash, $apikey_hash, $is_locked) && $stmt->execute()) {
			      $new_account_id = $this->mysqli->lastused->insert_id;
			      if (!is_null($coinaddress)) $this->coin_address->add($new_account_id, $coinaddress);
			      if (! $this->setting->getValue('accounts_confirm_email_disabled') && $is_admin != 1) {
			        if ($token = $this->token->createToken('confirm_email', $stmt->insert_id)) {
			          $aData['username'] = $username_clean;
			          $aData['token'] = $token;
			          $aData['email'] = $email1;
			          $aData['subject'] = 'E-Mail verification';
			          if (!$this->mail->sendMail('register/confirm_email', $aData)) {
			            $this->setErrorMessage('Unable to request email confirmation: ' . $this->mail->getError());
			            return false;
			          }
			          return true;
			        } else {
			          $this->setErrorMessage('Failed to create confirmation token');
			          $this->debug->append('Unable to create confirm_email token: ' . $this->token->getError());
			          return false;
			        }
			      } else {
			        return true;
			      }
			    } else {
			      $this->setErrorMessage( 'Unable to register' );
			      $this->debug->append('Failed to insert user into DB: ' . $this->mysqli->lastused->error);
			      echo $this->mysqli->lastused->error;
			      if ($stmt->sqlstate == '23000') $this->setErrorMessage( 'Username or email already registered' );
			      return false;
			    }
			    */
		} catch (SQLException e) {
			//에러메시지 추가 $this->setErrorMessage('Failed to create confirmation token');
			e.printStackTrace();
			return false;
		} finally {
			CloseUtilities.close(pstmt);
		}
		return true;
	}

	private boolean existEmail(Connection conn, String email) {

		return true;
	}

	private int getFirstID() {
		return 1;
	}
}
