package model.dao;

import java.security.*;
import java.sql.*;
import java.text.*;
import java.util.regex.*;

import javax.servlet.http.*;

import jdbc.*;
import model.*;
import model.vo.*;

public class AccountsDao extends Base {
	protected String table = "accounts";
	protected AccountsVo accountsVo = new AccountsVo();
	private static AccountsDao accountsDao = new AccountsDao();

	public static AccountsDao getInstance() {
		return accountsDao;
	}

	private int getUserId(Connection conn, String username) throws SQLException {
		String resultValue = "";
		int returnValue = 0;
		resultValue = getSingle(conn, username, "id", "username", 1, 0, false);
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
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyymmddhhmmss");
		String nowTime = dayTime.format(new Date(time));
		updateSingle(conn, id, "last_login", nowTime, 0, this.table);
	}

	/* failed_logins + 1 */
	private void incUserFailed(Connection conn, int id) throws SQLException {
		updateSingle(conn, id, "failed_logins", String.valueOf(getUserFailed(conn, id)), 0, this.table);
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

	public boolean checkLogin(Connection conn, String username, String password, String ip,
			HttpServletRequest request) throws SQLException {
		String name = getUserNameByEmail(conn, username);
		// 공백 확인
		if (username.trim().length() > 0 || password.trim().length() > 0) {
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
					request.setAttribute("token", token);
					String url = request.getRequestURL().substring(0, (request.getRequestURL().length() - request.getServletPath().length()));
					request.setAttribute("url", url);
					MailVo mailVo = new MailVo();
					mailVo.setEmail(username);
					mailVo.setSubject("Account auto-locked");
					mailVo.setContent("notifications/locked");
					boolean sendCheck = Mail.sendMail(mailVo);
					if(!sendCheck){
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

		aPassword = accountsVo.getPassword().split("$");
		password_Hash = aPassword.length == 1 ? getHash(password, 0, null)
				: getHash(password, Integer.parseInt(aPassword[0]), aPassword[1]);

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
			returnValue = hash("sha256", password);
			break;
		case 1:
			returnValue = "$" + version + "$" + pepper + "$" + hash("sha256", password + pepper);
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
		if (!lastIP.equals("") && lastLoginTime != 0) {
			String[] array = { lastIP, String.valueOf(lastLoginTime) };
			session.setAttribute("last_ip_pop", array);
		}
		session.setAttribute("AUTHENTICATED", 1);
		session.setAttribute("USERDATA", accountsVo);
	}
	
	
}
