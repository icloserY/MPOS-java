package model.dao;
  
import java.sql.*;

import jdbc.*;

public class Base {
	protected String table = " ";
	private String error = " ";
	
	public void setErrorMessage(String error){
		this.error = error;
	}
	
	public String getError(){
		return this.error;
	}
	
	/* table 레코드 수 리턴 */
	public int getCount(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pstmt = conn.prepareStatement("Select COUNT(id) as count From ?");
			pstmt.setString(1, this.table);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}else {
				throw new SQLException();
			}
		}finally {
			CloseUtilities.close(rs); 
			CloseUtilities.close(pstmt);
		}
		return count;
	}
	
	/* 필드 값 하나 검색
	 * 
	 * param
	 * conn : Connection 
	 * value : 검색조건 값
	 * search : 리턴할 필드
	 * field : 검색조건 필드
	 * valueType : 검색 값 int(0),String(1) 구분
	 * searchType : 리턴할 필드 int(0), String(1) 구분
	 * lower : 검색값 대소문자 구분 여부
	 * 
	 * */
	protected String getSingle(Connection conn, String value, String search, 
			String field, int valueType, int searchType, boolean lower) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String resultValue = ""; 
		try {
			pstmt = lower ? conn.prepareStatement("Select ? From ? where LOWER(?) = LOWER(?) LIMIT 1")
			 : conn.prepareStatement("Select ? From ? where ? = ? LIMIT 1");
			pstmt.setString(1, search);
			pstmt.setString(2, this.table);
			pstmt.setString(3, field);
			if(valueType == 0){
				pstmt.setInt(4, Integer.parseInt(value));
			}else {
				pstmt.setString(4, value);
			}
			rs = pstmt.executeQuery();
			if(rs.next()) {
				resultValue = (searchType == 0) ? String.valueOf(rs.getInt(1)) : rs.getString(1);
			}else {
				throw new SQLException();
			}
		}finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}
		return resultValue;
	}
	
	
	/* update문 실행
	 * 
	 * param
	 * conn : Connection 
	 * id : userId account id
	 * name : 변경할 필드명
	 * value : 변경할 값
	 * valueType : 변경값의 타입 int(0), String(1)
	 * table : 데이터를 변경할 테이블 이름  ""일경우 this.table 이용
	 * 
	 * */
	protected void updateSingle(Connection conn, int id, String name, String value, 
			int valueType, String table) throws SQLException {
	    table = (table.equals("")) ? this.table : table;
	    PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement("UPDATE ? set ? = ? where id = ? LIMIT 1");
			pstmt.setString(1, table);
			pstmt.setString(2, name);
			if(valueType == 0){
				pstmt.setInt(3, Integer.parseInt(value));
			}else{
				pstmt.setString(3, value);
			}
			pstmt.setInt(4, id);
			
			int insertCount = pstmt.executeUpdate();
			if(insertCount == 0) {
				throw new SQLException();
			}
		}finally{
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt); 
		}
	}
	
}
