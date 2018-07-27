package model.dao;

import java.sql.*;
import java.util.*;

import jdbc.*;
import model.*;
import model.vo.*;

public class InvitationsDao extends Base {
	public InvitationsDao() {
		this.table = "invitations";
	}
	private static InvitationsDao invitationsDao = new InvitationsDao();
	public static InvitationsDao getInstance() {
		return invitationsDao;
	}
	public int getCountInvitations(Connection conn, int account_id) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int returnValue = 0;
		try{
			pstmt = conn.prepareStatement("SELECT count(id) AS total FROM "+this.table+" WHERE account_id = ?");
			pstmt.setInt(1, account_id);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				returnValue = rs.getInt(1);
			}
			
		}finally{
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		
		
		return returnValue;
	}
	public boolean sendInvitation(Connection conn, int account_id, MailVo aData) throws SQLException {
		AccountsDao accountsDao = AccountsDao.getInstance();
		TokensDao tokensDao = TokensDao.getInstance();
		
		if(aData.getEmail() == null || !accountsDao.checkEmail(aData.getEmail())){
			setErrorMessage(GlobalSettings.get("error.E0023"));
			return false;
		}
		if(preg_match("[^a-z_/./!/?/-0-9 ]", aData.getMessage())){
			setErrorMessage(GlobalSettings.get("error.E0024"));
			return false;
		}
		
		if(!accountsDao.getEmail(conn, aData.getEmail()).equals("")){
			setErrorMessage(GlobalSettings.get("error.E0025"));
			return false;
		}
		if(!getByEmail(conn, aData.getEmail()).equals("")){
			setErrorMessage(GlobalSettings.get("error.E0026"));
			return false;
		}
		
		aData.setToken(tokensDao.createToken(conn, "invitation", account_id));
		if(aData.getToken().equals("")){
			setErrorMessage(GlobalSettings.get("error.E0027") + "Invalid token type: " + "invitation");
			return false;
		}
		
		aData.setUsername(accountsDao.getUserName(conn, account_id));
		aData.setSubject("Pending Invitation");
		aData.setContent(GlobalSettings.get("mail.ftl.invitations"));
		aData.setUrl(GlobalSettings.get("contextpath"));
		if(Mail.sendMail(aData)){
			TokensVo aToken = tokensDao.getToken(conn, aData.getToken(), "invitation");
			if(!createInvitation(conn, account_id, aData.getEmail(), aToken.getId())){
				return false;
			}
			return true;
		} else {
			setErrorMessage(GlobalSettings.get("error.E0028"));
		}
		setErrorMessage(GlobalSettings.get("error.E0029"));
		return false;
	}
	
	private boolean createInvitation(Connection conn, int account_id, String email, int token_id) throws SQLException {
		PreparedStatement pstmt = null;
		
		try{
			pstmt = conn.prepareStatement("INSERT INTO "+this.table+" ( account_id, email, token_id )"
					+ " VALUES ( ?, ?, ?)");
			pstmt.setInt(1, account_id);
			pstmt.setString(2, email);
			pstmt.setInt(3, token_id);
			
			int insertRow = pstmt.executeUpdate();
			
			if(insertRow > 0){
				return true;
			}else {
				throw new SQLException();
			}
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			CloseUtilities.close(pstmt);
		}
	}
	public static boolean preg_match(String pattern, String content){
		return content.matches(pattern);
	}
	
	public String getByEmail(Connection conn, String strEmail) throws SQLException{
		return getSingle(conn, strEmail, "id", "email", 1, 0, false);
	}
	
	public ArrayList<InvitationsVo> getInvitations(Connection conn, int account_id) throws SQLException {
		ArrayList<InvitationsVo> array = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("SELECT * FROM "+this.table+" WHERE account_id = ?");
			pstmt.setInt(1, account_id);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				InvitationsVo invitationsVo = new InvitationsVo();
				invitationsVo.setId(rs.getInt(1));
				invitationsVo.setAccount_id(rs.getInt(2));
				invitationsVo.setEmail(rs.getString(3));
				invitationsVo.setToken_id(rs.getInt(4));
				invitationsVo.setIs_activated(rs.getInt(5));
				invitationsVo.setTime(rs.getTimestamp(6));
				
				array.add(invitationsVo);
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return array;
	}
}
