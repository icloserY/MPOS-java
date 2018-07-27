package model.dao;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

import jdbc.*;
import model.*;
import model.vo.*;

public class Pool_workerDao extends Base {
	public Pool_workerDao() {
		this.table = "pool_worker";
	}
	
	private static Pool_workerDao pool_workerDao = new Pool_workerDao();

	public static Pool_workerDao getInstance() {
		return pool_workerDao;
	} {

}

	public boolean deleteWorker(Connection conn, int account_id, String paramId) {
		int deleteRow = 0;
		boolean returnValue = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("Delete from "+this.table+" where account_id = ? and id = ? LIMIT 1");
			pstmt.setInt(1, account_id);
			pstmt.setInt(2, Integer.parseInt(paramId));
			
			deleteRow = pstmt.executeUpdate();
			if(deleteRow != 0){
				returnValue = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			setErrorMessage(GlobalSettings.get("error.E0061"));
		} finally {
			CloseUtilities.close(pstmt);
		}
		return returnValue;
	}

	public boolean addWorker(Connection conn, int account_id, String paramName, String paramPass) {
		int insertRow = 0;
		boolean returnValue = false;
		PreparedStatement pstmt = null;
		AccountsDao accountsDao = AccountsDao.getInstance();
		
		if(paramName.trim().equals("") || paramPass.trim().equals("")){
			setErrorMessage(GlobalSettings.get("error.E0058"));
			return false;
		}
		if(!checkName(paramName)){
			setErrorMessage(GlobalSettings.get("error.E0072"));
			return false;
		}
		
		try {
			String username = accountsDao.getUserName(conn, account_id);
			String workerName = username + "." + paramName;
			if(workerName.length() > 50){
				setErrorMessage(GlobalSettings.get("error.E0073"));
				return false;
			}
			
			pstmt = conn.prepareStatement("INSERT INTO "+this.table+" (account_id, username, password) VALUES (?, ?, ?)");
			pstmt.setInt(1, account_id);
			pstmt.setString(2, workerName);
			pstmt.setString(3, paramPass);
			
			
			insertRow = pstmt.executeUpdate();
			if(insertRow != 0){
				returnValue = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			if(e.getSQLState().equals("23000")){
				setErrorMessage(GlobalSettings.get("error.E0059"));
			}else{
				setErrorMessage(GlobalSettings.get("error.E0060"));
			}
		} finally {
			CloseUtilities.close(pstmt);
		}
		return returnValue;
	}
	
	
	public boolean checkName(String name) {
		String regex = "^[a-zA-Z0-9]*$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(name);
		boolean isNormal = m.matches();
		return isNormal;
	}

	public boolean updateWorkers(Connection conn, int account_id, ArrayList<WorkerVo> workerVos) {
		
		if(workerVos.isEmpty()){
			setErrorMessage("No workers to update");
			return false;
		}
		
		PreparedStatement pstmt = null;
		AccountsDao accountsDao = AccountsDao.getInstance();
		
		try {
			String username = accountsDao.getUserName(conn, account_id);
			int iFailed = 0;
			for (WorkerVo workerVo : workerVos) {
				if (workerVo.getUsername().trim().equals("") || workerVo.getPassword().trim().equals("")) {
					iFailed++;
				} else {
					if (!checkName(workerVo.getUsername())) {
						iFailed++;
						continue;
					}
					workerVo.setUsername(username + "." + workerVo.getUsername());
					pstmt = conn.prepareStatement("UPDATE "+this.table+" set password = ?, username = ?, monitor = ? where account_id = ? and id = ? LIMIT 1");
					pstmt.setString(1, workerVo.getPassword());
					pstmt.setString(2, workerVo.getUsername());
					pstmt.setInt(3, Integer.parseInt(workerVo.getMonitor()));
					pstmt.setInt(4, account_id);
					pstmt.setInt(5, workerVo.getId());
					int updateRow = pstmt.executeUpdate();
					if(updateRow == 0){
						iFailed++;
					}
				}
			}
			
			if(iFailed == 0){
				return true;
			}
			setErrorMessage("Failed to update "+ String.valueOf(iFailed) +" workers");
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setErrorMessage("Failed to update workers");
		}finally {
			CloseUtilities.close(pstmt);
		}
		
		
		return false;
	}

	
	public ArrayList<WorkerVo> getWorkers(Connection conn, int account_id) {
		int interval = 600;
		
		ArrayList<WorkerVo> workers = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement(" SELECT id, username, password, monitor, ( SELECT COUNT(id) FROM shares"
					+ " WHERE our_result = 'Y' AND username = w.username AND time > DATE_SUB(now(), INTERVAL ? SECOND)"
					+ " ) + ( SELECT COUNT(id) FROM shares_archive WHERE our_result = 'Y' AND username = w.username AND time > DATE_SUB(now(), INTERVAL ? SECOND)"
					+ " ) AS count_all, ( SELECT IFNULL(SUM(difficulty), 0) FROM shares WHERE username = w.username AND our_result = 'Y' AND time > DATE_SUB(now(), INTERVAL ? SECOND)"
					+ " ) + ( SELECT IFNULL(SUM(difficulty), 0) FROM shares_archive WHERE username = w.username AND our_result = 'Y' AND time > DATE_SUB(now(), INTERVAL ? SECOND)"
					+ " ) AS shares FROM "+this.table+" AS w WHERE account_id = ?" );
			
			pstmt.setInt(1, interval);
			pstmt.setInt(2, interval);
			pstmt.setInt(3, interval);
			pstmt.setInt(4, interval);
			pstmt.setInt(5, account_id);
			
			rs = pstmt.executeQuery();
			workers = new ArrayList<WorkerVo>();
			Coin_Base coin_base = new Coin_Base();
			while(rs.next()){
				DecimalFormat formatter = new DecimalFormat("#0.00");
				WorkerVo worker = new WorkerVo();
				worker.setHashrate(Double.parseDouble(formatter.format(Math.round(coin_base.calcHashrate(rs.getInt("shares"), interval)))));
				if(rs.getInt("count_all") > 0){
					worker.setDifficulty(Double.parseDouble(formatter.format(Math.round(rs.getInt("shares")/rs.getInt("count_all")))));
				}else{
					worker.setDifficulty(0.00);
				}
				worker.setId(rs.getInt("id")); 
				String userName = rs.getString("username");
				worker.setUsername(userName.substring(userName.indexOf(".")+1, userName.length())); 
				worker.setPassword(rs.getString("password"));
				worker.setMonitor(String.valueOf(rs.getInt("monitor")));
				workers.add(worker);
				
			}
			 
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		
		return workers;
	}
	
	public int getCountAllActiveWorkers(Connection conn, int interval) {
		if(interval < 0) {
			interval = 120;
		}
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int result = 0;
	    try {
	    	pstmt = conn.prepareStatement(""
	    		+ "SELECT COUNT(DISTINCT(username)) AS total "
	    		+ "FROM shares "
	    		+ "WHERE our_result = 'Y' "
	    		+ "AND time > DATE_SUB(now(), INTERVAL ? SECOND)");
	    	pstmt.setInt(1, interval);
	    	rs = pstmt.executeQuery();
	    	rs.next();
	    	result = rs.getInt(1);
	    }catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return result;
	  }
}
