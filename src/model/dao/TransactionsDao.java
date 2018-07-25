package model.dao;

import java.sql.*;
import java.util.*;

import jdbc.*;
import model.*;
import model.vo.*;

public class TransactionsDao extends Base {
	public TransactionsDao() {
		this.table = "transactions";
	}

	private static TransactionsDao transactionsDao = new TransactionsDao();

	public static TransactionsDao getInstance() {
		return transactionsDao;
	}

	
	public ArrayList<DonationVo> getDonations(Connection conn) throws SQLException{
		ArrayList<DonationVo> donations = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null; 
		AccountsDao accountsDao = AccountsDao.getInstance();
		
		try {
			pstmt = conn.prepareStatement("SELECT SUM(t.amount) AS donation, a.username AS username, a.is_anonymous AS is_anonymous,"
					+ " ROUND(a.donate_percent, 2) AS donate_percent FROM "+this.table+" AS t LEFT JOIN "+accountsDao.table+"  AS a"
					+ " ON t.account_id = a.id LEFT JOIN blocks AS b ON t.block_id = b.id where ( (  t.type = 'Donation' AND b.confirmations >= "
					+GlobalSettings.get("confirmations")+ " ) OR t.type = 'Donation_PPS' ) GROUP BY a.username ORDER BY donation DESC " );
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DonationVo donationVo = new DonationVo();
				donationVo.setDonation(rs.getDouble("donation"));
				donationVo.setUsername(rs.getString("username"));
				donationVo.setDonate_percent(rs.getFloat("donate_percent"));
				donationVo.setIs_anonymous(rs.getInt("is_anonymous"));
				
				donations.add(donationVo);
			}
		} finally {
			CloseUtilities.close(rs);
			CloseUtilities.close(pstmt);
		}

		return null;
	}
}
