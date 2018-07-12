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
import model.dao.AccountsDao;
import model.vo.SignUpVo;

public class SignUpComHan implements ComHanInterFace{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String Content = request.getMethod().equals("GET") ? "/WEB-INF/view/Content/SignUp.jsp" 
															: request.getMethod().equals("POST") ? "/WEB-INF/view/Content/Login.jsp"
															: null;
		if(request.getMethod().equals("POST")) {
			SignUpVo signUpVo = mappingToRequest(request);
			Connection conn = null;
			try {
				conn = JDBCConnection.getConnection();
				//boolean complete = AccountsDao.getInstance().register(conn, signUpVo);
				
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				CloseUtilities.close(conn);
			}
		} else if(request.getMethod().equals("GET")) {
			List<Map<String, String>> popups = new ArrayList<>();
			Map<String, String> popup = new HashMap<>();
			popup.put("Content", "Login Data");
			popup.put("ID", "lastlogin");
			popup.put("Type", "alert alert-info");
			popup.put("Dismiss", "yes");
			popups.add(popup);
			popup = new HashMap<>();
			popup.put("Content", "warning your account is dangerous");
			popup.put("ID", "static");
			popup.put("Type", "alert alert-warning");
			popup.put("Dismiss", "no");
			popups.add(popup);
			popup = new HashMap<>();
			popup.put("Content", "Test danger");
			popup.put("ID", "static");
			popup.put("Type", "alert alert-warning");
			popup.put("Dismiss", "no");
			popups.add(popup);
			request.setAttribute("Errors", popups);
		}
		return Content;
	}
	
	public SignUpVo mappingToRequest(HttpServletRequest request) {
		SignUpVo signUpVo = new SignUpVo();
		signUpVo.setUsername(excludedTrimAndWhiteSpace(request.getParameter("username")));
		signUpVo.setCoinaddress(excludedTrimAndWhiteSpace(request.getParameter("coinaddress")));
		signUpVo.setPassword(excludedTrimAndWhiteSpace(request.getParameter("password1")));
		signUpVo.setEmail1(excludedTrimAndWhiteSpace(request.getParameter("email1")));
		signUpVo.setEmail2(excludedTrimAndWhiteSpace(request.getParameter("email2")));
		signUpVo.setPin(excludedTrimAndWhiteSpace(request.getParameter("pin")));
		signUpVo.setTac(excludedTrimAndWhiteSpace(request.getParameter("tac")));
		System.out.println(signUpVo.getTac());
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
