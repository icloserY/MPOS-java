package command.account;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.http.*;

import command.handler.*;
import jdbc.*;
import model.*;
import model.dao.*;
import model.vo.*;

public class MyworkersComHan implements ComHanInterFace {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		String Content = null;
		@SuppressWarnings("unchecked")
		List<Map<String, String>> popups = (ArrayList<Map<String, String>>) request.getAttribute("Errors") == null
				? new ArrayList<Map<String, String>>()
				: (ArrayList<Map<String, String>>) request.getAttribute("Errors");

		Map<String, String> popup = new HashMap<>();

		AccountsDao accountsDao = AccountsDao.getInstance();
		SettingsDao settingsDao = SettingsDao.getInstance();
		Coin_addressesDao coin_addressesDao = Coin_addressesDao.getInstance();
		Pool_workerDao pool_workerDao = Pool_workerDao.getInstance();

		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			if (accountsDao.isAuthenticated(conn, true, request)) {
				AccountsVo accountsVo = request.getSession().getAttribute("USERDATA") != null
						? (AccountsVo) request.getSession().getAttribute("USERDATA") : null;

				if (!coin_addressesDao.getCoinAddress(conn, accountsVo.getId()).equals("")
						&& !settingsDao.getValue(conn, "disable_worker_edit").equals("")) {
					popup = Popup.getPopup("You have no payout address set.", "alert alert-danger", null, null);
					popups.add(popup);
					popup = Popup.getPopup(
							"You can not add workers unless a valid Payout Address is set in your User Settings.",
							"alert alert-danger", null, null);
					popups.add(popup);
					Content = null;
				} else {
					String doParam = request.getParameter("do");
					if (doParam != null) {
						switch (doParam) {
						case "delete":
							String paramId = request.getParameter("id");
							if (pool_workerDao.deleteWorker(conn, accountsVo.getId(), paramId)) {
								popup = Popup.getPopup("Worker removed", "alert alert-success", null, null);
								popups.add(popup);
							} else {
								popup = Popup.getPopup(pool_workerDao.getError(), "alert alert-danger", null, null);
								popups.add(popup);
							}
							break;
						case "add":
							String paramName = request.getParameter("username");
							String paramPass = request.getParameter("password");
							if (pool_workerDao.addWorker(conn, accountsVo.getId(), paramName, paramPass)) {
								popup = Popup.getPopup("Worker added", "alert alert-success", null, null);
								popups.add(popup);
							} else {
								popup = Popup.getPopup(pool_workerDao.getError(), "alert alert-danger", null, null);
								popups.add(popup);
							}
							break;
						case "update":
							ArrayList<WorkerVo> data = getWorkerVos(request);
							if (pool_workerDao.updateWorkers(conn, accountsVo.getId(), data)) {
								popup = Popup.getPopup("Worker updated", "alert alert-success", null, null);
								popups.add(popup);
							} else {
								popup = Popup.getPopup(pool_workerDao.getError(), "alert alert-danger", null, null);
								popups.add(popup);
							}
							break;
						}
					}

					ArrayList<WorkerVo> aWorkers = pool_workerDao.getWorkers(conn, accountsVo.getId());

					if (aWorkers == null || aWorkers.isEmpty()) {
						popup = Popup.getPopup("You have no workers configured", "alert alert-danger", null, null);
						popups.add(popup);
					}
					request.setAttribute("WORKERS", aWorkers);

					Content = "/WEB-INF/view/Content/account/Myworkers.jsp";
				}

			}else{
				Content = "Login.do";
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			request.setAttribute("Errors", popups);
			CloseUtilities.close(conn);
		}

		return Content;

	}

	public ArrayList<WorkerVo> getWorkerVos(HttpServletRequest request) {
		ArrayList<WorkerVo> data = new ArrayList<WorkerVo>();

		String[] workers = request.getParameterValues("workers");
		for (String workerId : workers) {
			WorkerVo workerVo = new WorkerVo();

			workerVo.setId(Integer.parseInt(workerId));
			workerVo.setUsername(request.getParameter(workerId + ".username"));
			workerVo.setPassword(request.getParameter(workerId + ".password"));
			String monitor = request.getParameter(workerId + ".monitor");
			
			if (monitor != null) {
				workerVo.setMonitor(monitor);
			} else {
				workerVo.setMonitor("0");
			}

			data.add(workerVo);
		}

		return data;
	}

}
