package controller;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import command.handler.*;
import model.*;

public class DispatcherServlet extends HttpServlet {
	private Map<String, ComHanInterFace> commandHandlerMap = new HashMap<>();
	private Map<String, String> globalSettings = GlobalSettings.getInstance();
	
	public void init() throws ServletException {
		String configFile = getInitParameter("configFile");
		Properties prop = new Properties();
		String configFilePath = getServletContext().getRealPath(configFile);
		try (FileReader fis = new FileReader(configFilePath)) {
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Iterator<Object> keyIter = prop.keySet().iterator();
		while(keyIter.hasNext()) {
			String command = (String)keyIter.next();
			String handlerClassName = prop.getProperty(command);
			try {
				Class<?> handlerClass = Class.forName(handlerClassName);
				ComHanInterFace handlerInstance =
						(ComHanInterFace)handlerClass.newInstance();
				commandHandlerMap.put(command, handlerInstance);
			} catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		configFile = getInitParameter("GlobalSettings");
		prop = new Properties();
		configFilePath = getServletContext().getRealPath(configFile);
		try (FileReader fis = new FileReader(configFilePath)) {
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		keyIter = prop.keySet().iterator();
		while(keyIter.hasNext()) {
			String key = (String)keyIter.next();
			String value = prop.getProperty(key);
			globalSettings.put(key, value);
		}
		globalSettings.replace("mail.contextpath", getServletContext().getRealPath("/WEB-INF/view/mail"));
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}	
	
	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "http://" + request.getLocalAddr() + ":" + request.getLocalPort() + request.getContextPath();
		if(!globalSettings.get("contextpath").equals(url)) {
			globalSettings.replace("contextpath", url);
		}
		String command = request.getRequestURI();
		if(command.indexOf(request.getContextPath()) == 0) {
			command = command.substring(request.getContextPath().length());
		}
		ComHanInterFace handler = commandHandlerMap.get(command);
		if(handler == null) {
			handler = new NullComHan();
		}
		String Content = null;
		Content = handler.process(request, response);
		if(Content != null && Content.endsWith(".do")){
			RequestDispatcher dispatcher = request.getRequestDispatcher(Content);
			dispatcher.forward(request, response);
			
		}else{
			String activeSide = getSide(command);
			request.setAttribute("Content", Content);
			request.setAttribute(activeSide, true);
			request.setAttribute("titleValue", activeSide);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
			dispatcher.forward(request, response);
		}
	}
	private String getSide(String command) {
		String activeSide = null;
		
		if(command.equals("/EditAccount.do") || command.equals("/Myworkers.do") || command.equals("/Transactions.do") || command.equals("/Earnings.do") || command.equals("/Notifications.do")
				|| command.equals("/Invitations.do") || command.equals("/QRcodes.do")){
			activeSide = "Account";
		}else if(command.equals("")){
			
		}else if(command.equals("/Pool.do") || command.equals("/Blocks.do") || command.equals("/Round.do") || command.equals("/Blockfinder.do") || command.equals("/Uptime.do") || command.equals("/Graphs.do") || command.equals("/Donors.do") ){
			activeSide = "Statistics";
		}else if(command.equals("/Gettingstarted.do") || command.equals("/About.do")){
			activeSide = "Help";
		}else if(command.equals("/Logout.do") || command.equals("/Contactform.do") || command.equals("/Tac.do") || command.equals("/Login.do") || command.equals("/SignUp.do")){
			activeSide = "Other";
		}else{
			activeSide = "";
		}
		
		return activeSide;
	}
}
