package command.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ComHanInterFace {
	public String process(HttpServletRequest request, HttpServletResponse response);
}
