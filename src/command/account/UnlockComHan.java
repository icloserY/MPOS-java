package command.account;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;
import model.vo.*;

public class UnlockComHan implements ComHanInterFace{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		
		@SuppressWarnings("unchecked")
		List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
				? new ArrayList<Map<String, String>>() : (ArrayList<Map<String, String>>) request.getAttribute("Errors");

		Map<String, String> popup = new HashMap<>();
		
		String token = request.getParameter("token");
		
		TokensDao tokensDao = TokensDao.getInstance();
		AccountsDao accountsDao = AccountsDao.getInstance();
		Connection conn = null;
		
		try{
			conn = JDBCConnection.getConnection();
			TokensVo tokensVo = tokensDao.getToken(conn, token, "account_unlock");
			
			
			if(token == null || token.trim().equals("")){
				popup = Popup.getPopup("Missing token", "alert alert-danger", null, null);
				popups.add(popup);
			} else if(tokensVo == null){
				popup = Popup.getPopup("Unable to re-activate your account. Invalid token.", "alert alert-danger", null, null);
				popups.add(popup);
			} else{
				if(accountsDao.setUserFailed(conn, tokensVo.getAccounts_id(), 0) == 1 &&
				   accountsDao.setUserPinFailed(conn, tokensVo.getAccounts_id(), 0) == 1 &&
				   accountsDao.setLocked(conn, tokensVo.getAccounts_id(), 0) == 1){
					
					tokensDao.deleteToken(conn, tokensVo.getToken());
					popup = Popup.getPopup("Account re-activated. Please login.", "alert alert-info", null, null);
					popups.add(popup);
					
				}else{
					popup = Popup.getPopup("Failed to re-activate account. Contact site support.", "alert alert-danger", null, null);
					popups.add(popup);
				}
			}
		} catch(NamingException | SQLException e){
			e.printStackTrace();
		} finally{
			request.setAttribute("Errors", popups); 
			CloseUtilities.close(conn);
		} 
		
		return null;
	}

}
