package command.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import command.handler.ComHanInterFace;

public class SignUpComHan implements ComHanInterFace{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String Content = request.getMethod().equals("GET") ? "/WEB-INF/view/Content/SignUp.jsp" 
															: request.getMethod().equals("POST") ? "/WEB-INF/view/Content/Login.jsp"
															: null;
		
		return Content;
	}
	
}
