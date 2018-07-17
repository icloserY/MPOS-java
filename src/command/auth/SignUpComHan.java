package command.auth;

import java.sql.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.vo.*;

public class SignUpComHan implements ComHanInterFace{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String Content = request.getMethod().equals("GET") ? "/WEB-INF/view/Content/SignUp.jsp" 
															: request.getMethod().equals("POST") ? "Login.do"
															: null;
		if(request.getMethod().equals("POST")) {
			SignUpVo signUpVo = mappingToRequest(request);
			Connection conn = null;
			try {
				conn = JDBCConnection.getConnection();
				//boolean complete = AccountsDao.getInstance().register(conn, signUpVo);
				
			} catch (NamingException|SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
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