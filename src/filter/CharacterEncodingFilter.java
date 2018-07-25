package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {
	private String encoding;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		request.setCharacterEncoding(encoding);
		filterChain.doFilter(request, response);
		response.setCharacterEncoding(encoding);
	}

	@Override
	public void init(FilterConfig fiterConfig) throws ServletException {
		// TODO Auto-generated method stub
		encoding = fiterConfig.getInitParameter("encoding");
		if(encoding == null) {
			encoding = "utf-8";
		}
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
