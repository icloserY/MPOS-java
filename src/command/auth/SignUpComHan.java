package command.auth;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;
import model.vo.*;

public class SignUpComHan implements ComHanInterFace{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String Content = request.getMethod().equals("GET") ? "/WEB-INF/view/Content/SignUp.jsp" 
															: request.getMethod().equals("POST") ? "Login.do"
															: null;
		boolean flag = true;
		if(request.getMethod().equals("POST")) {
			@SuppressWarnings("unchecked")
			List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
					? new ArrayList<Map<String, String>>() : (ArrayList<Map<String, String>>) request.getAttribute("Errors");
			Map<String, String> popup = null;
			SignUpVo signUpVo = mappingToRequest(request);
			AccountsDao accountsDao = AccountsDao.getInstance();
			Connection conn = null;
			try {
				conn = JDBCConnection.getConnection();
				boolean complete = accountsDao.register(conn, signUpVo);
				if(complete) {
					Content = "Login.do";
				}else {
					flag = false;
					Content = "/WEB-INF/view/Content/SignUp.jsp";
				}
			} catch (NamingException|SQLException e) {
				flag = false;
				Content = "/WEB-INF/view/Content/SignUp.jsp";
				e.printStackTrace();
			} finally {
				if(flag) {
					popup = Popup.getPopup("Complete Signup: " + "Check your email and confirm your registration", GlobalSettings.get("popup.info"), null, "yes");
				}else {
					popup = Popup.getPopup("Unable to signup: " + accountsDao.getError(), GlobalSettings.get("popup.danger"), null, null);
				}
				popups.add(popup);
				request.setAttribute("Errors", popups);
				CloseUtilities.close(conn);
			}
		}
		return Content;
	}
	
	public SignUpVo mappingToRequest(HttpServletRequest request) {
		SignUpVo signUpVo = new SignUpVo();
		signUpVo.setUsername(excludedTrimAndWhiteSpace(request.getParameter("username")));
		signUpVo.setCoinaddress(excludedTrimAndWhiteSpace(request.getParameter("coinaddress")));
		signUpVo.setPassword1(excludedTrimAndWhiteSpace(request.getParameter("password1")));
		signUpVo.setPassword2(excludedTrimAndWhiteSpace(request.getParameter("password2")));
		signUpVo.setEmail1(excludedTrimAndWhiteSpace(request.getParameter("email1")));
		signUpVo.setEmail2(excludedTrimAndWhiteSpace(request.getParameter("email2")));
		signUpVo.setPin(excludedTrimAndWhiteSpace(request.getParameter("pin")));
		signUpVo.setTac(excludedTrimAndWhiteSpace(request.getParameter("tac")));
		return signUpVo;
	}
	
	public String excludedTrimAndWhiteSpace(String parameter) {
		if(parameter != null && !parameter.trim().isEmpty()) {
			parameter = parameter.replaceAll("\\p{Z}", "");
		}else {
			parameter = null;
		}
		return parameter;
	}
}