package command.auth;

import javax.servlet.http.*;

import command.handler.*;
import model.dao.*;

public class LogOutComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		
		AccountsDao accountsDao = AccountsDao.getInstance();
		
		accountsDao.logoutUser(request);
		 
		
		return "/WEB-INF/view/Content/Login.jsp";
	}

}
