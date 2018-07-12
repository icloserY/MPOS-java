package command.auth;

import java.sql.Connection;
import java.sql.SQLException;

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
				boolean complete = AccountsDao.getInstance().register(conn, signUpVo);
				
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				CloseUtilities.close(conn);
			}
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
		
		return signUpVo;
	}
	
	public String excludedTrimAndWhiteSpace(String parameter) {
		if(parameter != null && parameter.trim().isEmpty())
			parameter = null;
		parameter = parameter.replaceAll("\\p{Z}", "");
		
		return parameter;
	}
}
