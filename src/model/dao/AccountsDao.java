package model.dao;

import java.security.*;
import java.sql.*;
import java.util.regex.*;

import javax.naming.*;

import jdbc.*;
import model.vo.*;

public class AccountsDao extends Base {
	protected String table = "accounts";
	private static AccountsDao accountsDao = new AccountsDao();

	public static AccountsDao getInstance() {
		return accountsDao;
	}

	public int getUserIdByEmail(String email) {
		String resultValue = "";
		int returnValue = 0;
		try {
			Connection conn = JDBCConnection.getConnection();
			resultValue = getSingle(conn, email, "id", "email", 1, 0, false);
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return 0;
		}
		if (!resultValue.equals("")) {
			returnValue = Integer.parseInt(resultValue);
		}
		return returnValue;
	}

	private String getUserNameByEmail(String email) {
		String resultValue = "";
		try {
			Connection conn = JDBCConnection.getConnection();
			resultValue = getSingle(conn, email, "username", "email", 1, 1, false);
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return "";
		}

		return resultValue;
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
	public boolean isLocked(String name) {
		String resultValue = "";
		boolean returnValue = false;
		try {
			Connection conn = JDBCConnection.getConnection();
			resultValue = getSingle(conn, name, "is_locked", "username", 1, 0, false);
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		}
		if (!resultValue.equals("")) {
			returnValue = (Integer.parseInt(resultValue) == 0) ? false : true;
		}
		return returnValue;
	}

/*	public boolean checkLogin(Connection conn, String username, String password){
		String name = getUserNameByEmail(username);
		// 공백 확인  
	    if ( username.trim().length() > 0 || password.trim().length() > 0) {
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
	    if (isLocked(name)) {
	      setErrorMessage("Account locked. Please Check your Email for instructions to unlock.");
	      return false;
	    }
	    
	//    if (checkUserPassword(conn, name, password)) {
	      // delete notification cookies
	      setcookie("motd-box", "", time()-3600);
	      setcookie("lastlogin-box", "", time()-3600);
	      setcookie("backend-box", "", time()-3600);
	      // rest of login process
	      $uid = $this->getUserId($username);
	      $lastLoginTime = $this->getLastLogin($uid);
	      $this->updateLoginTimestamp($uid);
	      $getIPAddress = $this->getUserIp($uid);
	      if ($getIPAddress !== $this->getCurrentIP()) {
	        $this->log->log("warn", "$username has logged in with a different IP, saved is [$getIPAddress]");
	      }
	      $setIPAddress = $this->setUserIp($uid, $_SERVER['REMOTE_ADDR']);
	      $this->createSession($username, $getIPAddress, $lastLoginTime);
	      if ($setIPAddress) {
	        // send a notification if success_login is active
	        $uid = $this->getUserId($username);
	        $notifs = new Notification();
	        $notifs->setDebug($this->debug);
	        $notifs->setMysql($this->mysqli);
	        $notifs->setSmarty($this->smarty);
	        $notifs->setConfig($this->config);
	        $notifs->setSetting($this->setting);
	        $notifs->setErrorCodes($this->aErrorCodes);
	        $ndata = $notifs->getNotificationSettings($uid);
	        if ((array_key_exists('push_success_lo', $ndata) && $ndata['push_success_lo']) || (array_key_exists('success_login', $ndata) && $ndata['success_login'])){
	          // seems to be active, let's send it
	          $aDataN['username'] = $username;
	          $aDataN['email'] = $this->getUserEmail($username);
	          $aDataN['subject'] = 'Successful login notification';
	          $aDataN['LOGINIP'] = $this->getCurrentIP();
	          $aDataN['LOGINUSER'] = $username;
	          $aDataN['LOGINTIME'] = date('m/d/y H:i:s');
	          $notifs->sendNotification($uid, 'success_login', $aDataN);
	        }
	        return true;
	      }
	    }
	    $this->setErrorMessage("Invalid username or password");
	    $this->log->log('error', "Authentication failed for $username");
	    if ($id = $this->getUserId($username)) {
	      $this->incUserFailed($id);
	      // Check if this account should be locked
	      if (isset($this->config['maxfailed']['login']) && $this->getUserFailed($id) >= $this->config['maxfailed']['login']) {
	        $this->setLocked($id, 1);
	        $this->log->log("warn", "$username locked due to failed logins, saved is [".$this->getUserIp($this->getUserId($username))."]");
	        if ($token = $this->token->createToken('account_unlock', $id)) {
	          $aData['token'] = $token;
	          $aData['username'] = $username;
	          $aData['email'] = $this->getUserEmail($username);
	          $aData['subject'] = 'Account auto-locked';
	          $this->mail->sendMail('notifications/locked', $aData);
	        }
	      }
	    }

		
		// return false;
	}
*/
	/* 아이디 패스워드 체크 */
	public boolean checkUserPassword(Connection conn, String name, String password) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AccountsVo accountsVo = new AccountsVo();
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
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(string.getBytes());
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
}
