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

public class InvitationsComHan implements ComHanInterFace{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = null;
		@SuppressWarnings("unchecked")
		List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
				? new ArrayList<Map<String, String>>()
				: (ArrayList<Map<String, String>>) request.getAttribute("Errors");

		Map<String, String> popup = new HashMap<>();

		AccountsDao accountsDao = AccountsDao.getInstance();
		SettingsDao settingsDao = SettingsDao.getInstance();
		InvitationsDao invitationsDao = InvitationsDao.getInstance();
		
		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			String doParam = "";
			MailVo data = new MailVo();
			doParam = request.getParameter("do");
			data.setMessage(request.getParameter("message"));
			data.setEmail(request.getParameter("email"));
			AccountsVo userdata = (AccountsVo) request.getSession().getAttribute("USERDATA");
			
			if(accountsDao.isAuthenticated(conn, true, request)){
				if(settingsDao.getValue(conn, "disable_invitations").equals("")){
					if(invitationsDao.getCountInvitations(conn, userdata.getId()) >= Integer.parseInt(GlobalSettings.get("accounts.invitations.count"))){
						popup = Popup.getPopup("You have exceeded the allowed invitations of "+ GlobalSettings.get("accounts.invitations.count") , "alert alert-danger", null, null);
						popups.add(popup);
					} else if(doParam != null && doParam.equals("sendInvitation")){
						if(invitationsDao.sendInvitation(conn, userdata.getId(), data )){
							popup = Popup.getPopup("Invitation sent" , "alert alert-success", null, null);
							popups.add(popup);
						} else {
							popup = Popup.getPopup("Unable to send invitation to recipient: " + invitationsDao.getError() , "alert alert-danger", null, null);
							popups.add(popup);
						}
					} 
					ArrayList<InvitationsVo> aInvitations = invitationsDao.getInvitations(conn, userdata.getId());
					request.setAttribute("INVITATIONS", aInvitations);
				} else {
					popup = Popup.getPopup("Invitations are disabled", "alert alert-danger", null, null);
					popups.add(popup);
				}
			}
			request.setAttribute("email", request.getParameter("email"));
			request.setAttribute("message", request.getParameter("message"));
			Content = "/WEB-INF/view/Content/account/Invitations.jsp";
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			request.setAttribute("Errors", popups);
			CloseUtilities.close(conn);
		}
		return Content;
	}

}
