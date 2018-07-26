package command.auth;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import command.handler.ComHanInterFace;
import jdbc.CloseUtilities;
import jdbc.JDBCConnection;
import model.Popup;
import model.dao.AccountsDao;
import model.dao.TokensDao;
import model.vo.TokensVo;

public class ComfirmComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		String Content = "/WEB-INF/view/Content/Login.jsp";
		request.setAttribute("token", token);
		// POST 방식으로 넘어왔을 때
		if (request.getMethod().equals("GET")) {
			@SuppressWarnings("unchecked")
			List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
					? new ArrayList<Map<String, String>>()
					: (ArrayList<Map<String, String>>) request.getAttribute("Errors");

			Map<String, String> popup = new HashMap<>();

			AccountsDao accountsDao = AccountsDao.getInstance();
			TokensDao tokensDao = TokensDao.getInstance();
			Connection conn = null;
			try {
				conn = JDBCConnection.getConnection();
				conn.setAutoCommit(false);
				TokensVo tokensVo = tokensDao.getToken(conn, token, "confirm_email");
				if (tokensVo == null) {
					popup = Popup.getPopup(tokensDao.getError(), "alert alert-danger", null, null);
					popups.add(popup);
					return Content;
				}
				if (tokensDao.deleteToken(conn, token) == 1) {
					if (accountsDao.setLocked(conn, tokensVo.getAccounts_id(), 0) != 1) {
						popup = Popup.getPopup("Confirm registration is failed! Please try again", "alert alert-danger", null,
								null);
						popups.add(popup);
						conn.rollback();
					} else {
						popup = Popup.getPopup("Confirm registration is complete! Please login.", "alert alert-success", null,
								null);
						popups.add(popup);
						conn.commit();
					}
				} else {
					popup = Popup.getPopup(accountsDao.getError(), "alert alert-danger", null, null);
					popups.add(popup);
					conn.rollback();
				}
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} finally {
				request.setAttribute("Errors", popups);
				CloseUtilities.close(conn);
			}
		}

		return Content;
	}
}