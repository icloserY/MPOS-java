package command.help;

import java.sql.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;

public class GettingstartedComHan implements ComHanInterFace{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = null;
		
		request.setAttribute("algorithm", GlobalSettings.get("algorithm"));
		request.setAttribute("SITESTRATUMURL", GlobalSettings.get("gettingstarted.stratumurl"));
		request.setAttribute("SITESTRATUMPORT", GlobalSettings.get("gettingstarted.stratumport"));
		request.setAttribute("SITECOINNAME", GlobalSettings.get("gettingstarted.coinname"));
		request.setAttribute("SITECOINURL", GlobalSettings.get("gettingstarted.coinurl"));
		
		SettingsDao settingsDao = SettingsDao.getInstance();
		AccountsDao accountsDao = AccountsDao.getInstance();
		
		Connection conn = null;
		
		try{
			conn = JDBCConnection.getConnection();
			switch(settingsDao.getValue(conn, "acl_show_help_loggedin")){
			case "0":
				Content = "/WEB-INF/view/Content/Gettingstarted.jsp";
				break;
			case "1":
			case "":
				if(accountsDao.isAuthenticated(conn, true, request)){
					Content = "/WEB-INF/view/Content/Gettingstarted.jsp";
				}else{
					Content = "Login.do";
				}
				break;
			}
			
			
			
		}catch (NamingException | SQLException e) {
			e.printStackTrace();
		}finally{
			CloseUtilities.close(conn);
		}
		
		
		return Content;
	}

}
