package filter;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class LoginCheckFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession();
		boolean login = false;
		
		if(session != null) {
			if(session.getAttribute("Auth") != null) {
				login = true;
			}
		}
		//추가 적으로 로그인이 되었거나 로그인 처리 요청일 경우 통과.
		//추가 해주어야 함
		if(login) {
			filterChain.doFilter(request, response);
		} else {
			RequestDispatcher dispatcher = httpRequest.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
			
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
