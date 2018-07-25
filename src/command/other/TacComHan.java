package command.other;

import javax.servlet.http.*;

import command.handler.*;
import model.*;

public class TacComHan implements ComHanInterFace{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("websiteName", GlobalSettings.get("website.name"));
		request.setAttribute("payout_system", GlobalSettings.get("payout_system"));
		request.setAttribute("fees", GlobalSettings.get("fees"));
		
		return "/WEB-INF/view/Content/Tac.jsp";
	}

}
